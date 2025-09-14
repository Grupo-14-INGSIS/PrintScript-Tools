package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.Multiply
import org.junit.jupiter.api.Test

class MultiplyTest {
    @Test
    fun `subtract two numeric tokens returns correct result`() {
        val left = ASTNode(DataType.NUMBER_LITERAL, "10.5", Position(1, 1), emptyList())
        val right = ASTNode(DataType.NUMBER_LITERAL, "4.0", Position(1, 2), emptyList())
        val parent = ASTNode(DataType.MULTIPLICATION, "*", Position(1, 0), listOf(left, right))

        val result = Multiply.interpret(parent)
        assertEquals(42.0, result)
    }

    @Test
    fun `subtract with non-numeric token returns false`() {
        val left = ASTNode(DataType.STRING_LITERAL, "\"hola\"", Position(1, 1), emptyList())
        val right = ASTNode(DataType.NUMBER_LITERAL, "3", Position(1, 2), emptyList())
        val parent = ASTNode(DataType.MULTIPLICATION, "*", Position(1, 0), listOf(left, right))

        val result = Multiply.interpret(parent)
        assertEquals(false, result)
    }
}
