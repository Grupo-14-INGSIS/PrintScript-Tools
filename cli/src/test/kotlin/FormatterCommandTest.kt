import org.junit.jupiter.api.Assertions.assertTrue
import src.main.model.tools.cli.cli.FormatterCommand
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.test.Test

class FormatterCommandTest {
    @Test
    fun `missing arguments prints usage`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val command = FormatterCommand()
        command.execute(listOf("onlySource.txt"))

        val output = outputStream.toString().trim()
        assertTrue(output.contains("Error: Must specify the source file and the format configuration file."))
        assertTrue(output.contains("Usage: formatter <source_file> <configuration_file> [version]"))
    }

    @Test
    fun `unsupported version prints error`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val command = FormatterCommand()
        command.execute(listOf("source.txt", "config.yml", "2.0"))

        val output = outputStream.toString().trim()
        assertTrue(output.contains("Error: Unsupported version. Only 1.0 is admitted."))
    }

    @Test
    fun `source file does not exist`() {
        val configFile = File.createTempFile("config", ".yml")
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val command = FormatterCommand()
        command.execute(listOf("missing_source.txt", configFile.absolutePath))

        val output = outputStream.toString().trim()
        assertTrue(output.contains("Error: The source file 'missing_source.txt' does not exist."))
    }


}
