package formatter.src.test.kotlin

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import formatter.src.main.kotlin.formatrule.mandatory.SpaceAroundOperatorRule
import formatter.src.main.kotlin.formatrule.mandatory.SpaceBetweenTokensRule
import formatter.src.main.kotlin.formatrule.FormatRule
import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token

// Helpers para crear tokens y containers
fun token(type: DataType, content: String) = Token(type, content, Position(0, 0))
fun containerOf(vararg tokens: Token): Container {
    var c = Container()
    c = c.addAll(tokens.toList())
    return c
}

class FormatterRulesTest {


    @Test
    fun `SpaceAroundOperatorRule does not duplicate spaces if already present`() {
        val a = token(DataType.IDENTIFIER, "a")
        val sp1 = token(DataType.SPACE, " ")
        val plus = token(DataType.ADDITION, "+")
        val sp2 = token(DataType.SPACE, " ")
        val b = token(DataType.IDENTIFIER, "b")
        val input = containerOf(a, sp1, plus, sp2, b)

        val rule: FormatRule = SpaceAroundOperatorRule()
        val output = rule.format(listOf(input)).first()

        val expected = listOf("a", " ", "+", " ", "b")
        assertEquals(expected, output.container.map { it.content })
    }

    @Test
    fun `SpaceBetweenTokensRule collapses consecutive spaces`() {
        val a = token(DataType.IDENTIFIER, "a")
        val sp1 = token(DataType.SPACE, " ")
        val sp2 = token(DataType.SPACE, " ")
        val b = token(DataType.IDENTIFIER, "b")
        val input = containerOf(a, sp1, sp2, b)

        val rule: FormatRule = SpaceBetweenTokensRule()
        val output = rule.format(listOf(input)).first()

        val expected = listOf("a", " ", "b")
        assertEquals(expected, output.container.map { it.content })
    }



    @Test
    fun `Container basic operations work`() {
        var c = Container()
        val t1 = token(DataType.IDENTIFIER, "x")
        val t2 = token(DataType.NUMBER_LITERAL, "42")

        assertTrue(c.isEmpty())
        c = c.addContainer(t1)
        c = c.addContainer(t2)
        assertEquals(2, c.size())
        assertEquals("x", c.first()!!.content)
        assertEquals("42", c.last()!!.content)

        val slice = c.slice(0, 1)
        assertEquals(1, slice.size())
        assertEquals("x", slice.first()!!.content)
    }
}
