package formatter.src.test.kotlin

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import formatter.src.main.kotlin.formatrule.mandatory.IfBraceOnSameLineRule
import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token

class IfBranceOnSameLineRuleTest {

    private fun token(type: DataType, content: String) =
        Token(type, content, Position(0, 0))

    private fun containerOf(vararg tokens: Token): Container {
        var c = Container()
        for (t in tokens) {
            c = c.addContainer(
                t
            )
        }
        return c
    }

    @Test
    fun `keeps brace already on same line`() {
        val source = containerOf(
            token(
                DataType.IDENTIFIER,
                "if"
            ),
            token(
                DataType.OPEN_PARENTHESIS,
                "("
            ),
            token(
                DataType.IDENTIFIER,
                "z"
            ),
            token(
                DataType.CLOSE_PARENTHESIS,
                ")"
            ),
            token(
                DataType.SPACE,
                " "
            ),
            token(
                DataType.OPEN_BRACE,
                "{"
            )
        )

        val rule = IfBraceOnSameLineRule()
        val result = rule.format(source)

        // no debería cambiar nada
        assertEquals(source.size(), result.size())
        assertEquals(")", result.get(3)!!.content)
        assertEquals(" ", result.get(4)!!.content)
        assertEquals("{", result.get(5)!!.content)
    }

    @Test
    fun `does nothing when no if present`() {
        val source = containerOf(
            token(
                DataType.IDENTIFIER,
                "while"
            ),
            token(
                DataType.OPEN_PARENTHESIS,
                "("
            ),
            token(
                DataType.IDENTIFIER,
                "x"
            ),
            token(
                DataType.CLOSE_PARENTHESIS,
                ")"
            ),
            token(
                DataType.LINE_BREAK,
                "\n"
            ),
            token(
                DataType.OPEN_BRACE,
                "{"
            )
        )

        val rule = IfBraceOnSameLineRule()
        val result = rule.format(source)

        // no tocó nada
        assertEquals(source.size(), result.size())
        assertEquals("\n", result.get(4)!!.content)
        assertEquals("{", result.get(5)!!.content)
    }


    @Test
    fun `ignores if without brace`() {
        val source = containerOf(
            token(
                DataType.IDENTIFIER,
                "if"
            ),
            token(
                DataType.OPEN_PARENTHESIS,
                "("
            ),
            token(
                DataType.IDENTIFIER,
                "cond"
            ),
            token(
                DataType.CLOSE_PARENTHESIS,
                ")"
            ),
            token(
                DataType.IDENTIFIER,
                "x"
            ) // no hay llave
        )

        val rule = IfBraceOnSameLineRule()
        val result = rule.format(source)

        // no debe romper nada
        assertEquals(")", result.get(3)!!.content)
        assertEquals("x", result.get(4)!!.content)
    }
}
