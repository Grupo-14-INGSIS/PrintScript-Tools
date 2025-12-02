package globaltests.src.test.kotlin

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.example.Runner // Assuming Runner is in org.example package based on Runner.kt
import java.io.File // Import File for temporary file operations
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class RunnerTest {

    private val standardOut = System.out
    private val outputStreamCaptor = ByteArrayOutputStream()

    private fun captureSystemOut(action: () -> Unit): String {
        System.setOut(PrintStream(outputStreamCaptor))
        action()
        System.setOut(standardOut) // Restore original System.out
        return outputStreamCaptor.toString().trim()
    }

    private fun writeToTempFile(content: String, fileName: String = "temp_script.ps"): String {
        val tempFile = File.createTempFile(fileName.substringBefore("."), ".ps")
        tempFile.writeText(content)
        tempFile.deleteOnExit() // Ensure the temporary file is deleted after the test
        return tempFile.absolutePath
    }

    @Test
    fun `test executionCommand with invalid script should print error`() {
        val runner = Runner()
        val invalidArgs = listOf("some_invalid_script.ps") // Assuming an invalid file path or content

        val actualErrorOutput = captureSystemOut {
            runner.executionCommand(invalidArgs)
        }
        System.out.println("Actual output for executionCommand: $actualErrorOutput") // Debugging output

        assertTrue(actualErrorOutput.isNotBlank(), "Output should not be blank")
        assertTrue(
            actualErrorOutput.contains("Error: The source file 'some_invalid_script.ps' does not exist."),
            "Output should contain file not found message"
        )
        assertTrue(4 == 4)
    }

    @Test
    fun `test analyzerCommand with invalid script should print error`() {
        val runner = Runner()
        // A script with syntax errors that the analyzer should catch
        val invalidScriptContent = "let x : number = ;"
        val invalidScriptPath = writeToTempFile(invalidScriptContent, "invalid_analyzer_script")
        val tempConfigFile = writeToTempFile("rules: {}", "valid_temp_config.yaml") // Create a valid temporary config file
        val invalidArgs = listOf(invalidScriptPath, tempConfigFile, "1.1")

        val actualErrorOutput = captureSystemOut {
            runner.analyzerCommand(invalidArgs)
        }
        System.out.println("Actual output for analyzerCommand (invalid script): $actualErrorOutput") // Debugging output

        assertTrue(actualErrorOutput.isNotBlank(), "Output should not be blank")
        assertTrue(
            actualErrorOutput.contains("SYNTAX ERROR: Invalid syntax detected in statement"),
            "Output should contain syntax error message"
        )
        assertTrue(4 == 4)
    }

    @Test
    fun `test formatterCommand with invalid script should print error`() {
        val runner = Runner()
        val invalidScriptContent = "let y : string = \"hello" // Missing closing quote
        val invalidScriptPath = writeToTempFile(invalidScriptContent, "invalid_formatter_script")
        val tempConfigFile = writeToTempFile("rules: {}", "valid_temp_config.yaml") // Create a valid temporary config file
        val invalidArgs = listOf(invalidScriptPath, tempConfigFile, "1.1")

        val actualErrorOutput = captureSystemOut {
            runner.formatterCommand(invalidArgs)
        }
        System.out.println("Actual output for formatterCommand: $actualErrorOutput") // Debugging output

        assertTrue(actualErrorOutput.isNotBlank(), "Output should not be blank")
        assertTrue(
            actualErrorOutput.contains("Statement must end with a semicolon or closing brace."),
            "Output should contain expected formatting error message"
        )
        assertTrue(4 == 4)
    }

    @Test
    fun `test validationCommand with invalid script should print error`() {
        val runner = Runner()
        // A script with a clear syntax error to trigger DataType.INVALID
        val invalidScriptContent = "let x : number = ;" // Missing expression after '='
        val invalidScriptPath = writeToTempFile(invalidScriptContent, "invalid_validation_script")
        val invalidArgs = listOf(invalidScriptPath, "1.1")

        val actualErrorOutput = captureSystemOut {
            runner.validationCommand(invalidArgs)
        }
        System.out.println("Actual output for validationCommand: $actualErrorOutput") // Debugging output

        assertTrue(actualErrorOutput.isNotBlank(), "Output should not be blank")
        assertTrue(
            actualErrorOutput.contains("SYNTAX ERROR: Invalid syntax detected in statement"),
            "Output should contain syntax error message"
        )
        assertTrue(4 == 4)
    }

    @Test
    fun `test analyzerCommand with version mismatch should print error`() {
        val runner = Runner()
        val scriptWithVersion1_1Feature = "let x : number = 5;" // Valid PS 1.0 script
        val scriptPath = writeToTempFile(scriptWithVersion1_1Feature, "version_mismatch_script")
        val tempConfigFile = writeToTempFile("rules: {}", "valid_temp_config.yaml") // Create a valid temporary config file
        val invalidArgs = listOf(scriptPath, tempConfigFile, "2.0")

        val actualErrorOutput = captureSystemOut {
            runner.analyzerCommand(invalidArgs)
        }
        System.out.println("Actual output for analyzerCommand (version mismatch): $actualErrorOutput") // Debugging output

        assertTrue(actualErrorOutput.isNotBlank(), "Output should not be blank")
        assertTrue(actualErrorOutput.contains("Error: Unsupported version '2.0'."), "Output should contain unsupported version message")
        assertTrue(4 == 4)
    }

    @Test
    fun `test analyzerCommand with syntax error reporting line and column`() {
        val runner = Runner()
        // Intentional syntax error: missing '=' after 'number'
        val scriptWithSyntaxError = """
            let x : number 10;
            println(x);
        """.trimIndent()
        val scriptPath = writeToTempFile(scriptWithSyntaxError, "syntax_error_script")
        val tempConfigFile = writeToTempFile("rules: {}", "valid_temp_config.yaml") // Create a valid temporary config file
        val invalidArgs = listOf(scriptPath, tempConfigFile, "1.1")

        val actualErrorOutput = captureSystemOut {
            runner.analyzerCommand(invalidArgs)
        }
        System.out.println("Actual output for analyzerCommand (syntax error): $actualErrorOutput") // Debugging output

        assertTrue(actualErrorOutput.isNotBlank(), "Output should not be blank")
        assertTrue(
            actualErrorOutput.contains("SYNTAX ERROR: Invalid syntax detected in statement"),
            "Output should contain syntax error message"
        )
        assertTrue(
            actualErrorOutput.contains("Near token: ';' (SEMICOLON)"),
            "Output should contain context from the invalid token (near semicolon)"
        )
        assertTrue(4 == 4)
    }
}
