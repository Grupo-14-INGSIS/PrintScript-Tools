package cli.src.test.kotlin

import analyzer.src.main.kotlin.Analyzer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class AnalyzerTest {

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
    fun `test executeValidation with empty args`() {
        Analyzer().executeValidation(emptyList())
        val output = outputStream.toString()
        assertTrue(output.contains("Must specify the source file"))
    }

    @Test
    fun `test execute analysis with insufficient args`() {
        Analyzer().execute(listOf("file.ps"))
        val output = outputStream.toString()
        assertTrue(output.contains("Must specify the source file and the analysis configuration file"))
    }

    @Test
    fun `test execute analysis with unsupported version`() {
        Analyzer().execute(listOf("file.ps", "config.yaml", "3.0"))
        val output = outputStream.toString()
        assertTrue(output.contains("Unsupported version"))
    }

    @Test
    fun `test execute validation with non-existent source file`() {
        Analyzer().executeValidation(listOf("nonexistent_script_9876.ps", "1.0"))
        val output = outputStream.toString()
        assertTrue(output.contains("does not exist"))
    }

    @Test
    fun `test execute analysis with non-existent config file`() {
        val tempScript = File.createTempFile("test_valid", ".ps")
        tempScript.writeText("let x: number = 5;")
        tempScript.deleteOnExit()

        Analyzer().execute(listOf(tempScript.absolutePath, "nonexistent_config_9876.yaml", "1.0"))
        val output = outputStream.toString()
        assertTrue(output.contains("configuration file") && output.contains("does not exist"))
    }

    @Test
    fun `test execute validation with valid script`() {
        val tempScript = File.createTempFile("test_valid", ".ps")
        tempScript.writeText("let myVar: number = 42;\nprintln(myVar);")
        tempScript.deleteOnExit()

        Analyzer().executeValidation(listOf(tempScript.absolutePath, "1.0"))
        val output = outputStream.toString()
        assertTrue(output.contains("SUCCESS: File is syntactically and semantically valid"))
    }

    @Test
    fun `test execute validation with syntax error`() {
        val tempScript = File.createTempFile("test_invalid", ".ps")
        tempScript.writeText("let = 5;")
        tempScript.deleteOnExit()

        Analyzer().executeValidation(listOf(tempScript.absolutePath, "1.0"))
        val output = outputStream.toString()
        assertTrue(output.contains("SYNTAX ERROR") || output.contains("Syntax validation failed") || output.contains("error"))
    }

    @Test
    fun `test execute analysis with valid rules config and no violations`() {
        val tempScript = File.createTempFile("test_lint_clean", ".ps")
        tempScript.writeText("let myVariable: number = 42;\nprintln(myVariable);")
        tempScript.deleteOnExit()

        val tempConfig = File.createTempFile("test_rules", ".yaml")
        tempConfig.writeText(
            """
            rules:
              identifier_format:
                style: camelCase
              mandatory_variable_or_literal_in_println:
                enabled: true
            """.trimIndent()
        )
        tempConfig.deleteOnExit()

        Analyzer().execute(listOf(tempScript.absolutePath, tempConfig.absolutePath, "1.0"))
        val output = outputStream.toString()
        assertTrue(output.contains("SUCCESS: No issues were found"))
    }

    @Test
    fun `test execute analysis with lint violations`() {
        val tempScript = File.createTempFile("test_lint_violation", ".ps")
        tempScript.writeText("let snake_case_var: number = 42;\nprintln(5 + 5);")
        tempScript.deleteOnExit()

        val tempConfig = File.createTempFile("test_rules_violation", ".yaml")
        tempConfig.writeText(
            """
            rules:
              identifier_format:
                style: camelCase
              mandatory_variable_or_literal_in_println:
                enabled: true
            """.trimIndent()
        )
        tempConfig.deleteOnExit()

        Analyzer().execute(listOf(tempScript.absolutePath, tempConfig.absolutePath, "1.0"))
        val output = outputStream.toString()
        assertTrue(output.contains("ANALYSIS RESULTS") && output.contains("issue(s) found"))
    }
}
