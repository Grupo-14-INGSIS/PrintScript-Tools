package cli.src.test.kotlin

import executor.Executor
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class ExecutorTest {

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
    fun `test execute with empty args`() {
        Executor().execute(emptyList())
        val output = outputStream.toString()
        assertTrue(output.contains("Must specify the source file"))
    }

    @Test
    fun `test execute with unsupported version`() {
        Executor().execute(listOf("script.ps", "3.0"))
        val output = outputStream.toString()
        assertTrue(output.contains("Unsupported version"))
    }

    @Test
    fun `test execute with non-existent file`() {
        Executor().execute(listOf("nonexistent_file_12345.ps", "1.0"))
        val output = outputStream.toString()
        assertTrue(output.contains("does not exist"))
    }

    @Test
    fun `test execute with valid 1_0 script`() {
        val tempFile = File.createTempFile("test_exec_10", ".ps")
        tempFile.writeText("let x: number = 10 + 20;\nprintln(x);")
        tempFile.deleteOnExit()

        val printedOutputs = mutableListOf<Any?>()
        val executor = Executor(printer = { printedOutputs.add(it) })
        executor.execute(listOf(tempFile.absolutePath, "1.0"))

        assertTrue(printedOutputs.any { it.toString().contains("30") || it.toString() == "30.0" || it.toString() == "30" })
    }

    @Test
    fun `test execute with valid 1_1 script`() {
        val tempFile = File.createTempFile("test_exec_11", ".ps")
        tempFile.writeText("const greeting: string = \"Hello 1.1\";\nif (true) {\n  println(greeting);\n}")
        tempFile.deleteOnExit()

        val printedOutputs = mutableListOf<Any?>()
        val executor = Executor(printer = { printedOutputs.add(it) })
        executor.execute(listOf(tempFile.absolutePath, "1.1"))

        assertTrue(printedOutputs.any { it.toString() == "Hello 1.1" })
    }

    @Test
    fun `test execute with syntax error script`() {
        val tempFile = File.createTempFile("test_exec_err", ".ps")
        tempFile.writeText("let x = ;")
        tempFile.deleteOnExit()

        Executor().execute(listOf(tempFile.absolutePath, "1.0"))
        val output = outputStream.toString()
        assertTrue(output.contains("ERROR during execution") || output.contains("Syntax error") || output.contains("error"))
    }
}
