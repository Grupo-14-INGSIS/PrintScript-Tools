package src.test.model.tools.cli.cli

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import src.main.model.tools.cli.cli.FormatterCommand
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.file.Path

class FormatterCommandTest {
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
    fun `should show error when insufficient arguments`() {
        val command = FormatterCommand()
        command.execute(listOf("onlyOneArg"))

        val output = getOutput()
        assertTrue(output.contains("Error: Debe especificar el archivo fuente"))
        assertTrue(output.contains("Uso: formatter"))
    }

    @Test
    fun `should show error for unsupported version`() {
        val command = FormatterCommand()
        command.execute(listOf("file.txt", "config.yml", "2.0"))

        assertTrue(getOutput().contains("Error: Versión no soportada"))
    }

    @Test
    fun `should show error when source file does not exist`(@TempDir tempDir: Path) {
        val configFile = tempDir.resolve("config.yml").toFile()
        configFile.writeText("test config")

        val command = FormatterCommand()
        command.execute(listOf("nonexistent.txt", configFile.absolutePath))

        assertTrue(getOutput().contains("Error: El archivo fuente 'nonexistent.txt' no existe"))
    }

    @Test
    fun `should show error when config file does not exist`(@TempDir tempDir: Path) {
        val sourceFile = tempDir.resolve("source.txt").toFile()
        sourceFile.writeText("test source")

        val command = FormatterCommand()
        command.execute(listOf(sourceFile.absolutePath, "nonexistent.yml"))

        assertTrue(getOutput().contains("Error: El archivo de configuración"))
    }
}
