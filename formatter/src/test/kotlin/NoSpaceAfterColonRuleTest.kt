package test.formatter.formatrule.optional

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import container.src.main.kotlin.Container
import formatter.src.main.kotlin.formatrule.optional.NoSpaceAfterColonRule

class NoSpaceAfterColonRuleTest {

    private fun token(type: DataType, content: String, line: Int, column: Int): Token {
        return Token(type, content, Position(line, column))
    }

    private val rule = NoSpaceAfterColonRule()

    @Test
    fun `removes space after colon`() {
        var input = Container()
        input = input.addContainer(token(DataType.COLON, ":", 1, 0))
        input = input.addContainer(token(DataType.SPACE, " ", 1, 1))
        input = input.addContainer(token(DataType.IDENTIFIER, "x", 1, 2))

        val result = rule.format(input)

        assertEquals(2, result.size())
        assertEquals(":", result.get(0)?.content)
        assertEquals("x", result.get(1)?.content)
    }

    @Test
    fun `does not remove non-space after colon`() {
        var input = Container()
        input = input.addContainer(token(DataType.COLON, ":", 2, 0))
        input = input.addContainer(token(DataType.IDENTIFIER, "y", 2, 1))

        val result = rule.format(input)

        assertEquals(2, result.size())
        assertEquals(":", result.get(0)?.content)
        assertEquals("y", result.get(1)?.content)
    }

    @Test
    fun `does not remove space not after colon`() {
        var input = Container()
        input = input.addContainer(token(DataType.IDENTIFIER, "a", 3, 0))
        input = input.addContainer(token(DataType.SPACE, " ", 3, 1))
        input = input.addContainer(token(DataType.COLON, ":", 3, 2))

        val result = rule.format(input)

        assertEquals(3, result.size())
        assertEquals("a", result.get(0)?.content)
        assertEquals(" ", result.get(1)?.content)
        assertEquals(":", result.get(2)?.content)
    }

    @Test
    fun `handles colon at end of input`() {
        var input = Container()
        input = input.addContainer(token(DataType.COLON, ":", 4, 0))

        val result = rule.format(input)

        assertEquals(1, result.size())
        assertEquals(":", result.get(0)?.content)
    }

    @Test
    fun `preserves token positions`() {
        var input = Container()
        input = input.addContainer(token(DataType.COLON, ":", 5, 0))
        input = input.addContainer(token(DataType.SPACE, " ", 5, 1))
        input = input.addContainer(token(DataType.IDENTIFIER, "z", 5, 2))

        val result = rule.format(input)

        assertEquals(Position(5, 0), result.get(0)?.position)
        assertEquals(Position(5, 2), result.get(1)?.position)
    }
}
