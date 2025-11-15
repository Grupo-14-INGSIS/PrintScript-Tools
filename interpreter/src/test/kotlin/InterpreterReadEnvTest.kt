package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import inputprovider.src.main.kotlin.InputProvider
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InterpreterReadEnvTest {
    private class FakeInputProvider(private val envs: Map<String, String>) : InputProvider {
        override fun readInput(prompt: String): String = ""
        override fun readEnv(varName: String): String? = envs[varName]
    }

    @Test
    fun `readEnv returns correct value`() {
        val inputProvider = FakeInputProvider(mapOf("MY_VAR" to "my_value"))
        val interpreter = Interpreter("1.1", inputProvider)
        val node = ASTNode(
            DataType.FUNCTION_CALL,
            "readEnv",
            Position(0, 0),
            children = listOf(
                ASTNode(DataType.STRING_LITERAL, "MY_VAR", Position(0, 0), children = emptyList())
            )
        )
        val result = interpreter.interpret(node)
        assertEquals("my_value", result)
    }

    @Test
    fun `readEnv with unknown variable throws exception`() {
        val inputProvider = FakeInputProvider(emptyMap())
        val interpreter = Interpreter("1.1", inputProvider)
        val node = ASTNode(
            DataType.FUNCTION_CALL,
            "readEnv",
            Position(0, 0),
            children = listOf(
                ASTNode(DataType.STRING_LITERAL, "UNKNOWN_VAR", Position(0, 0), children = emptyList())
            )
        )
        assertThrows<IllegalArgumentException> {
            interpreter.interpret(node)
        }
    }
}
