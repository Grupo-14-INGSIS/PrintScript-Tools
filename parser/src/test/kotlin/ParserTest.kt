package parser.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.src.main.kotlin.Parser

class ParserTest {

    @Test
    fun newParserTest() {
        var container = Container()
        val sentence = listOf("println", "(", "5", "+", "3", ")", ";")
        val dataTypes = listOf(
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.ADDITION,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root = parser.parse()

        assertEquals(DataType.PRINTLN, root.type)
        assertEquals(DataType.ADDITION, root.children[0].type)
        assertEquals(DataType.NUMBER_LITERAL, root.children[0].children[0].type)
        assertEquals(DataType.NUMBER_LITERAL, root.children[0].children[1].type)
    }

    @Test
    fun basicAssignationTest() {
        var container = Container()
        val sentence = listOf("let", " ", "myVar", ":", " ", "number", " ", "=", " ", "14", ";")
        val dataTypes = listOf(
            DataType.LET_KEYWORD,
            DataType.SPACE,
            DataType.IDENTIFIER,
            DataType.COLON,
            DataType.SPACE,
            DataType.NUMBER_TYPE,
            DataType.SPACE,
            DataType.ASSIGNATION,
            DataType.SPACE,
            DataType.NUMBER_LITERAL,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()
        val declaration: ASTNode = root.children[0]

        assertEquals(DataType.DECLARATION, root.type)
        assertEquals(DataType.LET_KEYWORD, declaration.type)
        assertEquals(DataType.NUMBER_LITERAL, root.children[1].type)
        assertEquals(DataType.IDENTIFIER, declaration.children[0].type)
        assertEquals(DataType.NUMBER_TYPE, declaration.children[1].type)
    }

    @Test
    fun basicPrintTest() {
        var container = Container()
        val sentence = listOf("println", "(", "hi", ")", ";")
        val dataTypes = listOf(
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.STRING_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.PRINTLN, root.type)
        assertEquals(DataType.STRING_LITERAL, root.children[0].type)
    }

    @Test
    fun basicArithmeticTest() {
        /*
        Expected output:
          +
        8   *
           2 3
        */

        var container = Container()
        val sentence = listOf("8", "+", "2", "*", "3", ";")
        val dataTypes = listOf(
            DataType.NUMBER_LITERAL,
            DataType.ADDITION,
            DataType.NUMBER_LITERAL,
            DataType.MULTIPLICATION,
            DataType.NUMBER_LITERAL,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()
        val mult = root.children[1]

        assertEquals(DataType.ADDITION, root.type)
        assertEquals(DataType.MULTIPLICATION, mult.type)
        assertEquals(DataType.NUMBER_LITERAL, mult.children[0].type)
        assertEquals(DataType.NUMBER_LITERAL, mult.children[1].type)
        assertEquals(DataType.NUMBER_LITERAL, root.children[0].type)
    }

    @Test
    fun basicArithmeticTest2() {
        /*
        10 - 8 + 4 * 3 / 2 + 4
        Expected output:


        (10-8 + 4*3) / 2 + 4
         */
        var container = Container()
        val sentence = listOf("10", "-", "8", "+", "4", "*", "3", "/", "2", "+", "4", ";")
        val dataTypes = listOf(
            DataType.NUMBER_LITERAL,
            DataType.SUBTRACTION,
            DataType.NUMBER_LITERAL,
            DataType.ADDITION,
            DataType.NUMBER_LITERAL,
            DataType.MULTIPLICATION,
            DataType.NUMBER_LITERAL,
            DataType.DIVISION,
            DataType.NUMBER_LITERAL,
            DataType.ADDITION,
            DataType.NUMBER_LITERAL,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        val sum = root
        val sub = sum.children[0].children[0]
        val div = sum.children[0].children[1]
        val mul = div.children[0]

        assertEquals(DataType.ADDITION, root.type)
        assertEquals(DataType.ADDITION, sum.type)
        assertEquals(DataType.NUMBER_LITERAL, root.children[1].type)
        assertEquals(DataType.SUBTRACTION, sub.type)
        assertEquals(DataType.DIVISION, div.type)
        assertEquals(DataType.NUMBER_LITERAL, sub.children[0].type)
        assertEquals(DataType.NUMBER_LITERAL, sub.children[1].type)
        assertEquals(DataType.MULTIPLICATION, mul.type)
        assertEquals(DataType.NUMBER_LITERAL, div.children[1].type)
        assertEquals(DataType.NUMBER_LITERAL, mul.children[0].type)
        assertEquals(DataType.NUMBER_LITERAL, mul.children[1].type)
    }

    @Test
    fun tckTests() {
        var container = Container()
        val sentence = listOf("let", " ", "a", ":", " ", "number", " ", "=", " ", "21", ";")
        val dataTypes = listOf(
            DataType.LET_KEYWORD,
            DataType.SPACE,
            DataType.IDENTIFIER,
            DataType.COLON,
            DataType.SPACE,
            DataType.NUMBER_TYPE,
            DataType.SPACE,
            DataType.ASSIGNATION,
            DataType.SPACE,
            DataType.NUMBER_LITERAL,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root = parser.parse()
    }

    @Test
    fun constAssignationTest() {
        var container = Container()
        val sentence = listOf("const", " ", "myVar", ":", " ", "number", " ", "=", " ", "14", ";")
        val dataTypes = listOf(
            DataType.CONST_KEYWORD,
            DataType.SPACE,
            DataType.IDENTIFIER,
            DataType.COLON,
            DataType.SPACE,
            DataType.NUMBER_TYPE,
            DataType.SPACE,
            DataType.ASSIGNATION,
            DataType.SPACE,
            DataType.NUMBER_LITERAL,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()
        val declaration: ASTNode = root.children[0]

        assertEquals(DataType.DECLARATION, root.type)
        assertEquals(DataType.CONST_KEYWORD, declaration.type)
        assertEquals(DataType.NUMBER_LITERAL, root.children[1].type)
        assertEquals(DataType.IDENTIFIER, declaration.children[0].type)
        assertEquals(DataType.NUMBER_TYPE, declaration.children[1].type)
    }

    @Test
    fun ifStatementTest() {
        var container = Container()
        val sentence = listOf("if", "(", "true", ")", "{", "println", "(", "5", ")", ";", "}")
        val dataTypes = listOf(
            DataType.IF_KEYWORD,
            DataType.OPEN_PARENTHESIS,
            DataType.BOOLEAN_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.OPEN_BRACE,
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON,
            DataType.CLOSE_BRACE
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(DataType.IF_STATEMENT, root.type)
        assertEquals(DataType.BOOLEAN_LITERAL, root.children[0].type)
        assertEquals(DataType.BLOCK, root.children[1].type)
        assertEquals(DataType.PRINTLN, root.children[1].children[0].type)
        assertEquals(DataType.NUMBER_LITERAL, root.children[1].children[0].children[0].type)
    }

    @Test
    fun testPrattParserWithSimpleExpression() {
        var container = Container()
        val sentence = listOf("5", "*", "3")
        val dataTypes = listOf(
            DataType.NUMBER_LITERAL,
            DataType.MULTIPLICATION,
            DataType.NUMBER_LITERAL
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }

        val parser = Parser(container)
        val result = parser.expParse(container)

        assertEquals(DataType.MULTIPLICATION, result.type)
        assertEquals("5", result.children[0].content)
        assertEquals("3", result.children[1].content)
    }

    @Test
    fun testPrattParserWithParentheses() {
        var container = Container()
        val sentence = listOf("(", "5", "+", "3", ")", "*", "2")
        val dataTypes = listOf(
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.ADDITION,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.MULTIPLICATION,
            DataType.NUMBER_LITERAL
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }

        val parser = Parser(container)
        val result = parser.expParse(container)

        assertEquals(DataType.MULTIPLICATION, result.type)
        assertEquals(DataType.ADDITION, result.children[0].type)
        assertEquals("5", result.children[0].children[0].content)
        assertEquals("3", result.children[0].children[1].content)
        assertEquals("2", result.children[1].content)
    }

    @Test
    fun ifElseStatementTest() {
        var container = Container()
        val sentence = listOf(
            "if", "(", "false", ")", "{", "println", "(", "5", ")", ";", "}",
            "else", "{", "println", "(", "10", ")", ";", "}"
        )
        val dataTypes = listOf(
            DataType.IF_KEYWORD,
            DataType.OPEN_PARENTHESIS,
            DataType.BOOLEAN_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.OPEN_BRACE,
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON,
            DataType.CLOSE_BRACE,
            DataType.ELSE_KEYWORD,
            DataType.OPEN_BRACE,
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON,
            DataType.CLOSE_BRACE
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(DataType.IF_STATEMENT, root.type)
        assertEquals(DataType.BOOLEAN_LITERAL, root.children[0].type)
        assertEquals(DataType.BLOCK, root.children[1].type)
        assertEquals(DataType.PRINTLN, root.children[1].children[0].type)
        assertEquals(DataType.NUMBER_LITERAL, root.children[1].children[0].children[0].type)
        assertEquals(DataType.BLOCK, root.children[2].type)
        assertEquals(DataType.PRINTLN, root.children[2].children[0].type)
        assertEquals(DataType.NUMBER_LITERAL, root.children[2].children[0].children[0].type)
    }

    @Test
    fun emptyStatementTest() {
        var container = Container()
        val sentence = listOf(";")
        val dataTypes = listOf(DataType.SEMICOLON)
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }

    @Test
    fun invalidExpressionTest() {
        var container = Container()
        val sentence = listOf("5", "*")
        val dataTypes = listOf(
            DataType.NUMBER_LITERAL,
            DataType.MULTIPLICATION
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }

    @Test
    fun declarationWithoutAssignationTest() {
        var container = Container()
        val sentence = listOf("let", " ", "x", ":", " ", "number", ";")
        val dataTypes = listOf(
            DataType.LET_KEYWORD,
            DataType.SPACE,
            DataType.IDENTIFIER,
            DataType.COLON,
            DataType.SPACE,
            DataType.NUMBER_TYPE,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.VAR_DECLARATION_WITHOUT_ASSIGNATION, root.type)
        assertEquals(DataType.LET_KEYWORD, root.children[0].type)
        assertEquals(DataType.IDENTIFIER, root.children[0].children[0].type)
        assertEquals(DataType.NUMBER_TYPE, root.children[0].children[1].type)
    }

    @Test
    fun simpleAssignationTest() {
        var container = Container()
        val sentence = listOf("x", " ", "=", " ", "5", ";")
        val dataTypes = listOf(
            DataType.IDENTIFIER,
            DataType.SPACE,
            DataType.ASSIGNATION,
            DataType.SPACE,
            DataType.NUMBER_LITERAL,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.ASSIGNATION, root.type)
        assertEquals(DataType.IDENTIFIER, root.children[0].type)
        assertEquals(DataType.NUMBER_LITERAL, root.children[1].type)
    }

    @Test
    fun functionCallNoArgumentsTest() {
        var container = Container()
        val sentence = listOf("readInput", "(", ")", ";")
        val dataTypes = listOf(
            DataType.READ_INPUT,
            DataType.OPEN_PARENTHESIS,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(DataType.READ_INPUT, root.type)
        assertEquals(0, root.children.size)
    }

    @Test
    fun complexArithmeticTest() {
        // (5 + 3) * 2 - 8 / 4
        var container = Container()
        val sentence = listOf("(", "5", "+", "3", ")", "*", "2", "-", "8", "/", "4", ";")
        val dataTypes = listOf(
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.ADDITION,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.MULTIPLICATION,
            DataType.NUMBER_LITERAL,
            DataType.SUBTRACTION,
            DataType.NUMBER_LITERAL,
            DataType.DIVISION,
            DataType.NUMBER_LITERAL,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.SUBTRACTION, root.type)
        assertEquals(DataType.MULTIPLICATION, root.children[0].type)
        assertEquals(DataType.DIVISION, root.children[1].type)
        assertEquals(DataType.ADDITION, root.children[0].children[0].type)
        assertEquals(DataType.NUMBER_LITERAL, root.children[0].children[1].type)
        assertEquals(DataType.NUMBER_LITERAL, root.children[0].children[0].children[0].type)
        assertEquals(DataType.NUMBER_LITERAL, root.children[0].children[0].children[1].type)
        assertEquals(DataType.NUMBER_LITERAL, root.children[1].children[0].type)
        assertEquals(DataType.NUMBER_LITERAL, root.children[1].children[1].type)
    }

    @Test
    fun ifStatementWithoutElseTest() {
        var container = Container()
        val sentence = listOf("if", "(", "true", ")", "{", "println", "(", "5", ")", ";", "}")
        val dataTypes = listOf(
            DataType.IF_KEYWORD,
            DataType.OPEN_PARENTHESIS,
            DataType.BOOLEAN_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.OPEN_BRACE,
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON,
            DataType.CLOSE_BRACE
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(DataType.IF_STATEMENT, root.type)
        assertEquals(DataType.BOOLEAN_LITERAL, root.children[0].type)
        assertEquals(DataType.BLOCK, root.children[1].type)
        assertEquals(2, root.children.size)
    }

    @Test
    fun invalidIfStatementTest() {
        var container = Container()
        val sentence = listOf("if", "(", "true", ")", "{")
        val dataTypes = listOf(
            DataType.IF_KEYWORD,
            DataType.OPEN_PARENTHESIS,
            DataType.BOOLEAN_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.OPEN_BRACE
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }



    @Test
    fun isDeclarationWithAssignmentMissingIdentifierTest() {
        var container = Container()
        val sentence = listOf("let", ":", "number", "=", "5", ";")
        val dataTypes = listOf(
            DataType.LET_KEYWORD,
            DataType.COLON,
            DataType.NUMBER_TYPE,
            DataType.ASSIGNATION,
            DataType.NUMBER_LITERAL,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }

    @Test
    fun isDeclarationWithAssignmentMissingColonTest() {
        var container = Container()
        val sentence = listOf("let", "x", "number", "=", "5", ";")
        val dataTypes = listOf(
            DataType.LET_KEYWORD,
            DataType.IDENTIFIER,
            DataType.NUMBER_TYPE,
            DataType.ASSIGNATION,
            DataType.NUMBER_LITERAL,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }

    @Test
    fun isDeclarationWithAssignmentMissingAssignationTest() {
        var container = Container()
        val sentence = listOf("let", "x", ":", "number", "5", ";")
        val dataTypes = listOf(
            DataType.LET_KEYWORD,
            DataType.IDENTIFIER,
            DataType.COLON,
            DataType.NUMBER_TYPE,
            DataType.NUMBER_LITERAL,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }

    @Test
    fun isDeclarationWithoutAssignmentInvalidSizeTest() {
        var container = Container()
        val sentence = listOf("let", "x", ":", "number", "extra", ";")
        val dataTypes = listOf(
            DataType.LET_KEYWORD,
            DataType.IDENTIFIER,
            DataType.COLON,
            DataType.NUMBER_TYPE,
            DataType.IDENTIFIER,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }

    @Test
    fun isDeclarationWithoutAssignmentInvalidKeywordTest() {
        var container = Container()
        val sentence = listOf("var", "x", ":", "number", ";")
        val dataTypes = listOf(
            DataType.IDENTIFIER,
            DataType.IDENTIFIER,
            DataType.COLON,
            DataType.NUMBER_TYPE,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }

    @Test
    fun isDeclarationWithoutAssignmentMissingIdentifierTest2() {
        var container = Container()
        val sentence = listOf("let", ":", "number", ";")
        val dataTypes = listOf(
            DataType.LET_KEYWORD,
            DataType.COLON,
            DataType.NUMBER_TYPE,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }

    @Test
    fun isDeclarationWithoutAssignmentMissingColonTest2() {
        var container = Container()
        val sentence = listOf("let", "x", "number", ";")
        val dataTypes = listOf(
            DataType.LET_KEYWORD,
            DataType.IDENTIFIER,
            DataType.NUMBER_TYPE,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }

    @Test
    fun isDeclarationWithoutAssignmentMissingTypeTest() {
        var container = Container()
        val sentence = listOf("let", "x", ":", ";")
        val dataTypes = listOf(
            DataType.LET_KEYWORD,
            DataType.IDENTIFIER,
            DataType.COLON,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }

    @Test
    fun isDeclarationWithoutAssignmentWithAssignationTest() {
        var container = Container()
        val sentence = listOf("let", "x", ":", "number", "=", "5", ";")
        val dataTypes = listOf(
            DataType.LET_KEYWORD,
            DataType.IDENTIFIER,
            DataType.COLON,
            DataType.NUMBER_TYPE,
            DataType.ASSIGNATION,
            DataType.NUMBER_LITERAL,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.DECLARATION, root.type) // This should be a declaration with assignment
    }

    @Test
    fun isSimpleAssignmentInvalidSizeTest() {
        var container = Container()
        val sentence = listOf("x", "=", ";")
        val dataTypes = listOf(
            DataType.IDENTIFIER,
            DataType.ASSIGNATION,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }

    @Test
    fun isSimpleAssignmentNotIdentifierTest() {
        var container = Container()
        val sentence = listOf("5", "=", "x", ";")
        val dataTypes = listOf(
            DataType.NUMBER_LITERAL,
            DataType.ASSIGNATION,
            DataType.IDENTIFIER,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }

    @Test
    fun isSimpleAssignmentNotAssignationTest() {
        var container = Container()
        val sentence = listOf("x", "+", "5", ";")
        val dataTypes = listOf(
            DataType.IDENTIFIER,
            DataType.ADDITION,
            DataType.NUMBER_LITERAL,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.ADDITION, root.type)
    }

    @Test
    fun isFunctionCallInvalidSizeTest() {
        var container = Container()
        val sentence = listOf("println", "(", ";")
        val dataTypes = listOf(
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }

    @Test
    fun isFunctionCallInvalidFunctionNameTest() {
        var container = Container()
        val sentence = listOf("invalidFunction", "(", "5", ")", ";")
        val dataTypes = listOf(
            DataType.IDENTIFIER,
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }

    @Test
    fun isFunctionCallMissingOpenParenthesisTest() {
        var container = Container()
        val sentence = listOf("println", "5", ")", ";")
        val dataTypes = listOf(
            DataType.PRINTLN,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }

    @Test
    fun isFunctionCallMissingCloseParenthesisTest() {
        var container = Container()
        val sentence = listOf("println", "(", "5", ";")
        val dataTypes = listOf(
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }

    @Test
    fun isArithNoOperatorsTest() {
        var container = Container()
        val sentence = listOf("5", "5", "5", ";")
        val dataTypes = listOf(
            DataType.NUMBER_LITERAL,
            DataType.NUMBER_LITERAL,
            DataType.NUMBER_LITERAL,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }

    @Test
    fun isLiteralInvalidSizeTest() {
        var container = Container()
        val sentence = listOf("5", "5", ";")
        val dataTypes = listOf(
            DataType.NUMBER_LITERAL,
            DataType.NUMBER_LITERAL,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }


    @Test
    fun isLiteralBooleanNotSupportedTest() {
        var container = Container()
        val sentence = listOf("true", ";")
        val dataTypes = listOf(
            DataType.BOOLEAN_LITERAL,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container, "1.0") // version 1.0 does not support booleans
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }


    @Test
    fun isIfMissingIfKeywordTest() {
        var container = Container()
        val sentence = listOf("(", "true", ")", "{", "println", "(", "5", ")", ";", "}")
        val dataTypes = listOf(
            DataType.OPEN_PARENTHESIS,
            DataType.BOOLEAN_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.OPEN_BRACE,
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON,
            DataType.CLOSE_BRACE
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }

    @Test
    fun isIfInvalidConditionMissingOpenParenTest() {
        var container = Container()
        val sentence = listOf("if", "true", ")", "{", "println", "(", "5", ")", ";", "}")
        val dataTypes = listOf(
            DataType.IF_KEYWORD,
            DataType.BOOLEAN_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.OPEN_BRACE,
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON,
            DataType.CLOSE_BRACE
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }



    @Test
    fun isIfMissingOpenBraceTest() {
        var container = Container()
        val sentence = listOf("if", "(", "true", ")", "println", "(", "5", ")", ";", "}")
        val dataTypes = listOf(
            DataType.IF_KEYWORD,
            DataType.OPEN_PARENTHESIS,
            DataType.BOOLEAN_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON,
            DataType.CLOSE_BRACE
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
    }

    @Test
    fun isIfElseValidTest() {
        var container = Container()
        val sentence = listOf(
            "if", "(", "true", ")", "{", "println", "(", "5", ")", ";", "}",
            "else", "{", "println", "(", "10", ")", ";", "}"
        )
        val dataTypes = listOf(
            DataType.IF_KEYWORD,
            DataType.OPEN_PARENTHESIS,
            DataType.BOOLEAN_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.OPEN_BRACE,
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON,
            DataType.CLOSE_BRACE,
            DataType.ELSE_KEYWORD,
            DataType.OPEN_BRACE,
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON,
            DataType.CLOSE_BRACE
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(DataType.IF_STATEMENT, root.type)
    }

    @Test
    fun isIfElseMissingElseKeywordTest() {
        var container = Container()
        val sentence = listOf(
            "if", "(", "true", ")", "{", "println", "(", "5", ")", ";", "}"
        )
        val dataTypes = listOf(
            DataType.IF_KEYWORD,
            DataType.OPEN_PARENTHESIS,
            DataType.BOOLEAN_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.OPEN_BRACE,
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON,
            DataType.CLOSE_BRACE
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(DataType.IF_STATEMENT, root.type)
    }

    @Test
    fun isIfElseMissingOpenBraceInElseTest() {
        var container = Container()
        val sentence = listOf(
            "if", "(", "true", ")", "{", "println", "(", "5", ")", ";", "}",
            "else", "println", "(", "10", ")", ";", "}"
        )
        val dataTypes = listOf(
            DataType.IF_KEYWORD,
            DataType.OPEN_PARENTHESIS,
            DataType.BOOLEAN_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.OPEN_BRACE,
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON,
            DataType.CLOSE_BRACE,
            DataType.ELSE_KEYWORD,
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON,
            DataType.CLOSE_BRACE
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(DataType.IF_STATEMENT, root.type)
        assertEquals(2, root.children.size)
    }

    @Test
    fun isIfElseMissingCloseBraceInElseTest() {
        var container = Container()
        val sentence = listOf(
            "if", "(", "true", ")", "{", "println", "(", "5", ")", ";", "}",
            "else", "{", "println", "(", "10", ")", ";"
        )
        val dataTypes = listOf(
            DataType.IF_KEYWORD,
            DataType.OPEN_PARENTHESIS,
            DataType.BOOLEAN_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.OPEN_BRACE,
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON,
            DataType.CLOSE_BRACE,
            DataType.ELSE_KEYWORD,
            DataType.OPEN_BRACE,
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(DataType.IF_STATEMENT, root.type)
        assertEquals(2, root.children.size)
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
        var container = Container()
        val sentence = listOf("5", "*", "3", "+", "2")
        val dataTypes = listOf(
            DataType.NUMBER_LITERAL,
            DataType.MULTIPLICATION,
            DataType.NUMBER_LITERAL,
            DataType.ADDITION,
            DataType.NUMBER_LITERAL
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.arithParse(container)

        assertEquals(DataType.ADDITION, root.type)
        assertEquals(DataType.MULTIPLICATION, root.children[0].type)
    }

    @Test
    fun parseBlockWithMultipleStatementsTest() {
        var container = Container()
        val sentence = listOf(
            "let", "x", ":", "number", "=", "5", ";",
            "println", "(", "x", ")", ";"
        )
        val dataTypes = listOf(
            DataType.LET_KEYWORD,
            DataType.IDENTIFIER,
            DataType.COLON,
            DataType.NUMBER_TYPE,
            DataType.ASSIGNATION,
            DataType.NUMBER_LITERAL,
            DataType.SEMICOLON,
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.IDENTIFIER,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(DataType.DECLARATION, root.type)
    }

    @Test
    fun isIfWithElseKeywordTest() {
        var container = Container()
        val sentence = listOf(
            "if", "(", "true", ")", "{", "println", "(", "5", ")", ";", "}", "else", "{", "}"
        )
        val dataTypes = listOf(
            DataType.IF_KEYWORD,
            DataType.OPEN_PARENTHESIS,
            DataType.BOOLEAN_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.OPEN_BRACE,
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON,
            DataType.CLOSE_BRACE,
            DataType.ELSE_KEYWORD,
            DataType.OPEN_BRACE,
            DataType.CLOSE_BRACE
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(DataType.IF_STATEMENT, root.type)
    }

    @Test
    fun isIfElseMissingCloseBraceInIfTest() {
        var container = Container()
        val sentence = listOf(
            "if", "(", "true", ")", "{", "println", "(", "5", ")", ";",
            "else", "{", "println", "(", "10", ")", ";", "}"
        )
        val dataTypes = listOf(
            DataType.IF_KEYWORD,
            DataType.OPEN_PARENTHESIS,
            DataType.BOOLEAN_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.OPEN_BRACE,
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON,
            DataType.ELSE_KEYWORD,
            DataType.OPEN_BRACE,
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.NUMBER_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON,
            DataType.CLOSE_BRACE
        )
        for (i in sentence.indices) {
            container = container.addContainer(Token(dataTypes[i], sentence[i], Position(0, 0)))
        }
        val parser = Parser(container, "1.1")
        val root: ASTNode = parser.parse()

        assertEquals(DataType.INVALID, root.type)
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

        assertEquals(DataType.NUMBER_LITERAL, result.type)
        assertEquals("5", result.content)
        assertEquals(0, result.children.size)
    }
}
