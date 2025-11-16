package formatter.src.test.kotlin.formatrule.optional


import container.src.main.kotlin.Container
import formatter.src.main.kotlin.formatrule.optional.IfBraceBelowLineRule
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position


class IfBraceBelowLineRuleTest {


    private val rule = IfBraceBelowLineRule()
    private val pos = Position(0, 0)


    @Test
    fun `test simple if with brace on same line moves brace to next line`() {
        val tokens = Container(listOf(
            Token(DataType.IF_KEYWORD, "if", pos),
            Token(DataType.SPACE, " ", pos),
            Token(DataType.OPEN_PARENTHESIS, "(", pos),
            Token(DataType.IDENTIFIER, "condition", pos),
            Token(DataType.CLOSE_PARENTHESIS, ")", pos),
            Token(DataType.SPACE, " ", pos),
            Token(DataType.OPEN_BRACE, "{", pos),
            Token(DataType.SPACE, " ", pos),
            Token(DataType.IDENTIFIER, "body", pos),
            Token(DataType.SPACE, " ", pos),
            Token(DataType.CLOSE_BRACE, "}", pos)
        ))


        val result = rule.format(tokens)


        assertEquals(DataType.IF_KEYWORD, result.get(0)?.type)
        assertEquals(DataType.CLOSE_PARENTHESIS, result.get(4)?.type)
        assertEquals(DataType.LINE_BREAK, result.get(5)?.type)
        assertEquals(DataType.OPEN_BRACE, result.get(6)?.type)
    }


    @Test
    fun `test if with brace already on next line remains unchanged`() {
        val tokens = Container(listOf(
            Token(DataType.IF_KEYWORD, "if", pos),
            Token(DataType.SPACE, " ", pos),
            Token(DataType.OPEN_PARENTHESIS, "(", pos),
            Token(DataType.IDENTIFIER, "x", pos),
            Token(DataType.CLOSE_PARENTHESIS, ")", pos),
            Token(DataType.LINE_BREAK, "\n", pos),
            Token(DataType.OPEN_BRACE, "{", pos),
            Token(DataType.IDENTIFIER, "body", pos),
            Token(DataType.CLOSE_BRACE, "}", pos)
        ))


        val result = rule.format(tokens)


        assertEquals(9, result.size())
        assertEquals(DataType.LINE_BREAK, result.get(5)?.type)
        assertEquals(DataType.OPEN_BRACE, result.get(6)?.type)
    }


    @Test
    fun `test if without braces is not affected`() {
        val tokens = Container(listOf(
            Token(DataType.IF_KEYWORD, "if", pos),
            Token(DataType.SPACE, " ", pos),
            Token(DataType.OPEN_PARENTHESIS, "(", pos),
            Token(DataType.IDENTIFIER, "x", pos),
            Token(DataType.CLOSE_PARENTHESIS, ")", pos),
            Token(DataType.SPACE, " ", pos),
            Token(DataType.IDENTIFIER, "statement", pos),
            Token(DataType.SEMICOLON, ";", pos)
        ))


        val result = rule.format(tokens)


        assertEquals(tokens.size(), result.size())
        assertNull(findToken(result, DataType.LINE_BREAK))
    }


    @Test
    fun `test multiple if statements each gets processed`() {
        val tokens = Container(listOf(
            Token(DataType.IF_KEYWORD, "if", pos),
            Token(DataType.OPEN_PARENTHESIS, "(", pos),
            Token(DataType.IDENTIFIER, "a", pos),
            Token(DataType.CLOSE_PARENTHESIS, ")", pos),
            Token(DataType.SPACE, " ", pos),
            Token(DataType.OPEN_BRACE, "{", pos),
            Token(DataType.CLOSE_BRACE, "}", pos),
            Token(DataType.LINE_BREAK, "\n", pos),
            Token(DataType.IF_KEYWORD, "if", pos),
            Token(DataType.OPEN_PARENTHESIS, "(", pos),
            Token(DataType.IDENTIFIER, "b", pos),
            Token(DataType.CLOSE_PARENTHESIS, ")", pos),
            Token(DataType.SPACE, " ", pos),
            Token(DataType.OPEN_BRACE, "{", pos),
            Token(DataType.CLOSE_BRACE, "}", pos)
        ))


        val result = rule.format(tokens)


        var lineBreakCount = 0
        for (i in 0 until result.size()) {
            if (result.get(i)?.type == DataType.LINE_BREAK) {
                lineBreakCount++
            }
        }
        assertTrue(lineBreakCount >= 2) // At least 2 line breaks added
    }




    @Test
    fun `test if with complex condition`() {
        val tokens = Container(listOf(
            Token(DataType.IF_KEYWORD, "if", pos),
            Token(DataType.SPACE, " ", pos),
            Token(DataType.OPEN_PARENTHESIS, "(", pos),
            Token(DataType.IDENTIFIER, "x", pos),
            Token(DataType.SPACE, " ", pos),
            Token(DataType.ADDITION, "+", pos),
            Token(DataType.SPACE, " ", pos),
            Token(DataType.IDENTIFIER, "y", pos),
            Token(DataType.CLOSE_PARENTHESIS, ")", pos),
            Token(DataType.SPACE, " ", pos),
            Token(DataType.OPEN_BRACE, "{", pos),
            Token(DataType.IDENTIFIER, "body", pos),
            Token(DataType.CLOSE_BRACE, "}", pos)
        ))


        val result = rule.format(tokens)


        var closingParenIndex = -1
        for (i in 0 until result.size()) {
            if (result.get(i)?.type == DataType.CLOSE_PARENTHESIS) {
                closingParenIndex = i
                break
            }
        }
        assertTrue(closingParenIndex > 0)
        assertEquals(DataType.LINE_BREAK, result.get(closingParenIndex + 1)?.type)
    }


    @Test
    fun `test if with multiple spaces before brace`() {
        val tokens = Container(listOf(
            Token(DataType.IF_KEYWORD, "if", pos),
            Token(DataType.OPEN_PARENTHESIS, "(", pos),
            Token(DataType.IDENTIFIER, "x", pos),
            Token(DataType.CLOSE_PARENTHESIS, ")", pos),
            Token(DataType.SPACE, " ", pos),
            Token(DataType.SPACE, " ", pos),
            Token(DataType.SPACE, " ", pos),
            Token(DataType.OPEN_BRACE, "{", pos),
            Token(DataType.CLOSE_BRACE, "}", pos)
        ))


        val result = rule.format(tokens)


        assertEquals(DataType.CLOSE_PARENTHESIS, result.get(3)?.type)
        assertEquals(DataType.LINE_BREAK, result.get(4)?.type)
        assertEquals(DataType.OPEN_BRACE, result.get(5)?.type)
    }


    @Test
    fun `test empty container`() {
        val tokens = Container(emptyList())
        val result = rule.format(tokens)
        assertEquals(0, result.size())
    }


    @Test
    fun `test container with only if keyword`() {
        val tokens = Container(listOf(
            Token(DataType.IF_KEYWORD, "if", pos)
        ))
        val result = rule.format(tokens)
        assertEquals(1, result.size())
        assertEquals(DataType.IF_KEYWORD, result.get(0)?.type)
    }


    @Test
    fun `test if without closing parenthesis`() {
        val tokens = Container(listOf(
            Token(DataType.IF_KEYWORD, "if", pos),
            Token(DataType.OPEN_PARENTHESIS, "(", pos),
            Token(DataType.IDENTIFIER, "x", pos)
        ))
        val result = rule.format(tokens)
        assertEquals(3, result.size())
    }


    @Test
    fun `test if with brace but no body`() {
        val tokens = Container(listOf(
            Token(DataType.IF_KEYWORD, "if", pos),
            Token(DataType.OPEN_PARENTHESIS, "(", pos),
            Token(DataType.IDENTIFIER, "x", pos),
            Token(DataType.CLOSE_PARENTHESIS, ")", pos),
            Token(DataType.OPEN_BRACE, "{", pos),
            Token(DataType.CLOSE_BRACE, "}", pos)
        ))


        val result = rule.format(tokens)


        assertEquals(DataType.LINE_BREAK, result.get(4)?.type)
        assertEquals(DataType.OPEN_BRACE, result.get(5)?.type)
    }


    private fun findToken(container: Container, type: DataType): Int? {
        for (i in 0 until container.size()) {
            if (container.get(i)?.type == type) return i
        }
        return null
    }
}
