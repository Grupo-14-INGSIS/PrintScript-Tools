import org.junit.jupiter.api.Test
import src.main.model.tools.cli.cli.AnalyzerCommand
import src.main.model.tools.cli.cli.Cli
import src.main.model.tools.cli.cli.Command
import src.main.model.tools.cli.cli.FormatterCommand
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertTrue

class CliTest {
    @Test
    fun `empty args`() {
        val cli = Cli(mapOf("analyze" to AnalyzerCommand(), "format" to FormatterCommand()))
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))
        cli.run(emptyArray())
        val output = outputStream.toString().trim()
        assertTrue(output.contains("Must specify a command: formatter | analyzer | validation | execution"))
    }

    @Test
    fun `valid command executes successfully`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val mockCommand = object : Command {
            override fun execute(args: List<String>) {
                println("Mock command executed with args: ${args.joinToString()}")
            }
        }

        val cli = Cli(mapOf("analyze" to mockCommand))
        cli.run(arrayOf("analyze", "file.txt", "config.yml"))

        val output = outputStream.toString().trim()
        assertTrue(output.contains("Mock command executed with args: file.txt, config.yml"))
    }

    @Test
    fun `invalid command prints error`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val cli = Cli(mapOf("analyze" to AnalyzerCommand()))
        cli.run(arrayOf("unknown", "file.txt"))

        val output = outputStream.toString().trim()
        assertTrue(output.contains("Unknown command: unknown"))
    }
}

