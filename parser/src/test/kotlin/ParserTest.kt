package parser.src.test.kotlin

import common.src.main.kotlin.ASTNode
import common.src.main.kotlin.Container
import common.src.main.kotlin.DataType
import common.src.main.kotlin.Token
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.src.main.kotlin.Parser

class ParserTest {

    @Test
    fun basicAssignationTest() {
        val container = Container()
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
            container.addContainer(Token(dataTypes[i], sentence[i], 0))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()
        val declaration: ASTNode = root.children[0]

        assertEquals(DataType.ASSIGNATION, root.token.type)
        assertEquals(DataType.DECLARATION, declaration.token.type)
        assertEquals(DataType.NUMBER_LITERAL, root.children[1].token.type)
        assertEquals(DataType.IDENTIFIER, declaration.children[0].token.type)
        assertEquals(DataType.NUMBER_TYPE, declaration.children[1].token.type)
    }

    @Test
    fun basicPrintTest() {
        val container = Container()
        val sentence = listOf("println", "(", "hi", ")", ";")
        val dataTypes = listOf(
            DataType.PRINTLN,
            DataType.OPEN_PARENTHESIS,
            DataType.STRING_LITERAL,
            DataType.CLOSE_PARENTHESIS,
            DataType.SEMICOLON
        )
        for (i in sentence.indices) {
            container.addContainer(Token(dataTypes[i], sentence[i], 0))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.PRINTLN, root.token.type)
        assertEquals(DataType.STRING_LITERAL, root.children[0].token.type)
    }

    @Test
    fun basicArithmeticTest() {
        val container = Container()
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
            container.addContainer(Token(dataTypes[i], sentence[i], 0))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()
        val mult = root.children[1]

        assertEquals(DataType.ADDITION, root.token.type)
        assertEquals(DataType.NUMBER_LITERAL, root.children[0].token.type)
        assertEquals(DataType.MULTIPLICATION, mult.token.type)
        assertEquals(DataType.NUMBER_LITERAL, mult.children[0].token.type)
        assertEquals(DataType.NUMBER_LITERAL, mult.children[1].token.type)
    }

    @Test
    fun basicArithmeticTest2() {
        val container = Container()
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
            container.addContainer(Token(dataTypes[i], sentence[i], 0))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()
        /*
                          +
                 +                 4
            -         /
        10     8   *     2
                  4 3
         */

        val sum = root.children[0]
        val sub = sum.children[0]
        val div = sum.children[1]
        val mul = div.children[0]

        assertEquals(DataType.ADDITION, root.token.type)
        assertEquals(DataType.ADDITION, sum.token.type)
        assertEquals(DataType.NUMBER_LITERAL, root.children[1].token.type)
        assertEquals(DataType.SUBTRACTION, sub.token.type)
        assertEquals(DataType.DIVISION, div.token.type)
        assertEquals(DataType.NUMBER_LITERAL, sub.children[0].token.type)
        assertEquals(DataType.NUMBER_LITERAL, sub.children[1].token.type)
        assertEquals(DataType.MULTIPLICATION, mul.token.type)
        assertEquals(DataType.NUMBER_LITERAL, div.children[1].token.type)
        assertEquals(DataType.NUMBER_LITERAL, mul.children[0].token.type)
        assertEquals(DataType.NUMBER_LITERAL, mul.children[1].token.type)
    }
}
