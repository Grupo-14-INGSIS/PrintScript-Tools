package cli.src.test.kotlin

import org.junit.jupiter.api.Test
import cli.src.main.kotlin.Cli
import cli.src.main.kotlin.command.CliCommand
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

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

    @ParameterizedTest(name = "Core command ''{0}'' is recognized")
    @ValueSource(strings = ["formatter", "validation", "analyzer", "execution"])
    fun `core commands are recognized`(command: String) {
        val cli = Cli()
        cli.run(listOf(command))

        val output = outputStream.toString().trim()
        // Ensure the CLI knows the command (doesn't throw unknown command error)
        assertFalse(
            output.contains("Unknown command"),
            "Command $command was not recognized. Output: $output"
        )
    }

    @Test
    fun `custom plugin command can be registered and executed`() {
        val cli = Cli()
        var customExecuted = false

        val customCommand = object : CliCommand() {
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
}
