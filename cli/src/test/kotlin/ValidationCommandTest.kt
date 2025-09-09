package cli.src.test.kotlin

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.io.File
import cli.src.main.kotlin.ValidationCommand

class ValidationCommandTest {

    @Test
    fun `no arguments prints usage`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val command = ValidationCommand()
        command.execute(emptyList())

        val output = outputStream.toString().trim()
        assertTrue(output.contains("Error: Must specify the source file."))
        assertTrue(output.contains("Usage: validation <archivo_fuente> [version]"))
    }

    @Test
    fun `unsupported version prints error`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val command = ValidationCommand()
        command.execute(listOf("source.txt", "2.0"))

        val output = outputStream.toString().trim()
        assertTrue(output.contains("Error: Unsupported version. Only 1.0 and 1.1 are supported."))
    }

    @Test
    fun `source file does not exist`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val command = ValidationCommand()
        command.execute(listOf("missing_source.txt"))

        val output = outputStream.toString().trim()
        assertTrue(output.contains("Error: The source file 'missing_source.txt' does not exist."))
    }

    @Test
    fun `valid source with no lint errors`() {
        val sourceFile = File.createTempFile("source", ".txt").apply {
            writeText("print(1)") // Asegurate que sea válido para tu lexer/parser
        }

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val command = ValidationCommand()
        command.execute(listOf(sourceFile.absolutePath))

        val output = outputStream.toString()
        assertTrue(output.contains("Starting validation"))
        assertTrue(output.contains("SUCCESS: Code validation passed"))
    }



    @Test
    fun `internal error during validation prints exception`() {
        val sourceFile = File.createTempFile("source", ".txt").apply {
            writeText("$$$") // contenido inválido para lexer/parser
        }

        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val command = ValidationCommand()
        command.execute(listOf(sourceFile.absolutePath))

        val output = outputStream.toString()
        assertFalse(output.contains("Error durante validation:"))
    }
}
