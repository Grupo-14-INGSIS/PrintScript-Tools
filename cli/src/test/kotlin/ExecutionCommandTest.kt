import org.junit.jupiter.api.Assertions.assertTrue
import src.main.model.tools.cli.cli.ExecutionCommand
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.test.Test

class ExecutionCommandTest {
    @Test
    fun `no arguments prints usage`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val command = ExecutionCommand()
        command.execute(emptyList())

        val output = outputStream.toString().trim()
        assertTrue(output.contains("Must specify the source file."))
        assertTrue(output.contains("Usage: execution <source_file> [version]"))
    }

    @Test
    fun `unsupported version prints error`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val command = ExecutionCommand()
        command.execute(listOf("file.txt", "2.0"))

        val output = outputStream.toString().trim()
        assertTrue(output.contains("Error: Unsupported version. Only 1.0 and 1.1 are supported."))
    }

    @Test
    fun `source file does not exist`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val command = ExecutionCommand()
        command.execute(listOf("nonexistent.txt"))

        val output = outputStream.toString().trim()
        assertTrue(output.contains("Error: The source file 'nonexistent.txt' does not exist."))
    }

    @Test
    fun `successful execution prints completion`() {
        val sourceFile = File.createTempFile("source", ".txt").apply {
            writeText("print(1)") // Asegurate que esto sea válido para tu lexer/parser
        }

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val command = ExecutionCommand()
        command.execute(listOf(sourceFile.absolutePath))

        val output = outputStream.toString()
        assertTrue(output.contains("Starting execution of"))
        assertTrue(output.contains("Program executed successfully"))
    }
}
