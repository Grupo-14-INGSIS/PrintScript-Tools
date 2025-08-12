package src.test.model.tools.interpreter.parser

import src.main.model.structure.Container
import src.main.model.structure.DataType
import src.main.model.structure.Token
import src.main.model.structure.ASTNode
import src.main.model.tools.interpreter.parser.Parser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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
        for (i in 0 until sentence.size) {
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
        for (i in 0 until sentence.size) {
            container.addContainer(Token(dataTypes[i], sentence[i], 0))
        }
        val parser = Parser(container)
        val root: ASTNode = parser.parse()

        assertEquals(DataType.PRINTLN, root.token.type)
        assertEquals(DataType.STRING_LITERAL, root.children[0].token.type)
    }
}