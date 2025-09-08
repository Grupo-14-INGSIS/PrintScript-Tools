import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import org.junit.jupiter.api.Assertions.assertEquals
import src.main.model.tools.interpreter.interpreter.Divide
import kotlin.test.Test

class DivideTest {
    @Test
    fun `subtract two numeric tokens returns correct result`() {
        val left = ASTNode(DataType.NUMBER_LITERAL, "16.0", Position(1, 1), emptyList())
        val right = ASTNode(DataType.NUMBER_LITERAL, "4.0", Position(1, 2), emptyList())
        val parent = ASTNode(DataType.DIVISION, "/", Position(1, 0), listOf(left, right))

        val result = Divide.interpret(parent)
        assertEquals(4.0, result)
    }

    @Test
    fun `subtract with non-numeric token returns false`() {
        val left = ASTNode(DataType.STRING_LITERAL, "\"hola\"", Position(1, 1), emptyList())
        val right = ASTNode(DataType.NUMBER_LITERAL, "3", Position(1, 2), emptyList())
        val parent = ASTNode(DataType.DIVISION, "/", Position(1, 0), listOf(left, right))

        val result = Divide.interpret(parent)
        assertEquals(false, result)
    }

    @Test
    fun `divide by zero`() {
        val left = ASTNode(DataType.STRING_LITERAL, "5684095", Position(1, 1), emptyList())
        val right = ASTNode(DataType.NUMBER_LITERAL, "0", Position(1, 2), emptyList())
        val parent = ASTNode(DataType.DIVISION, "/", Position(1, 0), listOf(left, right))

        val result = Divide.interpret(parent)
        assertEquals(false, result)
    }
}
