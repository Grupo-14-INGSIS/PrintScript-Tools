package src.main.model.tools.interpreter.lexer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

class LexerFileTest {

    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `splitStringFromFile should process simple file content`() {
        // Given
        val testFile = tempDir.resolve("test.txt").toFile()
        testFile.writeText("let x = 10")
        val lexer = Lexer("")

        // When
        lexer.splitStringFromFile(testFile)

        // Then - "let x = 10" = let + espacio + x + espacio + = + espacio + 10 = 7 tokens
        assertEquals(7, lexer.list.size)
        assertTrue(lexer.list.contains("let"))
        assertTrue(lexer.list.contains("x"))
        assertTrue(lexer.list.contains("="))
        assertTrue(lexer.list.contains("10"))
        // Y 3 espacios
        assertEquals(3, lexer.list.count { it.isBlank() })
    }

    @Test
    fun `splitStringFromFile should handle empty file`() {
        // Given
        val testFile = tempDir.resolve("empty.txt").toFile()
        testFile.writeText("")
        val lexer = Lexer("")

        // When
        lexer.splitStringFromFile(testFile)

        // Then
        assertTrue(lexer.list.isEmpty())
    }

    @Test
    fun `splitStringFromFile should handle large file with custom chunk size`() {
        // Given
        val testFile = tempDir.resolve("large.txt").toFile()
        val content = "token ".repeat(1000) // 1000 palabras "token" + 999 espacios = 1999 tokens
        testFile.writeText(content.trim()) // trim quita el último espacio
        val lexer = Lexer("")

        // When
        lexer.splitStringFromFile(testFile, lotSize = 100)

        // Then - 1000 palabras "token" + 999 espacios = 1999 tokens
        assertEquals(1999, lexer.list.size)
        assertEquals(1000, lexer.list.count { it == "token" })
        assertEquals(999, lexer.list.count { it.isBlank() })
    }

    @Test
    fun `splitStringFromFile should handle multiline content`() {
        // Given
        val testFile = tempDir.resolve("multiline.txt").toFile()
        val content = """
            function add(a, b) {
                return a + b;
            }
        """.trimIndent()
        testFile.writeText(content)
        val lexer = Lexer("")

        // When
        lexer.splitStringFromFile(testFile)

        // Then - verificar que contiene las palabras clave (sin contar espacios exactos)
        assertTrue(lexer.list.contains("function"))
        assertTrue(lexer.list.contains("add"))
        assertTrue(lexer.list.contains("("))
        assertTrue(lexer.list.contains("a,"))
        assertTrue(lexer.list.contains(" "))
        assertTrue(lexer.list.contains("b"))
        assertTrue(lexer.list.contains(")"))
        assertTrue(lexer.list.contains("{"))
        assertTrue(lexer.list.contains(";"))
        assertTrue(lexer.list.contains("return"))
        assertTrue(lexer.list.contains("+"))
        assertTrue(lexer.list.contains("}"))
        // Verificar que hay espacios/saltos de línea
        assertTrue(lexer.list.any { it.isBlank() })
    }

    @Test
    fun `splitStringFromFile should handle file with special characters`() {
        // Given
        val testFile = tempDir.resolve("special.txt").toFile()
        testFile.writeText("name = \"José María\"; age >= 25")
        val lexer = Lexer("")

        // When
        lexer.splitStringFromFile(testFile)

        // Then
        assertTrue(lexer.list.contains("name"))
        assertTrue(lexer.list.contains("="))
        assertTrue(
            lexer.list.contains("\"José María\"") ||
                (lexer.list.contains("José") && lexer.list.contains("María"))
        )
        assertTrue(lexer.list.contains("age"))
        assertTrue(
            lexer.list.contains(">=") ||
                (lexer.list.contains(">") && lexer.list.contains("="))
        )
        assertTrue(lexer.list.contains("25"))
    }

    @Test
    fun `splitStringFromFile should process file that spans multiple chunks`() {
        // Given
        val testFile = tempDir.resolve("chunked.txt").toFile()
        val tokens = (1..50).map { "token$it" }
        testFile.writeText(tokens.joinToString(" ")) // 50 tokens + 49 espacios = 99 tokens
        val lexer = Lexer("")

        // When
        lexer.splitStringFromFile(testFile, lotSize = 20)

        // Then - 50 palabras + 49 espacios = 99 tokens
        assertEquals(99, lexer.list.size)
        for (i in 1..50) {
            assertTrue(lexer.list.contains("token$i"))
        }
        assertEquals(49, lexer.list.count { it.isBlank() }) // 49 espacios
    }

    @Test
    fun `splitStringFromFile should handle file ending with incomplete token`() {
        // Given
        val testFile = tempDir.resolve("incomplete.txt").toFile()
        testFile.writeText("complete token incomplete") // 3 palabras + 2 espacios = 5 tokens
        val lexer = Lexer("")

        // When
        lexer.splitStringFromFile(testFile)

        // Then
        assertEquals(5, lexer.list.size)
        assertTrue(lexer.list.contains("complete"))
        assertTrue(lexer.list.contains("token"))
        assertTrue(lexer.list.contains("incomplete"))
        assertEquals(2, lexer.list.count { it.isBlank() })
    }

    @Test
    fun `fromFile constructor should work with splitStringFromFile`() {
        // Given
        val testFile = tempDir.resolve("constructor.txt").toFile()
        testFile.writeText("constructor test") // 2 palabras + 1 espacio = 3 tokens

        // When
        val lexer = Lexer.fromFile(testFile)
        lexer.list.clear() // limpiar lo que se tokenizó en el constructor
        lexer.splitStringFromFile(testFile)

        // Then
        assertEquals(3, lexer.list.size)
        assertTrue(lexer.list.contains("constructor"))
        assertTrue(lexer.list.contains("test"))
        assertEquals(1, lexer.list.count { it.isBlank() })
    }

    @Test
    fun `createToken should work after splitStringFromFile`() {
        // Given
        val testFile = tempDir.resolve("token.txt").toFile()
        testFile.writeText("let x = 10")
        val lexer = Lexer("")
        lexer.splitStringFromFile(testFile)

        // When
        val container = lexer.createToken(lexer.list)

        // Then
        assertNotNull(container)
        assertEquals(7, container.size()) // Asumiendo que Container tiene método size()
    }
}
