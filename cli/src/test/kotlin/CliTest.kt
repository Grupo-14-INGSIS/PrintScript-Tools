package cli.src.test.kotlin

import org.junit.jupiter.api.Test
import cli.src.main.kotlin.Cli
import cli.src.main.kotlin.command.CliCommand
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertTrue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

class CliTest {

    private val originalOut = System.out
    private lateinit var outputStream: ByteArrayOutputStream

    @BeforeEach
    fun setUp() {
        outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))
    }

    @AfterEach
    fun tearDown() {
        System.setOut(originalOut)
    }

    @Test
    fun `empty args shows usage message`() {
        val cli = Cli()
        cli.run(emptyList())

        val output = outputStream.toString().trim()
        assertTrue(
            output.contains("Must specify a command: formatter | analyzer | validation | execution")
        )
    }

    @Test
    fun `invalid command prints error`() {
        val cli = Cli()
        cli.run(listOf("unknown", "file.txt"))

        val output = outputStream.toString().trim()
        assertTrue(output.contains("Unknown command: unknown"))
    }

    @Test
    fun `custom plugin command can be registered and executed`() {
        val cli = Cli()
        var customExecuted = false

        val customCommand = object : CliCommand {
            override val name: String = "custom"
            override val description: String = "A custom command plugin"
            override fun execute(args: List<String>) {
                customExecuted = true
                println("Executed custom with args: $args")
            }
        }

        cli.registerCommand(customCommand)
        assertTrue(cli.availableCommands().contains("custom"))

        cli.run(listOf("custom", "arg1", "arg2"))
        assertTrue(customExecuted)
        assertTrue(outputStream.toString().contains("Executed custom with args: [arg1, arg2]"))
    }

    @Test
    fun `execution command with missing file shows error`() {
        val cli = Cli()
        cli.run(listOf("execution", "nonexistent.ps"))

        val output = outputStream.toString().trim()
        assertTrue(
            output.contains("does not exist") ||
                output.contains("ERROR") ||
                output.contains("FileNotFoundException")
        )
    }

    @Test
    fun `analyzer command with insufficient args shows error`() {
        val cli = Cli()
        cli.run(listOf("analyzer"))

        val output = outputStream.toString().trim()
        assertTrue(
            output.contains("Must specify") ||
                output.contains("ERROR")
        )
    }

    @Test
    fun `formatter command is recognized`() {
        val cli = Cli()
        cli.run(listOf("formatter"))

        val output = outputStream.toString().trim()
        assertTrue(
            !output.contains("Unknown command") ||
                output.contains("Must specify") ||
                output.contains("ERROR")
        )
    }

    @Test
    fun `validation command is recognized`() {
        val cli = Cli()
        cli.run(listOf("validation"))

        val output = outputStream.toString().trim()
        assertTrue(
            !output.contains("Unknown command") ||
                output.contains("Must specify") ||
                output.contains("ERROR")
        )
    }
}
