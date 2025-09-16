package formatter.src.test.kotlin

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import formatter.src.main.kotlin.formatrule.optional.LineBreakBeforePrintRule
import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token

class LineBreakBeforePrintRuleTest {

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
    fun `adds missing line break before println`() {
        val source = containerOf(
            token(
                DataType.IDENTIFIER,
                "x"
            ),
            token(
                DataType.IDENTIFIER,
                "println"
            )
        )

        val rule = LineBreakBeforePrintRule(lineBreakCount = 1)
        val result = rule.format(source)

        assertEquals("x", result.get(0)!!.content)
        assertEquals("\n", result.get(1)!!.content) // se agregó salto
        assertEquals("println", result.get(2)!!.content)
    }

    @Test
    fun `keeps correct line breaks if already present`() {
        val source = containerOf(
            token(
                DataType.IDENTIFIER,
                "x"
            ),
            token(
                DataType.LINE_BREAK,
                "\n"
            ),
            token(
                DataType.IDENTIFIER,
                "println"
            )
        )

        val rule = LineBreakBeforePrintRule(lineBreakCount = 1)
        val result = rule.format(source)

        assertEquals("\n", result.get(1)!!.content) // ya estaba bien
        assertEquals("println", result.get(2)!!.content)
    }

    @Test
    fun `adds multiple line breaks before println`() {
        val source = containerOf(
            token(
                DataType.IDENTIFIER,
                "x"
            ),
            token(
                DataType.IDENTIFIER,
                "println"
            )
        )

        val rule = LineBreakBeforePrintRule(lineBreakCount = 2)
        val result = rule.format(source)

        assertEquals("x", result.get(0)!!.content)
        assertEquals("\n", result.get(1)!!.content)
        assertEquals("\n", result.get(2)!!.content)
        assertEquals("println", result.get(3)!!.content)
    }

    @Test
    fun `removes extra line breaks before println`() {
        val source = containerOf(
            token(
                DataType.IDENTIFIER,
                "x"
            ),
            token(
                DataType.LINE_BREAK,
                "\n"
            ),
            token(
                DataType.LINE_BREAK,
                "\n"
            ),
            token(
                DataType.IDENTIFIER,
                "println"
            )
        )

        val rule = LineBreakBeforePrintRule(lineBreakCount = 1)
        val result = rule.format(source)

        // debería haber solo 1 salto antes del println
        assertEquals("x", result.get(0)!!.content)
        assertEquals("\n", result.get(1)!!.content)
        assertEquals("println", result.get(2)!!.content)
        assertEquals(3, result.size())
    }

    @Test
    fun `does nothing when no println present`() {
        val source = containerOf(
            token(
                DataType.IDENTIFIER,
                "x"
            ),
            token(
                DataType.LINE_BREAK,
                "\n"
            ),
            token(
                DataType.IDENTIFIER,
                "y"
            )
        )

        val rule = LineBreakBeforePrintRule(lineBreakCount = 1)
        val result = rule.format(source)

        // no tocó nada
        assertEquals(source.size(), result.size())
        assertEquals("x", result.get(0)!!.content)
        assertEquals("\n", result.get(1)!!.content)
        assertEquals("y", result.get(2)!!.content)
    }
}
