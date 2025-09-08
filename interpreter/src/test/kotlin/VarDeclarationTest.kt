package test.model.tools.interpreter

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import src.main.model.tools.interpreter.interpreter.VarDeclaration
import ast.src.main.kotlin.ASTNode
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

class VarDeclarationTest {

    private val dummyPosition = Position(line = 1, column = 1)
    private val dummyType = DataType.IDENTIFIER

    private fun token(content: String) = Token(type = dummyType, content = content, position = dummyPosition)
    private fun node(content: String) = ASTNode(type = dummyType, content = content, position = dummyPosition, children = emptyList())

    private fun declarationNode(name: String, type: String): ASTNode {
        val nameNode = node(name)
        val typeNode = node(type)
        val rootToken = token("var")
        return ASTNode(type = dummyType, content = "var", position = dummyPosition, children = listOf(nameNode, typeNode))
    }

    @Test
    fun `declares number variable with default value`() {
        val node = declarationNode("x", "number")
        val result = VarDeclaration.interpret(node)
        assertEquals(Unit, result)
    }

    @Test
    fun `declares string variable with default value`() {
        val node = declarationNode("msg", "string")
        val result = VarDeclaration.interpret(node)
        assertEquals(Unit, result)
    }

    @Test
    fun `declares boolean variable with default value`() {
        val node = declarationNode("flag", "boolean")
        val result = VarDeclaration.interpret(node)
        assertEquals(Unit, result)
    }

    @Test
    fun `throws error on redeclaration`() {
        val node = declarationNode("dup", "number")
        VarDeclaration.interpret(node)
        val exception = assertThrows(IllegalStateException::class.java) {
            VarDeclaration.interpret(node)
        }
        assertTrue(exception.message!!.contains("ya fue declarada"))
    }

    @Test
    fun `throws error on missing arguments`() {
        val incompleteNode = ASTNode(type = dummyType, content = "var", position = dummyPosition, children = listOf(node("x")))
        val exception = assertThrows(IllegalArgumentException::class.java) {
            VarDeclaration.interpret(incompleteNode)
        }
        assertTrue(exception.message!!.contains("faltan argumentos"))
    }

    @Test
    fun `declares variable with unknown type and null default`() {
        val node = declarationNode("custom", "object")
        val result = VarDeclaration.interpret(node)
        assertEquals(Unit, result)
    }
}
