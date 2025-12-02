package lexer.src.test.kotlin

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import lexer.src.main.kotlin.CharSource
import lexer.src.main.kotlin.FileCharSource
import lexer.src.main.kotlin.StringCharSource
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
        val pieces = lexer.split().toList()

        assertEquals(7, pieces.size)
        assertTrue(pieces.contains("let"))
        assertTrue(pieces.contains("x"))
        assertTrue(pieces.contains("="))
        assertTrue(pieces.contains("10"))
        assertEquals(3, pieces.count { it.isBlank() })
    }

    @Test
    fun `split should handle empty file`() {
        val source = fileSource("", "empty.txt")
        val lexer = Lexer(source)
        val pieces = lexer.split().toList()
        assertTrue(pieces.isEmpty())
    }

    @Test
    fun `split should handle large file with custom chunk size`() {
        val content = "token ".repeat(1000).trim()
        val source = fileSource(content, "large.txt")
        val lexer = Lexer(source)
        val pieces = lexer.split().toList() // No longer accepts lotSize parameter

        assertEquals(1999, pieces.size)
        assertEquals(1000, pieces.count { it == "token" })
        assertEquals(999, pieces.count { it.isBlank() })
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
        val pieces = lexer.split().toList()

        // Tokens esperados según el diseño actual del lexer
        val expectedTokens = listOf(
            "function", " ", "add", "(", "a", ",", " ", "b", ")", " ", "{", "\n",
            " ", " ", " ", " ", "return", " ", "a", " ", "+", " ", "b", ";", "\n",
            "}"
        )

        println("ACTUAL PIECES: $pieces")
        assertEquals(expectedTokens, pieces)
    }

    @Test
    fun `split should handle special characters`() {
        val content = """name = "José María"; age >= 25"""
        val source = fileSource(content, "special.txt")
        val lexer = Lexer(source)
        val pieces = lexer.split().toList()

        assertTrue(pieces.contains("name"))
        assertTrue(pieces.contains("="))
        assertTrue(pieces.any { it.contains("José María") }) // The string literal should be one piece
        assertTrue(pieces.contains("age"))
        assertTrue(pieces.any { it.contains(">") })
        assertTrue(pieces.contains("25"))
    }

    @Test
    fun `split should process file that spans multiple chunks`() {
        val tokens = (1..50).joinToString(" ") { "token$it" }
        val source = fileSource(tokens, "chunked.txt")
        val lexer = Lexer(source)
        val pieces = lexer.split().toList()

        assertEquals(99, pieces.size)
        for (i in 1..50) {
            assertTrue(pieces.contains("token$i"))
        }
        assertEquals(49, pieces.count { it.isBlank() })
    }

    @Test
    fun `split should handle file ending with incomplete token`() {
        val source = fileSource("complete token incomplete", "incomplete.txt")
        val lexer = Lexer(source)
        val pieces = lexer.split().toList()

        assertEquals(5, pieces.size)
        assertTrue(pieces.contains("complete"))
        assertTrue(pieces.contains("token"))
        assertTrue(pieces.contains("incomplete"))
        assertEquals(2, pieces.count { it.isBlank() })
    }

    @Test
    fun `split should process file content with constructor-like flow`() {
        val file = tempDir.resolve("constructor.txt").toFile()
        file.writeText("constructor test")
        val source = FileCharSource(file)
        val lexer = Lexer(source)

        val pieces = lexer.split().toList()

        assertEquals(3, pieces.size)
        assertTrue(pieces.contains("constructor"))
        assertTrue(pieces.contains("test"))
        assertEquals(1, pieces.count { it.isBlank() })
    }

    @Test
    fun `createToken should work after split`() {
        val input = "let x = 10;"
        val lexer = Lexer(StringCharSource(input))
        val container = lexer.lexIntoStatements().first()
        assertNotNull(container)
        assertEquals(8, container.size()) // Increased size to account for the semicolon token
    }
}

