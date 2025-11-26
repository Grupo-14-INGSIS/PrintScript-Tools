package lexer.src.test.kotlin

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import lexer.src.main.kotlin.CharSource
import lexer.src.main.kotlin.FileCharSource
import lexer.src.main.kotlin.Lexer
import java.nio.file.Path

class LexerFileTest {

    @TempDir
    lateinit var tempDir: Path

    private fun fileSource(content: String, name: String): CharSource {
        val file = tempDir.resolve(name).toFile()
        file.writeText(content)
        return FileCharSource(file)
    }

    @Test
    fun `split should process simple file content`() {
        val source = fileSource("let x = 10", "simple.txt")
        val lexer = Lexer(source)
        lexer.split()

        assertEquals(7, lexer.list.size)
        assertTrue(lexer.list.contains("let"))
        assertTrue(lexer.list.contains("x"))
        assertTrue(lexer.list.contains("="))
        assertTrue(lexer.list.contains("10"))
        assertEquals(3, lexer.list.count { it.isBlank() })
    }

    @Test
    fun `split should handle empty file`() {
        val source = fileSource("", "empty.txt")
        val lexer = Lexer(source)
        lexer.split()
        assertTrue(lexer.list.isEmpty())
    }

    @Test
    fun `split should handle large file with custom chunk size`() {
        val content = "token ".repeat(1000).trim()
        val source = fileSource(content, "large.txt")
        val lexer = Lexer(source)
        lexer.split(lotSize = 100)

        assertEquals(1999, lexer.list.size)
        assertEquals(1000, lexer.list.count { it == "token" })
        assertEquals(999, lexer.list.count { it.isBlank() })
    }

    @Test
    fun `split should handle multiline content`() {
        val content = """
        function add(a, b) {
            return a + b;
        }
        """.trimIndent()
        val source = fileSource(content, "multiline.txt")
        val lexer = Lexer(source)
        lexer.split()

        // Tokens esperados según el diseño actual del lexer
        val expectedTokens = listOf(
            "function", "add", "(", "a,", "b", ")", "{",
            "return", "a", "+", "b", ";", "}"
        )

        expectedTokens.forEach { expected ->
            assertTrue(
                lexer.list.contains(expected),
                "Expected token '$expected' not found in lexer output"
            )
        }

        // Validación de que al menos un token sea espacio o salto de línea
        assertTrue(
            lexer.list.any { it.isBlank() },
            "Expected at least one whitespace token (e.g., space or newline)"
        )
    }

    @Test
    fun `split should handle special characters`() {
        val content = """name = "José María"; age >= 25"""
        val source = fileSource(content, "special.txt")
        val lexer = Lexer(source)
        lexer.split()

        assertTrue(lexer.list.contains("name"))
        assertTrue(lexer.list.contains("="))
        assertTrue(lexer.list.any { it.contains("José") })
        assertTrue(lexer.list.any { it.contains("María") })
        assertTrue(lexer.list.contains("age"))
        assertTrue(lexer.list.any { it.contains(">") })
        assertTrue(lexer.list.contains("25"))
    }

    @Test
    fun `split should process file that spans multiple chunks`() {
        val tokens = (1..50).joinToString(" ") { "token$it" }
        val source = fileSource(tokens, "chunked.txt")
        val lexer = Lexer(source)
        lexer.split(lotSize = 20)

        assertEquals(99, lexer.list.size)
        for (i in 1..50) {
            assertTrue(lexer.list.contains("token$i"))
        }
        assertEquals(49, lexer.list.count { it.isBlank() })
    }

    @Test
    fun `split should handle file ending with incomplete token`() {
        val source = fileSource("complete token incomplete", "incomplete.txt")
        val lexer = Lexer(source)
        lexer.split()

        assertEquals(5, lexer.list.size)
        assertTrue(lexer.list.contains("complete"))
        assertTrue(lexer.list.contains("token"))
        assertTrue(lexer.list.contains("incomplete"))
        assertEquals(2, lexer.list.count { it.isBlank() })
    }

    @Test
    fun `split should process file content with constructor-like flow`() {
        val file = tempDir.resolve("constructor.txt").toFile()
        file.writeText("constructor test")
        val source = FileCharSource(file)
        val lexer = Lexer(source)

        lexer.split()

        assertEquals(3, lexer.list.size)
        assertTrue(lexer.list.contains("constructor"))
        assertTrue(lexer.list.contains("test"))
        assertEquals(1, lexer.list.count { it.isBlank() })
    }

    @Test
    fun `createToken should work after split`() {
        val source = fileSource("let x = 10", "token.txt")
        val lexer = Lexer(source)
        val container = lexer.lexIntoStatements().first()
        assertNotNull(container)
        assertEquals(7, container.size())
    }
}
