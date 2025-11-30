package cli.src.test.kotlin

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import cli.src.main.kotlin.AnalyzerCommand
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class AnalayzerCommandTest {

    @Test
    fun `less than 2 arguments prints error`() {
        val analyzer = AnalyzerCommand()

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        analyzer.execute(listOf("file.txt"))

        val output = outputStream.toString().trim()

        assertTrue(
            output.contains(
                "Error: Must specify the source file and the analysis configuration file."
            )
        )
        assertTrue(
            output.contains(
                "Usage: analyzer <source_file> <configuration_file> [version]"
            )
        )
    }

    @Test
    fun `defaul version`() {
        val analyzer = AnalyzerCommand()

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        analyzer.execute(listOf("file.txt", "linter", "1.3"))

        val output = outputStream.toString().trim()

        assertTrue(output.contains("Error: Unsupported version '1.3'."))
    }

    @Test
    fun `source file does not exist`() {
        val analyzer = AnalyzerCommand()
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        analyzer.execute(listOf("nonexistent.txt", "config.yml"))

        val output = outputStream.toString().trim()
        assertTrue(
            output.contains(
                "Error: The source file 'nonexistent.txt' does not exist."
            )
        )
    }

    @Test
    fun `config file does not exist`() {
        val sourceFile = File.createTempFile("source", ".txt").apply {
            writeText(
                "print(1)"
            )
        }
        val analyzer = AnalyzerCommand()
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        analyzer.execute(
            listOf(sourceFile.absolutePath, "missing_config.yml")
        )

        val output = outputStream.toString().trim()
        assertTrue(
            output.contains(
                "Error: The configuration file 'missing_config.yml' does not exist."
            )
        )
    }

    @Test
    fun `successful analysis with no errors`() {
        val sourceFile = File.createTempFile("source", ".txt").apply {
            writeText(
                "print(1);"
            )
        }
        val configFile = File.createTempFile("config", ".yml").apply {
            writeText(
                """
            rules:
              identifierNaming:
                style: camelCase
              mandatory_variable_or_literal_in_println:
                enabled: false
                """.trimIndent()
            )
        }

        val analyzer = AnalyzerCommand()
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        analyzer.execute(
            listOf(sourceFile.absolutePath, configFile.absolutePath)
        )

        val output = outputStream.toString()
        assertTrue(output.contains("SUCCESS: No issues were found"))
    }
}
