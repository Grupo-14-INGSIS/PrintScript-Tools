package src.main.model.tools.interpreter.lexer

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.io.TempDir
import java.io.File
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

        // Then
        assertEquals(4, lexer.list.size)
        assertEquals("let", lexer.list[0])
        assertEquals("x", lexer.list[1])
        assertEquals("=", lexer.list[2])
        assertEquals("10", lexer.list[3])
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
        val content = "token ".repeat(1000) // 6000 caracteres aprox
        testFile.writeText(content.trim())
        val lexer = Lexer("")

        // When
        lexer.splitStringFromFile(testFile, lotSize = 100) // chunks pequeños

        // Then
        assertEquals(1000, lexer.list.size)
        assertTrue(lexer.list.all { it == "token" })
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

        // Then
        assertTrue(lexer.list.contains("function"))
        assertTrue(lexer.list.contains("add"))
        assertTrue(lexer.list.contains("("))
        assertTrue(lexer.list.contains("a"))
        assertTrue(lexer.list.contains(","))
        assertTrue(lexer.list.contains("b"))
        assertTrue(lexer.list.contains(")"))
        assertTrue(lexer.list.contains("{"))
        assertTrue(lexer.list.contains("return"))
        assertTrue(lexer.list.contains("+"))
        assertTrue(lexer.list.contains("}"))
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
        assertTrue(lexer.list.contains("\"José María\"") || lexer.list.contains("José") && lexer.list.contains("María"))
        assertTrue(lexer.list.contains("age"))
        assertTrue(lexer.list.contains(">=") || (lexer.list.contains(">") && lexer.list.contains("=")))
        assertTrue(lexer.list.contains("25"))
    }

    @Test
    fun `splitStringFromFile should process file that spans multiple chunks`() {
        // Given
        val testFile = tempDir.resolve("chunked.txt").toFile()
        // Crear contenido que se divida en múltiples chunks
        val tokens = (1..50).map { "token$it" }
        testFile.writeText(tokens.joinToString(" "))
        val lexer = Lexer("")

        // When - usar chunk size pequeño para forzar múltiples lecturas
        lexer.splitStringFromFile(testFile, lotSize = 20)

        // Then
        assertEquals(50, lexer.list.size)
        for (i in 1..50) {
            assertTrue(lexer.list.contains("token$i"))
        }
    }

    @Test
    fun `splitStringFromFile should handle file ending with incomplete token`() {
        // Given
        val testFile = tempDir.resolve("incomplete.txt").toFile()
        testFile.writeText("complete token incomplete") // sin newline al final
        val lexer = Lexer("")

        // When
        lexer.splitStringFromFile(testFile)

        // Then
        assertTrue(lexer.list.contains("complete"))
        assertTrue(lexer.list.contains("token"))
        assertTrue(lexer.list.contains("incomplete"))
    }

    @Test
    fun `fromFile constructor should work with splitStringFromFile`() {
        // Given
        val testFile = tempDir.resolve("constructor.txt").toFile()
        testFile.writeText("constructor test")

        // When
        val lexer = Lexer.fromFile(testFile)
        lexer.list.clear() // limpiar lo que se tokenizó en el constructor
        lexer.splitStringFromFile(testFile)

        // Then
        assertEquals(2, lexer.list.size)
        assertEquals("constructor", lexer.list[0])
        assertEquals("test", lexer.list[1])
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
        // Agregar más assertions según lo que devuelva TokenFactory
    }
}