package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import inputprovider.src.main.kotlin.InputProvider
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test

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

    @Test
    fun `returns numeric input when convertible`() {
        val inputProvider = FakeInputProvider("42.5")
        val interpreter = Interpreter("1.1", inputProvider)
        val node = ASTNode(
            DataType.FUNCTION_CALL,
            "readInput",
            Position(0, 0),
            children = listOf(
                ASTNode(DataType.STRING_LITERAL, "Enter a number", Position(0, 0), children = emptyList())
            )
        )
        val result = interpreter.interpret(node)
        assertEquals("42.5", result)
    }

    @Test
    fun `returns string input when not convertible`() {
        val inputProvider = FakeInputProvider("hello")
        val interpreter = Interpreter("1.1", inputProvider)
        val node = ASTNode(
            DataType.FUNCTION_CALL,
            "readInput",
            Position(0, 0),
            children = listOf(
                ASTNode(DataType.STRING_LITERAL, "Enter text", Position(0, 0), children = emptyList())
            )
        )
        val result = interpreter.interpret(node)
        assertEquals("hello", result)
    }

    @Test
    fun `handles call with no prompt node`() {
        val inputProvider = VerifiableInputProvider("")
        val interpreter = Interpreter("1.1", inputProvider)
        val node = ASTNode(DataType.FUNCTION_CALL, "readInput", Position(0, 0), children = emptyList())
        interpreter.interpret(node)
        assert(inputProvider.wasCalled)
    }
}
