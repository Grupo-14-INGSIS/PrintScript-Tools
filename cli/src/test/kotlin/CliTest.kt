package cli
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import src.main.model.tools.cli.cli.*

class CliTest {
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
    fun `should show error when no command specified`() {
        val cli = Cli(emptyMap())
        cli.run(arrayOf())

        assertTrue(getOutput().contains("Debe especificar un comando"))
    }

    @Test
    fun `should show error for unknown command`() {
        val cli = Cli(emptyMap())
        cli.run(arrayOf("unknown"))

        assertTrue(getOutput().contains("Comando desconocido: unknown"))
    }

    @Test
    fun `should execute existing command`() {
        var executed = false
        val testCommand = object : Command {
            override fun execute(args: List<String>) {
                executed = true
            }
        }

        val cli = Cli(mapOf("test" to testCommand))
        cli.run(arrayOf("test"))

        assertTrue(executed)
    }
}










