package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test

class InterpreterArithmeticTest {

    @Test
    fun `test addition`() {
        val interpreter = Interpreter("1.1")
        val addNode = ASTNode(
            DataType.ADDITION,
            "+",
            Position(1, 0),
            listOf(
                ASTNode(DataType.NUMBER_LITERAL, "10.0", Position(1, 1), emptyList()),
                ASTNode(DataType.NUMBER_LITERAL, "5.0", Position(1, 2), emptyList())
            )
        )
        val result = interpreter.interpret(addNode)
        assertEquals(15.0, result)
    }

    @Test
    fun `test subtraction`() {
        val interpreter = Interpreter("1.1")
        val subtractNode = ASTNode(
            DataType.SUBTRACTION,
            "-",
            Position(1, 0),
            listOf(
                ASTNode(DataType.NUMBER_LITERAL, "10.0", Position(1, 1), emptyList()),
                ASTNode(DataType.NUMBER_LITERAL, "5.0", Position(1, 2), emptyList())
            )
        )
        val result = interpreter.interpret(subtractNode)
        assertEquals(5.0, result)
    }

    @Test
    fun `test multiplication`() {
        val interpreter = Interpreter("1.1")
        val multiplyNode = ASTNode(
            DataType.MULTIPLICATION,
            "*",
            Position(1, 0),
            listOf(
                ASTNode(DataType.NUMBER_LITERAL, "10.0", Position(1, 1), emptyList()),
                ASTNode(DataType.NUMBER_LITERAL, "5.0", Position(1, 2), emptyList())
            )
        )
        val result = interpreter.interpret(multiplyNode)
        assertEquals(50.0, result)
    }

    @Test
    fun `test division`() {
        val interpreter = Interpreter("1.1")
        val divideNode = ASTNode(
            DataType.DIVISION,
            "/",
            Position(1, 0),
            listOf(
                ASTNode(DataType.NUMBER_LITERAL, "10.0", Position(1, 1), emptyList()),
                ASTNode(DataType.NUMBER_LITERAL, "5.0", Position(1, 2), emptyList())
            )
        )
        val result = interpreter.interpret(divideNode)
        assertEquals(2.0, result)
    }
}
