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
        val mult = root.children[0]

        assertEquals(DataType.ADDITION, root.type)
        assertEquals(DataType.MULTIPLICATION, mult.type)
        assertEquals(DataType.NUMBER_LITERAL, mult.children[0].type)
        assertEquals(DataType.NUMBER_LITERAL, mult.children[1].type)
        assertEquals(DataType.NUMBER_LITERAL, root.children[1].type)
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

        val sum = root.children[1]
        val sub = sum.children[1]
        val div = sum.children[0]
        val mul = div.children[1]

        assertEquals(DataType.ADDITION, root.type)
        assertEquals(DataType.ADDITION, sum.type)
        assertEquals(DataType.NUMBER_LITERAL, root.children[0].type)
        assertEquals(DataType.SUBTRACTION, sub.type)
        assertEquals(DataType.DIVISION, div.type)
        assertEquals(DataType.NUMBER_LITERAL, sub.children[1].type)
        assertEquals(DataType.NUMBER_LITERAL, sub.children[0].type)
        assertEquals(DataType.MULTIPLICATION, mul.type)
        assertEquals(DataType.NUMBER_LITERAL, div.children[0].type)
        assertEquals(DataType.NUMBER_LITERAL, mul.children[1].type)
        assertEquals(DataType.NUMBER_LITERAL, mul.children[0].type)
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
}
