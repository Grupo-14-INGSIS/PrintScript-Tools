package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import inputprovider.src.main.kotlin.InputProvider

class InterpreterIOTest {

    @Test
    fun `test readInput`() {
        val inputProvider = Mockito.mock(InputProvider::class.java)
        Mockito.`when`(inputProvider.readInput("Enter a number: ")).thenReturn("42")

        val interpreter = Interpreter("1.1", inputProvider)
        val readInputNode = ASTNode(
            DataType.FUNCTION_CALL,
            "readInput",
            Position(1, 0),
            listOf(ASTNode(DataType.STRING_LITERAL, "Enter a number: ", Position(1, 1), emptyList()))
        )

        val result = interpreter.interpret(readInputNode)
        assertEquals("42", result)
    }

    @Test
    fun `test readEnv`() {
        val inputProvider = Mockito.mock(InputProvider::class.java)
        Mockito.`when`(inputProvider.readEnv("MY_VAR")).thenReturn("my_value")

        val interpreter = Interpreter("1.1", inputProvider)
        val readEnvNode = ASTNode(
            DataType.FUNCTION_CALL,
            "readEnv",
            Position(1, 0),
            listOf(ASTNode(DataType.STRING_LITERAL, "MY_VAR", Position(1, 1), emptyList()))
        )

        val result = interpreter.interpret(readEnvNode)
        assertEquals("my_value", result)
    }
}
