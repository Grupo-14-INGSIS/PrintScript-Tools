package formatter.src.test.kotlin

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import formatter.src.main.kotlin.formatrule.mandatory.LineBreakAfterSemicolonRule
import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token

class LineBreakAfterSemicolonRuleTest {

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
    fun `does NOT add line break after semicolon at end of file`() {
        val source = containerOf(
            token(
                DataType.IDENTIFIER,
                "x"
            ),
            token(
                DataType.SEMICOLON,
                ";"
            )
        )

        val rule = LineBreakAfterSemicolonRule()
        val result = rule.format(source)

        assertEquals(";", result.get(1)!!.content)
        assertEquals(2, result.size()) // Solo 2 tokens, sin salto de línea añadido
    }

    @Test
    fun `inserts line break when missing after semicolon`() {
        val source = containerOf(
            token(
                DataType.IDENTIFIER,
                "x"
            ),
            token(
                DataType.SEMICOLON,
                ";"
            ),
            token(
                DataType.IDENTIFIER,
                "y"
            )
        )

        val rule = LineBreakAfterSemicolonRule()
        val result = rule.format(source)

        assertEquals(";", result.get(1)!!.content)
        assertEquals("\n", result.get(2)!!.content) // salto agregado
        assertEquals("y", result.get(3)!!.content)
    }

    @Test
    fun `does nothing if line break already present`() {
        val source = containerOf(
            token(
                DataType.IDENTIFIER,
                "x"
            ),
            token(
                DataType.SEMICOLON,
                ";"
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

        val rule = LineBreakAfterSemicolonRule()
        val result = rule.format(source)

        assertEquals(source.size(), result.size()) // no cambia nada
        assertEquals(";", result.get(1)!!.content)
        assertEquals("\n", result.get(2)!!.content)
        assertEquals("y", result.get(3)!!.content)
    }


    @Test
    fun `does nothing when no semicolon present`() {
        val source = containerOf(
            token(
                DataType.IDENTIFIER,
                "x"
            ),
            token(
                DataType.IDENTIFIER,
                "y"
            )
        )

        val rule = LineBreakAfterSemicolonRule()
        val result = rule.format(source)

        assertEquals(source.size(), result.size())
        assertEquals("x", result.get(0)!!.content)
        assertEquals("y", result.get(1)!!.content)
    }
}
