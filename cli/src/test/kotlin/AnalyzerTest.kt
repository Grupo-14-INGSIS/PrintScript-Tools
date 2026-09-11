package cli.src.test.kotlin

import analyzer.Analyzer
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


    @Test
    fun `test executeValidation with File and default version`() {
        val tempScript = File.createTempFile("test_file_val", ".ps")
        tempScript.writeText("let myVar: number = 42;\nprintln(myVar);")
        tempScript.deleteOnExit()

        Analyzer().executeValidation(tempScript)
        val output = outputStream.toString()
        assertTrue(output.contains("SUCCESS: File is syntactically and semantically valid"))
    }

    @Test
    fun `test executeValidation with File and explicit version 1_1`() {
        val tempScript = File.createTempFile("test_file_val_11", ".ps")
        tempScript.writeText("const myVar: boolean = true;\nprintln(myVar);")
        tempScript.deleteOnExit()

        Analyzer().executeValidation(tempScript, "1.1")
        val output = outputStream.toString()
        assertTrue(output.contains("SUCCESS: File is syntactically and semantically valid"))
    }

    @Test
    fun `test execute with File and default version`() {
        val tempScript = File.createTempFile("test_file_exec", ".ps")
        tempScript.writeText("let myVar: number = 42;\nprintln(myVar);")
        tempScript.deleteOnExit()

        val tempConfig = File.createTempFile("test_rules", ".yaml")
        tempConfig.writeText(
            """
            rules:
              identifier_format:
                style: camelCase
              mandatory-variable-or-literal-in-println:
                enabled: true
            """.trimIndent()
        )
        tempConfig.deleteOnExit()

        Analyzer().execute(tempScript, tempConfig)
        val output = outputStream.toString()
        assertTrue(output.contains("SUCCESS: No issues were found"))
    }

    @Test
    fun `test execute with File and explicit version 1_1`() {
        val tempScript = File.createTempFile("test_file_exec_11", ".ps")
        tempScript.writeText("const myVar: boolean = true;\nprintln(myVar);")
        tempScript.deleteOnExit()

        val tempConfig = File.createTempFile("test_rules", ".yaml")
        tempConfig.writeText(
            """
            rules:
              identifier_format:
                style: camelCase
              mandatory-variable-or-literal-in-println:
                enabled: true
            """.trimIndent()
        )
        tempConfig.deleteOnExit()

        Analyzer().execute(tempScript, tempConfig, "1.1")
        val output = outputStream.toString()
        assertTrue(output.contains("SUCCESS: No issues were found"))
    }

    @Test
    fun `test execute with null configFileObj`() {
        val tempScript = File.createTempFile("test_null_config", ".ps")
        tempScript.writeText("let x: number = 5;")
        tempScript.deleteOnExit()

        Analyzer().execute(tempScript, null)
        val output = outputStream.toString()
        assertTrue(output.contains("configuration file 'null' does not exist"))
    }

    @Test
    fun `test executeValidation with 1 arg defaults to version 1_0`() {
        val tempScript = File.createTempFile("test_val_1arg", ".ps")
        tempScript.writeText("let myVar: number = 42;\nprintln(myVar);")
        tempScript.deleteOnExit()

        Analyzer().executeValidation(listOf(tempScript.absolutePath))
        val output = outputStream.toString()
        assertTrue(output.contains("SUCCESS: File is syntactically and semantically valid"))
    }

    @Test
    fun `test execute with 2 args defaults to version 1_0`() {
        val tempScript = File.createTempFile("test_exec_2args", ".ps")
        tempScript.writeText("let myVar: number = 42;\nprintln(myVar);")
        tempScript.deleteOnExit()

        val tempConfig = File.createTempFile("test_rules", ".yaml")
        tempConfig.writeText("rules:\n  identifier_format:\n    style: camelCase\n")
        tempConfig.deleteOnExit()

        Analyzer().execute(listOf(tempScript.absolutePath, tempConfig.absolutePath))
        val output = outputStream.toString()
        assertTrue(output.contains("SUCCESS: No issues were found"))
    }

    @Test
    fun `test executeValidation with non-blank syntax error message`() {
        val tempScript = File.createTempFile("test_bool_in_1_0", ".ps")
        tempScript.writeText("let x: boolean = true;")
        tempScript.deleteOnExit()

        Analyzer().executeValidation(listOf(tempScript.absolutePath, "1.0"))
        val output = outputStream.toString()
        assertTrue(output.contains("SYNTAX ERROR: Error: Unknown or unsupported type 'boolean' in PrintScript 1.0"))
    }

    @Test
    fun `test executeAnalysis with lexer exception unclosed brace`() {
        val tempScript = File.createTempFile("test_unclosed", ".ps")
        tempScript.writeText("if (true) { let x: number = 5;")
        tempScript.deleteOnExit()

        Analyzer().executeValidation(listOf(tempScript.absolutePath, "1.1"))
        val output = outputStream.toString()
        assertTrue(output.contains("Unclosed brace detected") || output.contains("ERROR"))
    }

    @Test
    fun `test execute with invalid YAML configuration file`() {
        val tempScript = File.createTempFile("test_yaml_err_script", ".ps")
        tempScript.writeText("let x: number = 5;")
        tempScript.deleteOnExit()

        val tempConfig = File.createTempFile("test_bad_yaml", ".yaml")
        tempConfig.writeText("!config_error: something")
        tempConfig.deleteOnExit()

        Analyzer().execute(listOf(tempScript.absolutePath, tempConfig.absolutePath, "1.0"))
        val output = outputStream.toString()
        assertTrue(output.contains("Configuration file error") || output.contains("ERROR"))
    }

    @Test
    fun `test execute with YAML tag containing syntax`() {
        val tempScript = File.createTempFile("test_yaml_syntax_script", ".ps")
        tempScript.writeText("let x: number = 5;")
        tempScript.deleteOnExit()

        val tempConfig = File.createTempFile("test_syntax_yaml", ".yaml")
        tempConfig.writeText("!syntax_error: something")
        tempConfig.deleteOnExit()

        Analyzer().execute(listOf(tempScript.absolutePath, tempConfig.absolutePath, "1.0"))
        val output = outputStream.toString()
        assertTrue(output.contains("This appears to be a syntax error") || output.contains("ERROR"))
    }

    @Test
    fun `test execute with YAML tag containing unexpected`() {
        val tempScript = File.createTempFile("test_yaml_unexp_script", ".ps")
        tempScript.writeText("let x: number = 5;")
        tempScript.deleteOnExit()

        val tempConfig = File.createTempFile("test_unexp_yaml", ".yaml")
        tempConfig.writeText("!unexpected_error: something")
        tempConfig.deleteOnExit()

        Analyzer().execute(listOf(tempScript.absolutePath, tempConfig.absolutePath, "1.0"))
        val output = outputStream.toString()
        assertTrue(output.contains("Unexpected token found") || output.contains("ERROR"))
    }
}
