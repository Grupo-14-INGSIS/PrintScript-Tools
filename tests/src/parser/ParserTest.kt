import model.structure.Container
import model.structure.DataType
import model.structure.Token
import model.structure.ASTNode
import model.tools.interpreter.parser.Parser
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
}