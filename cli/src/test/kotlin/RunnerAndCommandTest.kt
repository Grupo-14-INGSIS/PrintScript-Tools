package cli.src.test.kotlin

import cli.src.main.kotlin.Main
import cli.src.main.kotlin.command.AnalyzerCommand
import cli.src.main.kotlin.command.ExecutionCommand
import cli.src.main.kotlin.command.FormatterCommand
import cli.src.main.kotlin.command.ValidationCommand
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import runner.Runner
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class RunnerAndCommandTest {

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
    fun `test Command properties and execution`() {
        val execCmd = ExecutionCommand()
        assertEquals("execution", execCmd.name)
        assertNotNull(execCmd.description)
        execCmd.execute(emptyList())

        val anCmd = AnalyzerCommand()
        assertEquals("analyzer", anCmd.name)
        assertNotNull(anCmd.description)
        anCmd.execute(emptyList())

        val fmtCmd = FormatterCommand()
        assertEquals("formatter", fmtCmd.name)
        assertNotNull(fmtCmd.description)
        fmtCmd.execute(emptyList())

        val valCmd = ValidationCommand()
        assertEquals("validation", valCmd.name)
        assertNotNull(valCmd.description)
        valCmd.execute(emptyList())

        assertTrue(outputStream.toString().isNotEmpty())
    }

    @Test
    fun `test Runner delegating methods`() {
        val runner = Runner()
        runner.executionCommand(emptyList())
        runner.analyzerCommand(emptyList())
        runner.formatterCommand(emptyList())
        runner.validationCommand(emptyList())

        assertTrue(outputStream.toString().contains("Must specify"))
    }

    @Test
    fun `test Main object execution`() {
        Main.main(arrayOf("unknown_cmd"))
        val output = outputStream.toString()
        assertTrue(output.contains("Unknown command: unknown_cmd"))
    }

    @Test
    fun `test AnalyzerCommand execute with valid arguments`() {
        val tempScript = File.createTempFile("test_an_cmd", ".ps")
        tempScript.writeText("let myVar: number = 42;\nprintln(myVar);")
        tempScript.deleteOnExit()

        val tempConfig = File.createTempFile("test_an_cmd_config", ".yaml")
        tempConfig.writeText("rules:\n  identifier_format:\n    style: camelCase\n")
        tempConfig.deleteOnExit()

        val anCmd = AnalyzerCommand()
        anCmd.execute(listOf(tempScript.absolutePath, tempConfig.absolutePath, "1.0"))
        assertTrue(outputStream.toString().contains("SUCCESS: No issues were found"))
    }

    @Test
    fun `test ValidationCommand execute with valid arguments`() {
        val tempScript = File.createTempFile("test_val_cmd", ".ps")
        tempScript.writeText("let myVar: number = 42;\nprintln(myVar);")
        tempScript.deleteOnExit()

        val valCmd = ValidationCommand()
        valCmd.execute(listOf(tempScript.absolutePath, "1.0"))
        assertTrue(outputStream.toString().contains("SUCCESS: File is syntactically and semantically valid"))
    }
}

