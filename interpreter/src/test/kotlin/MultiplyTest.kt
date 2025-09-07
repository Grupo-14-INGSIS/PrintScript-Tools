import common.src.main.kotlin.ASTNode
import common.src.main.kotlin.DataType
import common.src.main.kotlin.Position
import common.src.main.kotlin.Token
import org.junit.jupiter.api.Assertions.assertEquals
import src.main.model.tools.interpreter.interpreter.Multiply
import kotlin.test.Test

class MultiplyTest {
    @Test
    fun `subtract two numeric tokens returns correct result`() {
        val left = ASTNode(Token(DataType.NUMBER_LITERAL, "10.5", Position(1, 1)), emptyList())
        val right = ASTNode(Token(DataType.NUMBER_LITERAL, "4.0", Position(1, 2)), emptyList())
        val parent = ASTNode(Token(DataType.MULTIPLICATION, "*", Position(1, 0)), listOf(left, right))

        val result = Multiply.interpret(parent)
        assertEquals(42.0, result)
    }

    @Test
    fun `subtract with non-numeric token returns false`() {
        val left = ASTNode(Token(DataType.STRING_LITERAL, "\"hola\"", Position(1, 1)), emptyList())
        val right = ASTNode(Token(DataType.NUMBER_LITERAL, "3", Position(1, 2)), emptyList())
        val parent = ASTNode(Token(DataType.MULTIPLICATION, "*", Position(1, 0)), listOf(left, right))

        val result = Multiply.interpret(parent)
        assertEquals(false, result)
    }
}
