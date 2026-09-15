package parser.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.src.main.kotlin.Parser

fun buildTokenContainer(vararg tokens: Pair<DataType, String>): Container {
    var container = Container()
    for ((type, content) in tokens) {
        container = container.addContainer(Token(type, content, Position(0, 0)))
    }
    return container
}

class ParserTest {

    @Test
    fun newParserTest() {
        val container = buildTokenContainer(
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_TYPE to "5",
            DataType.ADDITION to "+",
            DataType.NUMBER_LITERAL to "3",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root = parser.parse()

        assertEquals(ASTNodeType.FUNCTION_CALL, root.type)
        assertEquals("println", root.content)
        assertEquals(ASTNodeType.ADDITION, root.children[0].type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, root.children[0].children[0].type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, root.children[0].children[1].type)
    }

    @Test
    fun basicAssignationTest() {
        val container = buildTokenContainer(
            DataType.LET_KEYWORD to "let",
            DataType.SPACE to " ",
            DataType.IDENTIFIER to "myVar",
            DataType.COLON to ":",
            DataType.SPACE to " ",
            DataType.NUMBER_TYPE to "number",
            DataType.SPACE to " ",
            DataType.ASSIGNATION to "=",
            DataType.SPACE to " ",
            DataType.NUMBER_LITERAL to "14",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()
        val declaration: ASTNode = root.children[0]

        assertEquals(ASTNodeType.DECLARATION, root.type)
        assertEquals(ASTNodeType.LET_KEYWORD, declaration.type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, root.children[1].type)
        assertEquals(ASTNodeType.IDENTIFIER, declaration.children[0].type)
        assertEquals(ASTNodeType.NUMBER_TYPE, declaration.children[1].type)
    }

    @Test
    fun basicPrintTest() {
        val container = buildTokenContainer(
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.STRING_LITERAL to "hi",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.FUNCTION_CALL, root.type)
        assertEquals(ASTNodeType.STRING_LITERAL, root.children[0].type)
    }

    @Test
    fun basicArithmeticTest() {
        /*
        Expected output:
          +
        8   *
           2 3
        */

        val container = buildTokenContainer(
            DataType.NUMBER_LITERAL to "8",
            DataType.ADDITION to "+",
            DataType.NUMBER_LITERAL to "2",
            DataType.MULTIPLICATION to "*",
            DataType.NUMBER_LITERAL to "3",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()
        val mult = root.children[1]

        assertEquals(ASTNodeType.ADDITION, root.type)
        assertEquals(ASTNodeType.MULTIPLICATION, mult.type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, mult.children[0].type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, mult.children[1].type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, root.children[0].type)
    }

    @Test
    fun basicArithmeticTest2() {
        /*
        10 - 8 + 4 * 3 / 2 + 4
        Expected output:


        (10-8 + 4*3) / 2 + 4
         */
        val container = buildTokenContainer(
            DataType.NUMBER_LITERAL to "10",
            DataType.SUBTRACTION to "-",
            DataType.NUMBER_LITERAL to "8",
            DataType.ADDITION to "+",
            DataType.NUMBER_LITERAL to "4",
            DataType.MULTIPLICATION to "*",
            DataType.NUMBER_LITERAL to "3",
            DataType.DIVISION to "/",
            DataType.NUMBER_LITERAL to "2",
            DataType.ADDITION to "+",
            DataType.NUMBER_LITERAL to "4",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        val sum = root
        val sub = sum.children[0].children[0]
        val div = sum.children[0].children[1]
        val mul = div.children[0]

        assertEquals(ASTNodeType.ADDITION, root.type)
        assertEquals(ASTNodeType.ADDITION, sum.type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, root.children[1].type)
        assertEquals(ASTNodeType.SUBTRACTION, sub.type)
        assertEquals(ASTNodeType.DIVISION, div.type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, sub.children[0].type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, sub.children[1].type)
        assertEquals(ASTNodeType.MULTIPLICATION, mul.type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, div.children[1].type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, mul.children[0].type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, mul.children[1].type)
    }

    @Test
    fun tckTests() {
        val container = buildTokenContainer(
            DataType.LET_KEYWORD to "let",
            DataType.SPACE to " ",
            DataType.IDENTIFIER to "a",
            DataType.COLON to ":",
            DataType.SPACE to " ",
            DataType.NUMBER_TYPE to "number",
            DataType.SPACE to " ",
            DataType.ASSIGNATION to "=",
            DataType.SPACE to " ",
            DataType.NUMBER_LITERAL to "21",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root = parser.parse()
    }

    @Test
    fun constAssignationTest() {
        val container = buildTokenContainer(
            DataType.CONST_KEYWORD to "const",
            DataType.SPACE to " ",
            DataType.IDENTIFIER to "myVar",
            DataType.COLON to ":",
            DataType.SPACE to " ",
            DataType.NUMBER_TYPE to "number",
            DataType.SPACE to " ",
            DataType.ASSIGNATION to "=",
            DataType.SPACE to " ",
            DataType.NUMBER_LITERAL to "14",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()
        val declaration: ASTNode = root.children[0]

        assertEquals(ASTNodeType.DECLARATION, root.type)
        assertEquals(ASTNodeType.CONST_KEYWORD, declaration.type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, root.children[1].type)
        assertEquals(ASTNodeType.IDENTIFIER, declaration.children[0].type)
        assertEquals(ASTNodeType.NUMBER_TYPE, declaration.children[1].type)
    }

    @Test
    fun ifStatementTest() {
        val container = buildTokenContainer(
            DataType.IF_KEYWORD to "if",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.BOOLEAN_LITERAL to "true",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.OPEN_BRACE to "{",
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "5",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";",
            DataType.CLOSE_BRACE to "}"
        )
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.IF_STATEMENT, root.type)
        assertEquals(ASTNodeType.BOOLEAN_LITERAL, root.children[0].type)
        assertEquals(ASTNodeType.BLOCK, root.children[1].type)
        assertEquals(ASTNodeType.FUNCTION_CALL, root.children[1].children[0].type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, root.children[1].children[0].children[0].type)
    }

    @Test
    fun testPrattParserWithSimpleExpression() {
        val container = buildTokenContainer(
            DataType.NUMBER_LITERAL to "5",
            DataType.MULTIPLICATION to "*",
            DataType.NUMBER_LITERAL to "3"
        )

        val parser = Parser(container)
        val result = parser.expParse(container)

        assertEquals(ASTNodeType.MULTIPLICATION, result.type)
        assertEquals("5", result.children[0].content)
        assertEquals("3", result.children[1].content)
    }

    @Test
    fun testPrattParserWithParentheses() {
        val container = buildTokenContainer(
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "5",
            DataType.ADDITION to "+",
            DataType.NUMBER_LITERAL to "3",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.MULTIPLICATION to "*",
            DataType.NUMBER_LITERAL to "2"
        )

        val parser = Parser(container)
        val result = parser.expParse(container)

        assertEquals(ASTNodeType.MULTIPLICATION, result.type)
        assertEquals(ASTNodeType.ADDITION, result.children[0].type)
        assertEquals("5", result.children[0].children[0].content)
        assertEquals("3", result.children[0].children[1].content)
        assertEquals("2", result.children[1].content)
    }

    @Test
    fun ifElseStatementTest() {
        val container = buildTokenContainer(
            DataType.IF_KEYWORD to "if",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.BOOLEAN_LITERAL to "false",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.OPEN_BRACE to "{",
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "5",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";",
            DataType.CLOSE_BRACE to "}",
            DataType.ELSE_KEYWORD to "else",
            DataType.OPEN_BRACE to "{",
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "10",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";",
            DataType.CLOSE_BRACE to "}"
        )
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.IF_STATEMENT, root.type)
        assertEquals(ASTNodeType.BOOLEAN_LITERAL, root.children[0].type)
        assertEquals(ASTNodeType.BLOCK, root.children[1].type)
        assertEquals(ASTNodeType.FUNCTION_CALL, root.children[1].children[0].type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, root.children[1].children[0].children[0].type)
        assertEquals(ASTNodeType.BLOCK, root.children[2].type)
        assertEquals(ASTNodeType.FUNCTION_CALL, root.children[2].children[0].type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, root.children[2].children[0].children[0].type)
    }

    @Test
    fun emptyStatementTest() {
        val container = buildTokenContainer(
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun invalidExpressionTest() {
        val container = buildTokenContainer(
            DataType.NUMBER_LITERAL to "5",
            DataType.MULTIPLICATION to "*"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun declarationWithoutAssignationTest() {
        val container = buildTokenContainer(
            DataType.LET_KEYWORD to "let",
            DataType.SPACE to " ",
            DataType.IDENTIFIER to "x",
            DataType.COLON to ":",
            DataType.SPACE to " ",
            DataType.NUMBER_TYPE to "number",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.VAR_DECLARATION_WITHOUT_ASSIGNATION, root.type)
        assertEquals(ASTNodeType.LET_KEYWORD, root.children[0].type)
        assertEquals(ASTNodeType.IDENTIFIER, root.children[0].children[0].type)
        assertEquals(ASTNodeType.NUMBER_TYPE, root.children[0].children[1].type)
    }

    @Test
    fun simpleAssignationTest() {
        val container = buildTokenContainer(
            DataType.IDENTIFIER to "x",
            DataType.SPACE to " ",
            DataType.ASSIGNATION to "=",
            DataType.SPACE to " ",
            DataType.NUMBER_LITERAL to "5",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.ASSIGNATION, root.type)
        assertEquals(ASTNodeType.IDENTIFIER, root.children[0].type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, root.children[1].type)
    }

    @Test
    fun functionCallNoArgumentsTest() {
        val container = buildTokenContainer(
            DataType.READ_INPUT to "readInput",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.FUNCTION_CALL, root.type)
        assertEquals("readInput", root.content)
        assertEquals(0, root.children.size)
    }

    @Test
    fun complexArithmeticTest() {
        // (5 + 3) * 2 - 8 / 4
        val container = buildTokenContainer(
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "5",
            DataType.ADDITION to "+",
            DataType.NUMBER_LITERAL to "3",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.MULTIPLICATION to "*",
            DataType.NUMBER_LITERAL to "2",
            DataType.SUBTRACTION to "-",
            DataType.NUMBER_LITERAL to "8",
            DataType.DIVISION to "/",
            DataType.NUMBER_LITERAL to "4",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.SUBTRACTION, root.type)
        assertEquals(ASTNodeType.MULTIPLICATION, root.children[0].type)
        assertEquals(ASTNodeType.DIVISION, root.children[1].type)
        assertEquals(ASTNodeType.ADDITION, root.children[0].children[0].type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, root.children[0].children[1].type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, root.children[0].children[0].children[0].type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, root.children[0].children[0].children[1].type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, root.children[1].children[0].type)
        assertEquals(ASTNodeType.NUMBER_LITERAL, root.children[1].children[1].type)
    }

    @Test
    fun ifStatementWithoutElseTest() {
        val container = buildTokenContainer(
            DataType.IF_KEYWORD to "if",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.BOOLEAN_LITERAL to "true",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.OPEN_BRACE to "{",
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "5",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";",
            DataType.CLOSE_BRACE to "}"
        )
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.IF_STATEMENT, root.type)
        assertEquals(ASTNodeType.BOOLEAN_LITERAL, root.children[0].type)
        assertEquals(ASTNodeType.BLOCK, root.children[1].type)
        assertEquals(2, root.children.size)
    }

    @Test
    fun invalidIfStatementTest() {
        val container = buildTokenContainer(
            DataType.IF_KEYWORD to "if",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.BOOLEAN_LITERAL to "true",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.OPEN_BRACE to "{"
        )
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }



    @Test
    fun isDeclarationWithAssignmentMissingIdentifierTest() {
        val container = buildTokenContainer(
            DataType.LET_KEYWORD to "let",
            DataType.COLON to ":",
            DataType.NUMBER_TYPE to "number",
            DataType.ASSIGNATION to "=",
            DataType.NUMBER_LITERAL to "5",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun isDeclarationWithAssignmentMissingColonTest() {
        val container = buildTokenContainer(
            DataType.LET_KEYWORD to "let",
            DataType.IDENTIFIER to "x",
            DataType.NUMBER_TYPE to "number",
            DataType.ASSIGNATION to "=",
            DataType.NUMBER_LITERAL to "5",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun isDeclarationWithAssignmentMissingAssignationTest() {
        val container = buildTokenContainer(
            DataType.LET_KEYWORD to "let",
            DataType.IDENTIFIER to "x",
            DataType.COLON to ":",
            DataType.NUMBER_TYPE to "number",
            DataType.NUMBER_LITERAL to "5",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun isDeclarationWithoutAssignmentInvalidSizeTest() {
        val container = buildTokenContainer(
            DataType.LET_KEYWORD to "let",
            DataType.IDENTIFIER to "x",
            DataType.COLON to ":",
            DataType.NUMBER_TYPE to "number",
            DataType.IDENTIFIER to "extra",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun isDeclarationWithoutAssignmentInvalidKeywordTest() {
        val container = buildTokenContainer(
            DataType.IDENTIFIER to "var",
            DataType.IDENTIFIER to "x",
            DataType.COLON to ":",
            DataType.NUMBER_TYPE to "number",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun isDeclarationWithoutAssignmentMissingIdentifierTest2() {
        val container = buildTokenContainer(
            DataType.LET_KEYWORD to "let",
            DataType.COLON to ":",
            DataType.NUMBER_TYPE to "number",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun isDeclarationWithoutAssignmentMissingColonTest2() {
        val container = buildTokenContainer(
            DataType.LET_KEYWORD to "let",
            DataType.IDENTIFIER to "x",
            DataType.NUMBER_TYPE to "number",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun isDeclarationWithoutAssignmentMissingTypeTest() {
        val container = buildTokenContainer(
            DataType.LET_KEYWORD to "let",
            DataType.IDENTIFIER to "x",
            DataType.COLON to ":",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun isDeclarationWithoutAssignmentWithAssignationTest() {
        val container = buildTokenContainer(
            DataType.LET_KEYWORD to "let",
            DataType.IDENTIFIER to "x",
            DataType.COLON to ":",
            DataType.NUMBER_TYPE to "number",
            DataType.ASSIGNATION to "=",
            DataType.NUMBER_LITERAL to "5",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.DECLARATION, root.type) // This should be a declaration with assignment
    }

    @Test
    fun isSimpleAssignmentInvalidSizeTest() {
        val container = buildTokenContainer(
            DataType.IDENTIFIER to "x",
            DataType.ASSIGNATION to "=",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun isSimpleAssignmentNotIdentifierTest() {
        val container = buildTokenContainer(
            DataType.NUMBER_LITERAL to "5",
            DataType.ASSIGNATION to "=",
            DataType.IDENTIFIER to "x",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun isSimpleAssignmentNotAssignationTest() {
        val container = buildTokenContainer(
            DataType.IDENTIFIER to "x",
            DataType.ADDITION to "+",
            DataType.NUMBER_LITERAL to "5",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.ADDITION, root.type)
    }

    @Test
    fun isFunctionCallInvalidSizeTest() {
        val container = buildTokenContainer(
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun isFunctionCallInvalidFunctionNameTest() {
        val container = buildTokenContainer(
            DataType.IDENTIFIER to "invalidFunction",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "5",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun isFunctionCallMissingOpenParenthesisTest() {
        val container = buildTokenContainer(
            DataType.PRINTLN to "println",
            DataType.NUMBER_LITERAL to "5",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun isFunctionCallMissingCloseParenthesisTest() {
        val container = buildTokenContainer(
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "5",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun isArithNoOperatorsTest() {
        val container = buildTokenContainer(
            DataType.NUMBER_LITERAL to "5",
            DataType.NUMBER_LITERAL to "5",
            DataType.NUMBER_LITERAL to "5",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun isLiteralInvalidSizeTest() {
        val container = buildTokenContainer(
            DataType.NUMBER_LITERAL to "5",
            DataType.NUMBER_LITERAL to "5",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }


    @Test
    fun isLiteralBooleanNotSupportedTest() {
        val container = buildTokenContainer(
            DataType.BOOLEAN_LITERAL to "true",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container, "1.0") // version 1.0 does not support booleans
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }


    @Test
    fun isIfMissingIfKeywordTest() {
        val container = buildTokenContainer(
            DataType.OPEN_PARENTHESIS to "(",
            DataType.BOOLEAN_LITERAL to "true",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.OPEN_BRACE to "{",
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "5",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";",
            DataType.CLOSE_BRACE to "}"
        )
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun isIfInvalidConditionMissingOpenParenTest() {
        val container = buildTokenContainer(
            DataType.IF_KEYWORD to "if",
            DataType.BOOLEAN_LITERAL to "true",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.OPEN_BRACE to "{",
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "5",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";",
            DataType.CLOSE_BRACE to "}"
        )
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }



    @Test
    fun isIfMissingOpenBraceTest() {
        val container = buildTokenContainer(
            DataType.IF_KEYWORD to "if",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.BOOLEAN_LITERAL to "true",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "5",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";",
            DataType.CLOSE_BRACE to "}"
        )
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun isIfElseValidTest() {
        val container = buildTokenContainer(
            DataType.IF_KEYWORD to "if",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.BOOLEAN_LITERAL to "true",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.OPEN_BRACE to "{",
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "5",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";",
            DataType.CLOSE_BRACE to "}",
            DataType.ELSE_KEYWORD to "else",
            DataType.OPEN_BRACE to "{",
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "10",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";",
            DataType.CLOSE_BRACE to "}"
        )
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.IF_STATEMENT, root.type)
    }

    @Test
    fun isIfElseMissingElseKeywordTest() {
        val container = buildTokenContainer(
            DataType.IF_KEYWORD to "if",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.BOOLEAN_LITERAL to "true",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.OPEN_BRACE to "{",
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "5",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";",
            DataType.CLOSE_BRACE to "}"
        )
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.IF_STATEMENT, root.type)
    }

    @Test
    fun isIfElseMissingOpenBraceInElseTest() {
        val container = buildTokenContainer(
            DataType.IF_KEYWORD to "if",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.BOOLEAN_LITERAL to "true",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.OPEN_BRACE to "{",
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "5",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";",
            DataType.CLOSE_BRACE to "}",
            DataType.ELSE_KEYWORD to "else",
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "10",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";",
            DataType.CLOSE_BRACE to "}"
        )
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun isIfElseMissingCloseBraceInElseTest() {
        val container = buildTokenContainer(
            DataType.IF_KEYWORD to "if",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.BOOLEAN_LITERAL to "true",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.OPEN_BRACE to "{",
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "5",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";",
            DataType.CLOSE_BRACE to "}",
            DataType.ELSE_KEYWORD to "else",
            DataType.OPEN_BRACE to "{",
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "10",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun processTokensWithSizeLessThanOrEqualToOne() {
        val parser = Parser(Container(), "1.1")
        val token = Token(DataType.NUMBER_LITERAL, "5", Position(0, 0))
        val prattToken = parser.tokenFactory.createPrattToken(token)
        val symbols = listOf(prattToken)
        val result = parser.processTokens(symbols)
        assertEquals(symbols, result)
    }

    @Test
    fun processTokensWithNoOperators() {
        val parser = Parser(Container(), "1.1")
        val token1 = Token(DataType.NUMBER_LITERAL, "5", Position(0, 0))
        val token2 = Token(DataType.NUMBER_LITERAL, "3", Position(0, 0))
        val prattToken1 = parser.tokenFactory.createPrattToken(token1)
        val prattToken2 = parser.tokenFactory.createPrattToken(token2)
        val symbols = listOf(prattToken1, prattToken2)
        val result = parser.processTokens(symbols)
        assertEquals(symbols, result)
    }

    @Test
    fun arithParseTest() {
        val container = buildTokenContainer(
            DataType.NUMBER_LITERAL to "5",
            DataType.MULTIPLICATION to "*",
            DataType.NUMBER_LITERAL to "3",
            DataType.ADDITION to "+",
            DataType.NUMBER_LITERAL to "2"
        )
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.arithParse(container)

        assertEquals(ASTNodeType.ADDITION, root.type)
        assertEquals(ASTNodeType.MULTIPLICATION, root.children[0].type)
    }

    @Test
    fun parseBlockWithMultipleStatementsTest() {
        val container = buildTokenContainer(
            DataType.LET_KEYWORD to "let",
            DataType.IDENTIFIER to "x",
            DataType.COLON to ":",
            DataType.NUMBER_TYPE to "number",
            DataType.ASSIGNATION to "=",
            DataType.NUMBER_LITERAL to "5",
            DataType.SEMICOLON to ";",
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.IDENTIFIER to "x",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";"
        )
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.DECLARATION, root.type)
    }

    @Test
    fun isIfWithElseKeywordTest() {
        val container = buildTokenContainer(
            DataType.IF_KEYWORD to "if",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.BOOLEAN_LITERAL to "true",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.OPEN_BRACE to "{",
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "5",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";",
            DataType.CLOSE_BRACE to "}",
            DataType.ELSE_KEYWORD to "else",
            DataType.OPEN_BRACE to "{",
            DataType.CLOSE_BRACE to "}"
        )
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.IF_STATEMENT, root.type)
    }

    @Test
    fun isIfElseMissingCloseBraceInIfTest() {
        val container = buildTokenContainer(
            DataType.IF_KEYWORD to "if",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.BOOLEAN_LITERAL to "true",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.OPEN_BRACE to "{",
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "5",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";",
            DataType.ELSE_KEYWORD to "else",
            DataType.OPEN_BRACE to "{",
            DataType.PRINTLN to "println",
            DataType.OPEN_PARENTHESIS to "(",
            DataType.NUMBER_LITERAL to "10",
            DataType.CLOSE_PARENTHESIS to ")",
            DataType.SEMICOLON to ";",
            DataType.CLOSE_BRACE to "}"
        )
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(ASTNodeType.INVALID, root.type)
    }

    @Test
    fun associateOperationTest() {
        val parser = Parser(Container(), "1.1")
        val five = Token(DataType.NUMBER_LITERAL, "5", Position(0, 0))
        val plus = Token(DataType.ADDITION, "+", Position(0, 0))
        val three = Token(DataType.NUMBER_LITERAL, "3", Position(0, 0))

        val fivePratt = parser.tokenFactory.createPrattToken(five)
        val plusPratt = parser.tokenFactory.createPrattToken(plus)
        val threePratt = parser.tokenFactory.createPrattToken(three)

        val symbols = listOf(fivePratt, plusPratt, threePratt)
        val result = parser.associateOperation(symbols, 1)

        assertEquals(1, result.size)
        assertEquals(plus, result[0].token())
        assertEquals(five, result[0].allChildren()[0].token())
        assertEquals(three, result[0].allChildren()[1].token())
    }

    @Test
    fun highestPrecedIndexWithSamePrecedenceTest() {
        val parser = Parser(Container(), "1.1")
        val five = Token(DataType.NUMBER_LITERAL, "5", Position(0, 0))
        val plus = Token(DataType.ADDITION, "+", Position(0, 0))
        val three = Token(DataType.NUMBER_LITERAL, "3", Position(0, 0))
        val minus = Token(DataType.SUBTRACTION, "-", Position(0, 0))
        val two = Token(DataType.NUMBER_LITERAL, "2", Position(0, 0))

        val fivePratt = parser.tokenFactory.createPrattToken(five)
        val plusPratt = parser.tokenFactory.createPrattToken(plus)
        val threePratt = parser.tokenFactory.createPrattToken(three)
        val minusPratt = parser.tokenFactory.createPrattToken(minus)
        val twoPratt = parser.tokenFactory.createPrattToken(two)

        val symbols = listOf(fivePratt, plusPratt, threePratt, minusPratt, twoPratt)
        val result = parser.highestPrecedIndex(symbols)

        assertEquals(1, result)
    }

    @Test
    fun prattToASTWithNoChildrenTest() {
        val parser = Parser(Container(), "1.1")
        val five = Token(DataType.NUMBER_LITERAL, "5", Position(0, 0))
        val fivePratt = parser.tokenFactory.createPrattToken(five)
        val result = parser.prattToAST(fivePratt)

        assertEquals(ASTNodeType.NUMBER_LITERAL, result.type)
        assertEquals("5", result.content)
        assertEquals(0, result.children.size)
    }
}


