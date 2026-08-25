package cli.src.test.kotlin

import formatteraction.FormatterAction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class FormatterActionTest {

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
    fun `test execute with insufficient args`() {
        FormatterAction().execute(listOf("only_one_file.ps"))
        val output = outputStream.toString()
        assertTrue(output.contains("Must specify the source file and the format configuration file"))
    }

    @Test
    fun `test execute with unsupported version`() {
        FormatterAction().execute(listOf("file.ps", "config.yaml", "3.0"))
        val output = outputStream.toString()
        assertTrue(output.contains("Unsupported version"))
    }

    @Test
    fun `test execute with non-existent config file`() {
        FormatterAction().execute(listOf("file.ps", "nonexistent_cfg_123.yaml", "1.0"))
        val output = outputStream.toString()
        assertTrue(output.contains("configuration file") && output.contains("does not exist"))
    }

    @Test
    fun `test execute with non-existent source file`() {
        val tempConfig = File.createTempFile("test_fmt_cfg", ".yaml")
        tempConfig.writeText("rules:\n  space_after_colon:\n    enabled: true")
        tempConfig.deleteOnExit()

        FormatterAction().execute(listOf("nonexistent_src_123.ps", tempConfig.absolutePath, "1.0"))
        val output = outputStream.toString()
        assertTrue(output.contains("source file") && output.contains("does not exist"))
    }

    @Test
    fun `test execute formatting successfully`() {
        val tempScript = File.createTempFile("test_fmt_src", ".ps")
        tempScript.writeText("let x:number=5;")
        tempScript.deleteOnExit()

        val tempConfig = File.createTempFile("test_fmt_cfg", ".yaml")
        tempConfig.writeText(
            """
            rules:
              space_after_colon:
                enabled: true
              space_before_colon:
                enabled: false
              assign_spacing:
                enabled: true
              line_break_after_semicolon:
                enabled: true
            """.trimIndent()
        )
        tempConfig.deleteOnExit()

        FormatterAction().execute(listOf(tempScript.absolutePath, tempConfig.absolutePath, "1.0"))
        val output = outputStream.toString()
        assertTrue(output.contains("Formatting rules applied successfully"))
    }
}
