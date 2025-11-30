package cli.src.test.kotlin

import org.junit.jupiter.api.Assertions.assertTrue
import cli.src.main.kotlin.FormatterCommand
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class FormatterCommandTest {
    @Test
    fun `missing arguments prints usage`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val command = FormatterCommand()
        command.execute(listOf("onlySource.txt"))

        val output = outputStream.toString().trim()
        assertTrue(
            output.contains(
                "Error: Must specify the source file and the format configuration file."
            )
        )
        assertTrue(
            output.contains(
                "Usage: formatter <source_file> <configuration_file> [version]"
            )
        )
    }

    @Test
    fun `unsupported version prints error`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val command = FormatterCommand()
        command.execute(listOf("source.txt", "config.yml", "2.0"))

        val output = outputStream.toString().trim()
        assertTrue(
            output.contains(
                "Error: Unsupported version. Only 1.0 and 1.1 are admitted."
            )
        )
    }

    @Test
    fun `source file does not exist`() {
        val configFile = File.createTempFile("config", ".yml")
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val command = FormatterCommand()
        command.execute(
            listOf("missing_source.txt", configFile.absolutePath)
        )

        val output = outputStream.toString().trim()
        assertTrue(
            output.contains(
                "Error: The source file 'missing_source.txt' does not exist."
            )
        )
    }

    @Test
    fun `successful formatting prints completion`() {
        val sourceFile = File.createTempFile("source", ".txt").apply {
            writeText(
                "print(1);"
            )
        }

        val configFile = File.createTempFile("config", ".yml").apply {
            writeText(
                """
            rules:
              mandatory-single-space-separation: true
                """.trimIndent()
            )
        }
        val originalOut = System.out
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val command = FormatterCommand()
        command.execute(listOf(sourceFile.absolutePath, configFile.absolutePath))

        val output = outputStream.toString()

        System.setOut(originalOut)
        println(output)

        assertTrue(output.contains("Starting formatting of"))
        assertTrue(output.contains("Applying formatting rules"))
    }
}
