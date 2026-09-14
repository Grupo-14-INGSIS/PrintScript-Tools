package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
import inputprovider.src.main.kotlin.InputProvider
import ast.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class InterpreterReadInputTest {
    private class FakeInputProvider(private val response: String) : InputProvider {
        override fun readInput(prompt: String): String = response
        override fun readEnv(varName: String): String? = null
    }

    private class VerifiableInputProvider(private val expectedPrompt: String) : InputProvider {
        var wasCalled = false
        override fun readInput(prompt: String): String {
            assertEquals(expectedPrompt, prompt)
            wasCalled = true
            return ""
        }
        override fun readEnv(varName: String): String? = null
    }

    @ParameterizedTest(name = "Input ''{0}'' returns ''{1}''")
    @CsvSource(
        "42.5, 42.5", // Numeric string
        "hello, hello", // Standard string
        "'', ''" // Empty string
    )
    fun `readInput returns correct value`(mockedResponse: String, expectedResult: String) {
        val inputProvider = FakeInputProvider(mockedResponse)
        val interpreter = Interpreter("1.1", inputProvider)

        val result = interpreter.interpret(createReadInputNode("Enter value"))
        assertEquals(expectedResult, result)
    }

    @Test
    fun `handles call with no prompt node`() {
        val inputProvider = VerifiableInputProvider("")
        val interpreter = Interpreter("1.1", inputProvider)

        val node = ASTNode(ASTNodeType.FUNCTION_CALL, "readInput", Position(0, 0), emptyList())
        interpreter.interpret(node)

        assert(inputProvider.wasCalled)
    }

    private fun createReadInputNode(prompt: String): ASTNode {
        return ASTNode(
            ASTNodeType.FUNCTION_CALL,
            "readInput",
            Position(0, 0),
            children = listOf(
                ASTNode(ASTNodeType.STRING_LITERAL, prompt, Position(0, 0), emptyList())
            )
        )
    }
}
