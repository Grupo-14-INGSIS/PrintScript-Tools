package src.test.model.tools.cli.cli

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import src.main.model.tools.cli.cli.ValidationCommand
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.file.Path

class ValidationCommandTest {
    private val originalOut = System.out
    private val outputStream = ByteArrayOutputStream()

    @BeforeEach
    fun setUp() {
        System.setOut(PrintStream(outputStream))
    }

    @AfterEach
    fun tearDown() {
        System.setOut(originalOut)
    }

    private fun getOutput(): String = outputStream.toString()

    @Test
    fun `should require source file argument`() {
        val command = ValidationCommand()
        command.execute(emptyList())

        val output = getOutput()
        assertTrue(output.contains("Error: Debe especificar el archivo fuente"))
        assertTrue(output.contains("Uso: validation"))
    }

    @Test
    fun `should reject unsupported version`() {
        val command = ValidationCommand()
        command.execute(listOf("file.txt", "2.0"))

        assertTrue(getOutput().contains("Error: Versión no soportada"))
    }

    @Test
    fun `should handle non-existent file`() {
        val command = ValidationCommand()
        command.execute(listOf("nonexistent.txt"))

        assertTrue(getOutput().contains("Error: El archivo 'nonexistent.txt' no existe"))
    }

    @Test
    fun `should start validation when file exists`(@TempDir tempDir: Path) {
        val sourceFile = tempDir.resolve("valid.txt").toFile()
        sourceFile.writeText("let x: number = 42;")

        val command = ValidationCommand()

        try {
            command.execute(listOf(sourceFile.absolutePath))
        } catch (e: Exception) {
            // Expected - dependencies not available
        }

        assertTrue(getOutput().contains("Iniciando validación"))
    }
}
