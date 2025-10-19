package parser.src.test.kotlin

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import container.src.main.kotlin.Container
import parser.src.main.kotlin.Parser

class MoreParserTest {

    private fun token(type: DataType, content: String, line: Int = 0, col: Int = 0) =
        Token(type, content, Position(line, col))

    private fun containerOf(vararg tokens: Token): Container {
        var c = Container()
        for (t in tokens) {
            c = c.addContainer(t)
        }
        return c
    }

    @Test
    fun `stmtParse returns invalid on empty input`() {
        val parser = Parser(Container())
        val result = parser.stmtParse(Container())
        assertEquals(DataType.INVALID, result.type)
    }

    @Test
    fun `stmtParse parses const declaration`() {
        val tokens = containerOf(
            token(DataType.CONST_KEYWORD, "const"),
            token(DataType.IDENTIFIER, "x"),
            token(DataType.COLON, ":"),
            token(DataType.STRING_TYPE, "String"),
            token(DataType.ASSIGNATION, "="),
            token(DataType.STRING_LITERAL, "\"hello\"")
        )

        val parser = Parser(tokens)
        val result = parser.stmtParse(tokens)
        assertEquals(DataType.DECLARATION, result.type)
        assertEquals("\"hello\"", result.children.last().content)
    }

    @Test
    fun `stmtParse parses simple assignment`() {
        val tokens = containerOf(
            token(DataType.IDENTIFIER, "x"),
            token(DataType.ASSIGNATION, "="),
            token(DataType.NUMBER_LITERAL, "42")
        )

        val parser = Parser(tokens)
        val result = parser.stmtParse(tokens)
        assertEquals(DataType.ASSIGNATION, result.type)
        assertEquals("x", result.children.first().content)
    }

    @Test
    fun `expParse parses literal`() {
        val tokens = containerOf(token(DataType.STRING_LITERAL, "\"hi\""))
        val parser = Parser(tokens)
        val result = parser.expParse(tokens)
        assertEquals(DataType.STRING_LITERAL, result.type)
        assertEquals("\"hi\"", result.content)
    }

    @Test
    fun `expParse parses parenthesized expression`() {
        val tokens = containerOf(
            token(DataType.OPEN_PARENTHESIS, "("),
            token(DataType.NUMBER_LITERAL, "1"),
            token(DataType.CLOSE_PARENTHESIS, ")")
        )

        val parser = Parser(tokens)
        val result = parser.expParse(tokens)
        assertEquals(DataType.NUMBER_LITERAL, result.type)
        assertEquals("1", result.content)
    }

    @Test
    fun `expParse parses function call`() {
        val tokens = containerOf(
            token(DataType.PRINTLN, "println"),
            token(DataType.OPEN_PARENTHESIS, "("),
            token(DataType.STRING_LITERAL, "\"msg\""),
            token(DataType.CLOSE_PARENTHESIS, ")")
        )

        val parser = Parser(tokens)
        val result = parser.expParse(tokens)
        assertEquals(DataType.PRINTLN, result.type)
        assertEquals("println", result.content)
        assertEquals("\"msg\"", result.children.first().content)
    }

    @Test
    fun `isLiteral detects boolean literal if supported`() {
        val tokens = containerOf(token(DataType.BOOLEAN_LITERAL, "true"))
        val parser = Parser(tokens, "1.1")
        val result = parser.expParse(tokens)
        assertEquals(DataType.BOOLEAN_LITERAL, result.type)
    }

    @Test
    fun `ifStmtParse returns valid AST with else block`() {
        val tokens = containerOf(
            token(DataType.IF_KEYWORD, "if"),
            token(DataType.OPEN_PARENTHESIS, "("),
            token(DataType.BOOLEAN_LITERAL, "true"),
            token(DataType.CLOSE_PARENTHESIS, ")"),
            token(DataType.OPEN_BRACE, "{"),
            token(DataType.IDENTIFIER, "x"),
            token(DataType.ASSIGNATION, "="),
            token(DataType.NUMBER_LITERAL, "1"),
            token(DataType.SEMICOLON, ";"),
            token(DataType.CLOSE_BRACE, "}"),
            token(DataType.ELSE_KEYWORD, "else"),
            token(DataType.OPEN_BRACE, "{"),
            token(DataType.IDENTIFIER, "x"),
            token(DataType.ASSIGNATION, "="),
            token(DataType.NUMBER_LITERAL, "2"),
            token(DataType.SEMICOLON, ";"),
            token(DataType.CLOSE_BRACE, "}")
        )

        val parser = Parser(tokens)
        val result = parser.stmtParse(tokens)
        assertEquals(DataType.IF_STATEMENT, result.type)
        assertEquals(3, result.children.size) // condition, ifBlock, elseBlock
    }
}
