package globaltests.src.test.kotlin.dsl

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
import inputprovider.src.main.kotlin.InputProvider
import interpreter.src.main.kotlin.Interpreter
import lexer.src.main.kotlin.Lexer
import lexer.src.main.kotlin.StringCharSource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import parser.src.main.kotlin.Parser
import java.io.File
import java.util.LinkedList
import java.util.Queue

/**
 * Entry point for the PrintScript Test DSL.
 */
fun printScriptTest(block: PrintScriptTestBuilder.() -> Unit): List<String> {
    val builder = PrintScriptTestBuilder()
    builder.apply(block)
    return builder.execute()
}

/**
 * Helper to parse a single statement string into an ASTNode.
 */
fun parseStatement(code: String, version: String = "1.0"): ASTNode {
    val lexer = Lexer.from(code, version)
    val tokens = lexer.lexIntoStatements().first()
    return Parser(tokens, version).parse()
}

/**
 * Builder class for configuring and executing end-to-end and validation tests.
 */
class PrintScriptTestBuilder {
    var version: String = "1.0"
    var code: String = ""

    private val inputs: Queue<String> = LinkedList()
    private val envVars: MutableMap<String, String> = mutableMapOf()
    private val expectedOutputs: MutableList<String> = mutableListOf()
    private var hasExpectedOutputs: Boolean = false

    var expectedExceptionClass: Class<out Throwable>? = null
    var expectedErrorMessageSubstring: String? = null
    var shouldFail: Boolean = false
    var shouldSucceed: Boolean = false
    var parseOnly: Boolean = false

    fun withInput(vararg inputValues: String) {
        inputs.addAll(inputValues.toList())
    }

    fun withInputs(inputList: List<String>) {
        inputs.addAll(inputList)
    }

    fun withEnv(key: String, value: String) {
        envVars[key] = value
    }

    fun withEnv(vars: Map<String, String>) {
        envVars.putAll(vars)
    }

    fun fromResourceDir(relativePath: String) {
        val cleanPath = relativePath.removePrefix("/").removeSuffix("/")
        val localDir = File("src/test/resources/$cleanPath")
        val baseDir = if (localDir.exists()) {
            localDir
        } else {
            val res = this::class.java.classLoader.getResource(cleanPath)
                ?: Thread.currentThread().contextClassLoader.getResource(cleanPath)
            res?.let { File(it.toURI()) }
        }

        if (baseDir != null && baseDir.exists()) {
            val mainFile = File(baseDir, "main.ps")
            if (mainFile.exists()) {
                code = mainFile.readText()
            }
            val inputFile = File(baseDir, "input.txt")
            if (inputFile.exists()) {
                withInputs(inputFile.readLines())
            }
            val outputFile = File(baseDir, "output.txt")
            if (outputFile.exists()) {
                expectOutputs(outputFile.readLines())
            }
        }
    }

    fun expectOutputs(vararg outputs: String) {
        expectedOutputs.addAll(outputs.toList())
        hasExpectedOutputs = true
    }

    fun expectOutputs(outputs: List<String>) {
        expectedOutputs.addAll(outputs)
        hasExpectedOutputs = true
    }

    fun expectEmptyOutput() {
        expectedOutputs.clear()
        hasExpectedOutputs = true
    }

    fun expectError(exceptionClass: Class<out Throwable>, messageSubstring: String? = null) {
        expectedExceptionClass = exceptionClass
        expectedErrorMessageSubstring = messageSubstring
    }

    inline fun <reified T : Throwable> expectError(messageSubstring: String? = null) {
        expectError(T::class.java, messageSubstring)
    }

    fun expectFailure() {
        shouldFail = true
    }

    fun expectSuccess() {
        shouldSucceed = true
    }

    fun expectValidSyntax() {
        parseOnly = true
        shouldSucceed = true
    }

    fun execute(): List<String> {
        val capturedOutput = mutableListOf<String>()
        val testPrinter: (Any?) -> Unit = { message -> capturedOutput.add(message.toString()) }

        val mockProvider = object : InputProvider {
            override fun readInput(prompt: String): String {
                testPrinter(prompt)
                return inputs.poll() ?: ""
            }

            override fun readEnv(varName: String): String? {
                return envVars[varName]
            }
        }

        val action = {
            if (parseOnly) {
                val lexer = Lexer.from(code, version)
                val tokens = lexer.lexIntoStatements().first()
                val parser = Parser(tokens, version)
                val ast = parser.parse()
                if (ast.type == ASTNodeType.INVALID) {
                    throw IllegalStateException("Parsed AST is INVALID")
                }
            } else {
                val lexer = Lexer(StringCharSource(code), version)
                val statements = lexer.lexIntoStatements()
                val interpreter = Interpreter(version, mockProvider, testPrinter)

                for (statement in statements) {
                    val parser = Parser(statement, version)
                    val ast = parser.parse()
                    interpreter.interpret(ast)
                }
            }
        }

        val targetExClass = expectedExceptionClass
        if (targetExClass != null) {
            val thrown = org.junit.jupiter.api.Assertions.assertThrows(targetExClass) { action() }
            val expectedSub = expectedErrorMessageSubstring
            if (expectedSub != null) {
                assertTrue(
                    thrown.message?.contains(expectedSub) == true,
                    "Expected error message to contain '$expectedSub' but was '${thrown.message}'"
                )
            }
            return capturedOutput
        }

        if (shouldFail) {
            org.junit.jupiter.api.Assertions.assertThrows(Throwable::class.java) { action() }
            return capturedOutput
        }

        if (shouldSucceed) {
            assertDoesNotThrow { action() }
            return capturedOutput
        }

        action()

        if (hasExpectedOutputs) {
            assertEquals(expectedOutputs, capturedOutput)
        }

        return capturedOutput
    }
}
