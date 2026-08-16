package parser.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.src.main.kotlin.DeclarationWithAssignmentParser
import parser.src.main.kotlin.DeclarationWithoutAssignmentParser
import parser.src.main.kotlin.ExpressionStatementParser
import parser.src.main.kotlin.IfStatementParser
import parser.src.main.kotlin.Parser
import parser.src.main.kotlin.SimpleAssignmentParser
import parser.src.main.kotlin.StatementParser
import parser.src.main.kotlin.StatementParserFactory
import parser.src.main.kotlin.VersionConfig
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

class StatementParserTest {

    private fun token(type: DataType, content: String): Token {
        return Token(type, content, Position(1, 1))
    }

    private fun containerOf(vararg tokens: Token): Container {
        var container = Container()
        for (token in tokens) {
            container = container.addContainer(token)
        }
        return container
    }

    @Test
    fun `DeclarationWithAssignmentParser canParse and parses correctly in v1_0 and v1_1`() {
        val featuresV10 = VersionConfig.getFeatures("1.0")
        val featuresV11 = VersionConfig.getFeatures("1.1")
        val parserV10 = DeclarationWithAssignmentParser(featuresV10, "1.0")
        val parserV11 = DeclarationWithAssignmentParser(featuresV11, "1.1")

        val letTokens = containerOf(
            token(DataType.LET_KEYWORD, "let"),
            token(DataType.IDENTIFIER, "x"),
            token(DataType.COLON, ":"),
            token(DataType.NUMBER_TYPE, "number"),
            token(DataType.ASSIGNATION, "="),
            token(DataType.NUMBER_LITERAL, "5")
        )

        assertTrue(parserV10.canParse(letTokens))
        val dummyParser = Parser(letTokens, "1.0")
        val letNode = parserV10.parse(letTokens, dummyParser)
        assertEquals(DataType.DECLARATION, letNode.type)

        val constTokens = containerOf(
            token(DataType.CONST_KEYWORD, "const"),
            token(DataType.IDENTIFIER, "x"),
            token(DataType.COLON, ":"),
            token(DataType.NUMBER_TYPE, "number"),
            token(DataType.ASSIGNATION, "="),
            token(DataType.NUMBER_LITERAL, "5")
        )

        assertTrue(parserV10.canParse(constTokens))
        val invalidConstNode = parserV10.parse(constTokens, dummyParser)
        assertEquals(DataType.INVALID, invalidConstNode.type)
        assertTrue(invalidConstNode.content.contains("Cannot use 'const' keyword in PrintScript 1.0"))

        val dummyParserV11 = Parser(constTokens, "1.1")
        val validConstNode = parserV11.parse(constTokens, dummyParserV11)
        assertEquals(DataType.DECLARATION, validConstNode.type)
        assertEquals(DataType.CONST_KEYWORD, validConstNode.children[0].type)
    }

    @Test
    fun `DeclarationWithoutAssignmentParser canParse and parses correctly`() {
        val featuresV10 = VersionConfig.getFeatures("1.0")
        val featuresV11 = VersionConfig.getFeatures("1.1")
        val parserV10 = DeclarationWithoutAssignmentParser(featuresV10, "1.0")
        val parserV11 = DeclarationWithoutAssignmentParser(featuresV11, "1.1")

        val letTokens = containerOf(
            token(DataType.LET_KEYWORD, "let"),
            token(DataType.IDENTIFIER, "x"),
            token(DataType.COLON, ":"),
            token(DataType.NUMBER_TYPE, "number")
        )

        assertTrue(parserV10.canParse(letTokens))
        val dummyParser = Parser(letTokens, "1.0")
        val node = parserV10.parse(letTokens, dummyParser)
        assertEquals(DataType.VAR_DECLARATION_WITHOUT_ASSIGNATION, node.type)

        val constTokens = containerOf(
            token(DataType.CONST_KEYWORD, "const"),
            token(DataType.IDENTIFIER, "x"),
            token(DataType.COLON, ":"),
            token(DataType.NUMBER_TYPE, "number")
        )

        val invalidConst = parserV10.parse(constTokens, dummyParser)
        assertEquals(DataType.INVALID, invalidConst.type)

        val dummyParserV11 = Parser(constTokens, "1.1")
        val validConst = parserV11.parse(constTokens, dummyParserV11)
        assertEquals(DataType.VAR_DECLARATION_WITHOUT_ASSIGNATION, validConst.type)
    }

    @Test
    fun `SimpleAssignmentParser canParse and parses correctly`() {
        val assignmentParser = SimpleAssignmentParser()

        val tokens = containerOf(
            token(DataType.IDENTIFIER, "x"),
            token(DataType.ASSIGNATION, "="),
            token(DataType.NUMBER_LITERAL, "10")
        )

        assertTrue(assignmentParser.canParse(tokens))
        val dummyParser = Parser(tokens, "1.0")
        val node = assignmentParser.parse(tokens, dummyParser)
        assertEquals(DataType.ASSIGNATION, node.type)
        assertEquals(DataType.IDENTIFIER, node.children[0].type)
        assertEquals(DataType.NUMBER_LITERAL, node.children[1].type)

        val invalidTokens = containerOf(token(DataType.NUMBER_LITERAL, "10"))
        assertFalse(assignmentParser.canParse(invalidTokens))
    }

    @Test
    fun `IfStatementParser canParse and delegates to ifStmtParse`() {
        val ifParser = IfStatementParser()

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
            token(DataType.CLOSE_BRACE, "}")
        )

        assertTrue(ifParser.canParse(tokens))
        val dummyParser = Parser(tokens, "1.1")
        val node = ifParser.parse(tokens, dummyParser)
        assertEquals(DataType.IF_STATEMENT, node.type)

        val notIfTokens = containerOf(token(DataType.IDENTIFIER, "x"))
        assertFalse(ifParser.canParse(notIfTokens))
    }

    @Test
    fun `ExpressionStatementParser parses expressions`() {
        val expParser = ExpressionStatementParser()

        val tokens = containerOf(
            token(DataType.PRINTLN, "println"),
            token(DataType.OPEN_PARENTHESIS, "("),
            token(DataType.STRING_LITERAL, "hello"),
            token(DataType.CLOSE_PARENTHESIS, ")")
        )

        assertTrue(expParser.canParse(tokens))
        val dummyParser = Parser(tokens, "1.0")
        val node = expParser.parse(tokens, dummyParser)
        assertEquals(DataType.FUNCTION_CALL, node.type)
    }

    @Test
    fun `StatementParserFactory configures parsers according to version`() {
        val v10Parsers = StatementParserFactory.createParsers(VersionConfig.getFeatures("1.0"), "1.0")
        assertFalse(v10Parsers.any { it is IfStatementParser })

        val v11Parsers = StatementParserFactory.createParsers(VersionConfig.getFeatures("1.1"), "1.1")
        assertTrue(v11Parsers.any { it is IfStatementParser })
    }

    @Test
    fun `Custom StatementParser plugin can be injected into Parser`() {
        val customParser = object : StatementParser {
            override fun canParse(tokens: Container): Boolean {
                return !tokens.isEmpty() && tokens.get(0)?.type == DataType.IDENTIFIER && tokens.get(0)?.content == "customKeyword"
            }

            override fun parse(tokens: Container, parser: Parser): ASTNode {
                return ASTNode(DataType.IDENTIFIER, "CUSTOM_HANDLED", Position(1, 1), emptyList())
            }
        }

        val tokens = containerOf(
            token(DataType.IDENTIFIER, "customKeyword"),
            token(DataType.SEMICOLON, ";")
        )

        val parser = Parser(tokens, "1.1", listOf(customParser))
        val result = parser.parse()
        assertEquals(DataType.IDENTIFIER, result.type)
        assertEquals("CUSTOM_HANDLED", result.content)
    }
}
