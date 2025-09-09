package interpreter.src.test.kotlin

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import interpreter.src.main.kotlin.VarDeclarationAndAssignment
import ast.src.main.kotlin.ASTNode
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

class VarDeclarationAndAssignmentTest {

    private val dummyType = DataType.IDENTIFIER
    private val dummyPosition = Position(line = 1, column = 1)

    private fun token(content: String) = Token(type = dummyType, content = content, position = dummyPosition)
    private fun node(content: String) = ASTNode(type = dummyType, content = content, position = dummyPosition, children = emptyList())

    private fun declarationNode(name: String, type: String, value: String): ASTNode {
        val nameNode = node(name)
        val typeNode = node(type)
        val valueNode = node(value)
        val rootToken = token("=")
        return ASTNode(type = dummyType, content = "=", position = dummyPosition, children = listOf(nameNode, typeNode, valueNode))
    }

    @Test
    fun `declares number variable with assigned value`() {
        val node = declarationNode("x", "number", "42")
        val result = VarDeclarationAndAssignment.interpret(node)
        assertEquals(Unit, result)
    }

    @Test
    fun `declares string variable with assigned value`() {
        val node = declarationNode("msg", "string", "hello")
        val result = VarDeclarationAndAssignment.interpret(node)
        assertEquals(Unit, result)
    }

    @Test
    fun `declares boolean variable with assigned value`() {
        val node = declarationNode("flag", "boolean", "true")
        val result = VarDeclarationAndAssignment.interpret(node)
        assertEquals(Unit, result)
    }

    @Test
    fun `throws error on redeclaration`() {
        val node = declarationNode("dup", "number", "5")
        VarDeclarationAndAssignment.interpret(node)
        val exception = assertThrows(IllegalStateException::class.java) {
            VarDeclarationAndAssignment.interpret(node)
        }
        assertTrue(exception.message!!.contains("ya fue declarada"))
    }

    @Test
    fun `throws error on incompatible value type`() {
        val node = declarationNode("bad", "number", "abc")
        val exception = assertThrows(IllegalArgumentException::class.java) {
            VarDeclarationAndAssignment.interpret(node)
        }
        assertTrue(exception.message!!.contains("no es compatible"))
    }

    @Test
    fun `throws error on missing arguments`() {
        val incompleteNode = ASTNode(
            type = dummyType,
            content = "=",
            position = dummyPosition,
            children = listOf(node("x"), node("number"))
        )
        val exception = assertThrows(IllegalArgumentException::class.java) {
            VarDeclarationAndAssignment.interpret(incompleteNode)
        }
        assertTrue(exception.message!!.contains("faltan argumentos"))
    }

    @Test
    fun `declares variable with unknown type and stores raw value`() {
        val node = declarationNode("custom", "object", "raw")
        val result = VarDeclarationAndAssignment.interpret(node)
        assertEquals(Unit, result)
    }
}
