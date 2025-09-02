package cli

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import src.main.model.tools.cli.cli.ExecutionCommand
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.file.Path

class ExecutionCommandTest {
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
        val command = ExecutionCommand()
        command.execute(emptyList())

        val output = getOutput()
        assertTrue(output.contains("Error: Debe especificar el archivo fuente"))
        assertTrue(output.contains("Uso: execution"))
    }

    @Test
    fun `should handle non-existent file`() {
        val command = ExecutionCommand()
        command.execute(listOf("missing.txt"))

        assertTrue(getOutput().contains("Error: El archivo 'missing.txt' no existe"))
    }

    @Test
    fun `should start execution when file exists`(@TempDir tempDir: Path) {
        val sourceFile = tempDir.resolve("program.txt").toFile()
        sourceFile.writeText("println(\"Hello\");")

        val command = ExecutionCommand()

        try {
            command.execute(listOf(sourceFile.absolutePath))
        } catch (e: Exception) {
            // Expected - dependencies not available
        }

        assertTrue(getOutput().contains("Iniciando ejecución"))
    }
}
