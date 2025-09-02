package src.test.model.tools.cli.cli

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import src.main.model.tools.cli.cli.AnalyzerCommand
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.file.Path

class AnalyzerCommandTest {
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
    fun `should show usage error when insufficient arguments`() {
        val command = AnalyzerCommand()
        command.execute(listOf("onlyOne"))

        val output = getOutput()
        assertTrue(output.contains("Error: Debe especificar el archivo fuente"))
        assertTrue(output.contains("Uso: analyzer"))
    }

    @Test
    fun `should reject unsupported version`() {
        val command = AnalyzerCommand()
        command.execute(listOf("file.txt", "config.yml", "3.0"))

        assertTrue(getOutput().contains("Error: Versión no soportada"))
    }

    @Test
    fun `should handle missing source file`(@TempDir tempDir: Path) {
        val configFile = tempDir.resolve("config.yml").toFile()
        configFile.writeText("rules: {}")

        val command = AnalyzerCommand()
        command.execute(listOf("missing.txt", configFile.absolutePath))

        assertTrue(getOutput().contains("Error: El archivo fuente 'missing.txt' no existe"))
    }

    @Test
    fun `should handle missing config file`(@TempDir tempDir: Path) {
        val sourceFile = tempDir.resolve("source.txt").toFile()
        sourceFile.writeText("some code")

        val command = AnalyzerCommand()
        command.execute(listOf(sourceFile.absolutePath, "missing.yml"))

        assertTrue(getOutput().contains("Error: El archivo de configuración"))
    }
}
