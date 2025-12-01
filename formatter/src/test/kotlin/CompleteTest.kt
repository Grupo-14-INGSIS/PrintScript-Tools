package formatter.src.test.kotlin

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.formatrule.mandatory.LineBreakAfterSemicolonRule
import formatter.src.main.kotlin.formatrule.mandatory.SpaceAroundOperatorRule
import formatter.src.main.kotlin.formatrule.mandatory.SpaceBetweenTokensRule
import formatter.src.main.kotlin.formatrule.optional.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

class CompleteTest {


    private fun createToken(type: DataType, content: String, line: Int = 1, col: Int = 1): Token {
        return Token(type, content, Position(line, col))
    }


    private fun createContainer(vararg tokens: Token): Container {
        var container = Container()
        tokens.forEach { container = container.addContainer(it) }
        return container
    }


    @Test
    fun `test LineBreakAfterSemicolonRule adds line break after semicolon`() {
        val rule = LineBreakAfterSemicolonRule(true)
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.SEMICOLON, ";"),
            createToken(DataType.IDENTIFIER, "y")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.SEMICOLON, result.get(1)?.type)
        assertEquals(DataType.LINE_BREAK, result.get(2)?.type)
        assertEquals(DataType.IDENTIFIER, result.get(3)?.type)
    }


    @Test
    fun `test LineBreakAfterSemicolonRule does nothing when disabled`() {
        val rule = LineBreakAfterSemicolonRule(false)
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.SEMICOLON, ";"),
            createToken(DataType.IDENTIFIER, "y")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(tokens.size(), result.size())
    }


    @Test
    fun `test LineBreakAfterSemicolonRule removes spaces before adding line break`() {
        val rule = LineBreakAfterSemicolonRule(true)
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.SEMICOLON, ";"),
            createToken(DataType.SPACE, " "),
            createToken(DataType.SPACE, " "),
            createToken(DataType.IDENTIFIER, "y")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.LINE_BREAK, result.get(2)?.type)
        assertEquals(DataType.IDENTIFIER, result.get(3)?.type)
    }


    @Test
    fun `test LineBreakAfterSemicolonRule does not add break before close brace`() {
        val rule = LineBreakAfterSemicolonRule(true)
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.SEMICOLON, ";"),
            createToken(DataType.CLOSE_BRACE, "}")
        )


        val result = rule.format(listOf(tokens)).first()


        // No debe agregar line break antes de }
        assertEquals(DataType.CLOSE_BRACE, result.get(2)?.type)
    }


    @Test
    fun `test LineBreakAfterSemicolonRule handles multiple semicolons`() {
        val rule = LineBreakAfterSemicolonRule(true)
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.SEMICOLON, ";"),
            createToken(DataType.IDENTIFIER, "y"),
            createToken(DataType.SEMICOLON, ";"),
            createToken(DataType.IDENTIFIER, "z")
        )


        val result = rule.format(listOf(tokens)).first()


        assertTrue(result.size() > tokens.size())
    }


    @Test
    fun `test LineBreakAfterSemicolonRule at end of file`() {
        val rule = LineBreakAfterSemicolonRule(true)
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.SEMICOLON, ";")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(2, result.size())
    }

    @Test
    fun `test SpaceAroundOperatorRule adds spaces around addition`() {
        val rule = SpaceAroundOperatorRule()
        val tokens = createContainer(
            createToken(DataType.NUMBER_LITERAL, "5"),
            createToken(DataType.ADDITION, "+"),
            createToken(DataType.NUMBER_LITERAL, "3")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.SPACE, result.get(1)?.type)
        assertEquals(DataType.ADDITION, result.get(2)?.type)
        assertEquals(DataType.SPACE, result.get(3)?.type)
    }


    @Test
    fun `test SpaceAroundOperatorRule adds spaces around subtraction`() {
        val rule = SpaceAroundOperatorRule()
        val tokens = createContainer(
            createToken(DataType.NUMBER_LITERAL, "10"),
            createToken(DataType.SUBTRACTION, "-"),
            createToken(DataType.NUMBER_LITERAL, "3")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.SPACE, result.get(1)?.type)
        assertEquals(DataType.SUBTRACTION, result.get(2)?.type)
        assertEquals(DataType.SPACE, result.get(3)?.type)
    }


    @Test
    fun `test SpaceAroundOperatorRule adds spaces around multiplication`() {
        val rule = SpaceAroundOperatorRule()
        val tokens = createContainer(
            createToken(DataType.NUMBER_LITERAL, "4"),
            createToken(DataType.MULTIPLICATION, "*"),
            createToken(DataType.NUMBER_LITERAL, "5")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.SPACE, result.get(1)?.type)
        assertEquals(DataType.MULTIPLICATION, result.get(2)?.type)
        assertEquals(DataType.SPACE, result.get(3)?.type)
    }


    @Test
    fun `test SpaceAroundOperatorRule adds spaces around division`() {
        val rule = SpaceAroundOperatorRule()
        val tokens = createContainer(
            createToken(DataType.NUMBER_LITERAL, "20"),
            createToken(DataType.DIVISION, "/"),
            createToken(DataType.NUMBER_LITERAL, "4")
        )


        val result = rule.format(listOf(tokens)).first()

        assertEquals(DataType.SPACE, result.get(1)?.type)
        assertEquals(DataType.DIVISION, result.get(2)?.type)
        assertEquals(DataType.SPACE, result.get(3)?.type)
    }


    @Test
    fun `test SpaceAroundOperatorRule does not duplicate spaces`() {
        val rule = SpaceAroundOperatorRule()
        val tokens = createContainer(
            createToken(DataType.NUMBER_LITERAL, "5"),
            createToken(DataType.SPACE, " "),
            createToken(DataType.ADDITION, "+"),
            createToken(DataType.SPACE, " "),
            createToken(DataType.NUMBER_LITERAL, "3")
        )


        val result = rule.format(listOf(tokens)).first()


        var spaceCount = 0
        for (i in 0 until result.size()) {
            if (result.get(i)?.type == DataType.SPACE) spaceCount++
        }
        assertEquals(2, spaceCount)
    }


    @Test
    fun `test SpaceAroundOperatorRule handles multiple operators`() {
        val rule = SpaceAroundOperatorRule()
        val tokens = createContainer(
            createToken(DataType.NUMBER_LITERAL, "1"),
            createToken(DataType.ADDITION, "+"),
            createToken(DataType.NUMBER_LITERAL, "2"),
            createToken(DataType.MULTIPLICATION, "*"),
            createToken(DataType.NUMBER_LITERAL, "3")
        )


        val result = rule.format(listOf(tokens)).first()


        assertTrue(result.size() > tokens.size())
    }


    @Test
    fun `test SpaceAroundOperatorRule at start of expression`() {
        val rule = SpaceAroundOperatorRule()
        val tokens = createContainer(
            createToken(DataType.SUBTRACTION, "-"),
            createToken(DataType.NUMBER_LITERAL, "5")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.SPACE, result.get(1)?.type)
    }


    @Test
    fun `test SpaceAroundOperatorRule at end of expression`() {
        val rule = SpaceAroundOperatorRule()
        val tokens = createContainer(
            createToken(DataType.NUMBER_LITERAL, "5"),
            createToken(DataType.ADDITION, "+")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.SPACE, result.get(1)?.type)
    }



    @Test
    fun `test SpaceBetweenTokensRule adds space between tokens`() {
        val rule = SpaceBetweenTokensRule(true)
        val tokens = createContainer(
            createToken(DataType.LET_KEYWORD, "let"),
            createToken(DataType.IDENTIFIER, "x")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.LET_KEYWORD, result.get(0)?.type)
        assertEquals(DataType.SPACE, result.get(1)?.type)
        assertEquals(DataType.IDENTIFIER, result.get(2)?.type)
    }


    @Test
    fun `test SpaceBetweenTokensRule does nothing when disabled`() {
        val rule = SpaceBetweenTokensRule(false)
        val tokens = createContainer(
            createToken(DataType.LET_KEYWORD, "let"),
            createToken(DataType.IDENTIFIER, "x")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(tokens.size(), result.size())
    }


    @Test
    fun `test SpaceBetweenTokensRule does not add space before semicolon`() {
        val rule = SpaceBetweenTokensRule(true)
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.SEMICOLON, ";")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.IDENTIFIER, result.get(0)?.type)
        assertEquals(DataType.SEMICOLON, result.get(1)?.type)
    }


    @Test
    fun `test SpaceBetweenTokensRule removes duplicate spaces`() {
        val rule = SpaceBetweenTokensRule(true)
        val tokens = createContainer(
            createToken(DataType.LET_KEYWORD, "let"),
            createToken(DataType.SPACE, " "),
            createToken(DataType.SPACE, " "),
            createToken(DataType.IDENTIFIER, "x")
        )


        val result = rule.format(listOf(tokens)).first()


        var spaceCount = 0
        for (i in 0 until result.size()) {
            if (result.get(i)?.type == DataType.SPACE) spaceCount++
        }
        assertEquals(1, spaceCount)
    }


    @Test
    fun `test SpaceBetweenTokensRule handles empty container`() {
        val rule = SpaceBetweenTokensRule(true)
        val tokens = Container()


        val result = rule.format(listOf(tokens)).first()


        assertEquals(0, result.size())
    }



    @Test
    fun `test AssignSpacingRule with spaces before and after`() {
        val rule = AssignSpacingRule(true, true)
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.ASSIGNATION, "="),
            createToken(DataType.NUMBER_LITERAL, "5")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.SPACE, result.get(1)?.type)
        assertEquals(DataType.ASSIGNATION, result.get(2)?.type)
        assertEquals(DataType.SPACE, result.get(3)?.type)
    }


    @Test
    fun `test AssignSpacingRule with no spaces`() {
        val rule = AssignSpacingRule(false, false)
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.SPACE, " "),
            createToken(DataType.ASSIGNATION, "="),
            createToken(DataType.SPACE, " "),
            createToken(DataType.NUMBER_LITERAL, "5")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.IDENTIFIER, result.get(0)?.type)
        assertEquals(DataType.ASSIGNATION, result.get(1)?.type)
        assertEquals(DataType.NUMBER_LITERAL, result.get(2)?.type)
    }


    @Test
    fun `test AssignSpacingRule with space only before`() {
        val rule = AssignSpacingRule(true, false)
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.ASSIGNATION, "="),
            createToken(DataType.NUMBER_LITERAL, "5")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.SPACE, result.get(1)?.type)
        assertEquals(DataType.ASSIGNATION, result.get(2)?.type)
        assertEquals(DataType.NUMBER_LITERAL, result.get(3)?.type)
    }


    @Test
    fun `test AssignSpacingRule with space only after`() {
        val rule = AssignSpacingRule(false, true)
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.ASSIGNATION, "="),
            createToken(DataType.NUMBER_LITERAL, "5")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.IDENTIFIER, result.get(0)?.type)
        assertEquals(DataType.ASSIGNATION, result.get(1)?.type)
        assertEquals(DataType.SPACE, result.get(2)?.type)
    }


    @Test
    fun `test AssignSpacingRule removes multiple spaces`() {
        val rule = AssignSpacingRule(true, true)
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.SPACE, " "),
            createToken(DataType.SPACE, " "),
            createToken(DataType.ASSIGNATION, "="),
            createToken(DataType.SPACE, " "),
            createToken(DataType.SPACE, " "),
            createToken(DataType.NUMBER_LITERAL, "5")
        )


        val result = rule.format(listOf(tokens)).first()


        var spaceCount = 0
        for (i in 0 until result.size()) {
            if (result.get(i)?.type == DataType.SPACE) spaceCount++
        }
        assertEquals(2, spaceCount) // Solo uno antes y uno después
    }


    @Test
    fun `test AssignSpacingRule handles multiple assignments`() {
        val rule = AssignSpacingRule(true, true)
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.ASSIGNATION, "="),
            createToken(DataType.NUMBER_LITERAL, "5"),
            createToken(DataType.IDENTIFIER, "y"),
            createToken(DataType.ASSIGNATION, "="),
            createToken(DataType.NUMBER_LITERAL, "10")
        )


        val result = rule.format(listOf(tokens)).first()


        assertTrue(result.size() > tokens.size())
    }



    @Test
    fun `test NoSpaceBeforeColonRule removes space before colon`() {
        val rule = NoSpaceBeforeColonRule()
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.SPACE, " "),
            createToken(DataType.COLON, ":"),
            createToken(DataType.NUMBER_TYPE, "number")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.IDENTIFIER, result.get(0)?.type)
        assertEquals(DataType.COLON, result.get(1)?.type)
        assertEquals(DataType.NUMBER_TYPE, result.get(2)?.type)
    }


    @Test
    fun `test NoSpaceBeforeColonRule removes multiple spaces before colon`() {
        val rule = NoSpaceBeforeColonRule()
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.SPACE, " "),
            createToken(DataType.SPACE, " "),
            createToken(DataType.SPACE, " "),
            createToken(DataType.COLON, ":"),
            createToken(DataType.NUMBER_TYPE, "number")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.IDENTIFIER, result.get(0)?.type)
        assertEquals(DataType.COLON, result.get(1)?.type)
    }


    @Test
    fun `test NoSpaceBeforeColonRule does nothing when no space`() {
        val rule = NoSpaceBeforeColonRule()
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.COLON, ":"),
            createToken(DataType.NUMBER_TYPE, "number")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(tokens.size(), result.size())
    }



    @Test
    fun `test NoSpaceAfterColonRule removes space after colon`() {
        val rule = NoSpaceAfterColonRule()
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.COLON, ":"),
            createToken(DataType.SPACE, " "),
            createToken(DataType.NUMBER_TYPE, "number")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.COLON, result.get(1)?.type)
        assertEquals(DataType.NUMBER_TYPE, result.get(2)?.type)
    }


    @Test
    fun `test NoSpaceAfterColonRule removes multiple spaces after colon`() {
        val rule = NoSpaceAfterColonRule()
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.COLON, ":"),
            createToken(DataType.SPACE, " "),
            createToken(DataType.SPACE, " "),
            createToken(DataType.NUMBER_TYPE, "number")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.COLON, result.get(1)?.type)
        assertEquals(DataType.NUMBER_TYPE, result.get(2)?.type)
    }



    @Test
    fun `test SpaceBeforeColonRule adds space before colon`() {
        val rule = SpaceBeforeColonRule()
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.COLON, ":"),
            createToken(DataType.NUMBER_TYPE, "number")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.SPACE, result.get(1)?.type)
        assertEquals(DataType.COLON, result.get(2)?.type)
    }


    @Test
    fun `test SpaceBeforeColonRule removes extra spaces and adds one`() {
        val rule = SpaceBeforeColonRule()
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.SPACE, " "),
            createToken(DataType.SPACE, " "),
            createToken(DataType.COLON, ":"),
            createToken(DataType.NUMBER_TYPE, "number")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.SPACE, result.get(1)?.type)
        assertEquals(DataType.COLON, result.get(2)?.type)
    }


    @Test
    fun `test SpaceAfterColonRule adds space after colon`() {
        val rule = SpaceAfterColonRule()
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.COLON, ":"),
            createToken(DataType.NUMBER_TYPE, "number")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.COLON, result.get(1)?.type)
        assertEquals(DataType.SPACE, result.get(2)?.type)
        assertEquals(DataType.NUMBER_TYPE, result.get(3)?.type)
    }


    @Test
    fun `test SpaceAfterColonRule removes extra spaces and adds one`() {
        val rule = SpaceAfterColonRule()
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.COLON, ":"),
            createToken(DataType.SPACE, " "),
            createToken(DataType.SPACE, " "),
            createToken(DataType.NUMBER_TYPE, "number")
        )


        val result = rule.format(listOf(tokens)).first()


        var spaceCount = 0
        for (i in 0 until result.size()) {
            if (result.get(i)?.type == DataType.SPACE) spaceCount++
        }
        assertEquals(1, spaceCount)
    }


    @Test
    fun `test LineBreakBeforePrintRule adds line breaks before println`() {
        val rule = LineBreakBeforePrintRule(1)
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.PRINTLN, "println")
        )


        val result = rule.format(listOf(tokens)).first()

        var lineBreakCount = 0
        for (i in 0 until result.size()) {
            if (result.get(i)?.type == DataType.LINE_BREAK) lineBreakCount++
        }
        assertEquals(2, lineBreakCount)
    }


    @Test
    fun `test LineBreakBeforePrintRule removes existing spaces and line breaks`() {
        val rule = LineBreakBeforePrintRule(1)
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.SPACE, " "),
            createToken(DataType.LINE_BREAK, "\n"),
            createToken(DataType.PRINTLN, "println")
        )


        val result = rule.format(listOf(tokens)).first()


        var lineBreakCount = 0
        for (i in 0 until result.size()) {
            if (result.get(i)?.type == DataType.LINE_BREAK) lineBreakCount++
        }
        assertEquals(2, lineBreakCount)
    }


    @Test
    fun `test LineBreakBeforePrintRule with count zero`() {
        val rule = LineBreakBeforePrintRule(0)
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.PRINTLN, "println")
        )


        val result = rule.format(listOf(tokens)).first()


        var lineBreakCount = 0
        for (i in 0 until result.size()) {
            if (result.get(i)?.type == DataType.LINE_BREAK) lineBreakCount++
        }
        assertEquals(1, lineBreakCount)
    }


    @Test
    fun `test LineBreakBeforePrintRule with count three`() {
        val rule = LineBreakBeforePrintRule(3)
        val tokens = createContainer(
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.PRINTLN, "println")
        )


        val result = rule.format(listOf(tokens)).first()


        var lineBreakCount = 0
        for (i in 0 until result.size()) {
            if (result.get(i)?.type == DataType.LINE_BREAK) lineBreakCount++
        }
        assertEquals(4, lineBreakCount)
    }


    @Test
    fun `test LineBreakAfterPrintRule adds line breaks after println`() {
        val rule = LineBreakAfterPrintRule(1)
        val tokens = createContainer(
            createToken(DataType.PRINTLN, "println"),
            createToken(DataType.OPEN_PARENTHESIS, "("),
            createToken(DataType.STRING_LITERAL, "\"hello\""),
            createToken(DataType.CLOSE_PARENTHESIS, ")"),
            createToken(DataType.SEMICOLON, ";"),
            createToken(DataType.IDENTIFIER, "x")
        )


        val result = rule.format(listOf(tokens)).first()


        var lineBreakCount = 0
        for (i in 0 until result.size()) {
            if (result.get(i)?.type == DataType.LINE_BREAK) lineBreakCount++
        }
        assertEquals(2, lineBreakCount) // count + 1
    }


    @Test
    fun `test LineBreakAfterPrintRule does not add breaks for last statement`() {
        val rule = LineBreakAfterPrintRule(1)
        val tokens = createContainer(
            createToken(DataType.PRINTLN, "println"),
            createToken(DataType.OPEN_PARENTHESIS, "("),
            createToken(DataType.STRING_LITERAL, "\"hello\""),
            createToken(DataType.CLOSE_PARENTHESIS, ")"),
            createToken(DataType.SEMICOLON, ";")
        )


        val result = rule.format(listOf(tokens)).first()


        var lineBreakCount = 0
        for (i in 0 until result.size()) {
            if (result.get(i)?.type == DataType.LINE_BREAK) lineBreakCount++
        }
        assertEquals(0, lineBreakCount)
    }


    @Test
    fun `test LineBreakAfterPrintRule with zero line breaks`() {
        val rule = LineBreakAfterPrintRule(0)
        val tokens = createContainer(
            createToken(DataType.PRINTLN, "println"),
            createToken(DataType.OPEN_PARENTHESIS, "("),
            createToken(DataType.STRING_LITERAL, "\"hello\""),
            createToken(DataType.CLOSE_PARENTHESIS, ")"),
            createToken(DataType.SEMICOLON, ";"),
            createToken(DataType.IDENTIFIER, "x")
        )


        val result = rule.format(listOf(tokens)).first()


        var lineBreakCount = 0
        for (i in 0 until result.size()) {
            if (result.get(i)?.type == DataType.LINE_BREAK) lineBreakCount++
        }
        assertEquals(1, lineBreakCount)
    }

    @Test
    fun `test IndentationRule adds indentation after open brace`() {
        val rule = IndentationRule(4)
        val tokens = createContainer(
            createToken(DataType.OPEN_BRACE, "{"),
            createToken(DataType.LINE_BREAK, "\n"),
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.CLOSE_BRACE, "}")
        )


        val result = rule.format(listOf(tokens)).first()


        assertEquals(DataType.SPACE, result.get(2)?.type)
        assertEquals("    ", result.get(2)?.content)
    }


    @Test
    fun `test IndentationRule handles nested braces`() {
        val rule = IndentationRule(2)
        val tokens = createContainer(
            createToken(DataType.OPEN_BRACE, "{"),
            createToken(DataType.LINE_BREAK, "\n"),
            createToken(DataType.OPEN_BRACE, "{"),
            createToken(DataType.LINE_BREAK, "\n"),
            createToken(DataType.IDENTIFIER, "x"),
            createToken(DataType.CLOSE_BRACE, "}"),
            createToken(DataType.CLOSE_BRACE, "}")
        )


        val result = rule.format(listOf(tokens)).first()


        assertTrue(result.size() > tokens.size())
    }
}
