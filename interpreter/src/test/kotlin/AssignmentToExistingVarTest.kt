package test.model.tools.interpreter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import src.main.model.tools.interpreter.interpreter.AssignmentToExistingVar
import common.src.main.kotlin.ASTNode
import common.src.main.kotlin.DataType
import common.src.main.kotlin.Position
import common.src.main.kotlin.Token

class AssignmentToExistingVarTest {

    private fun createNode(variableName: String, value: String): ASTNode {
        val dummyType = DataType.IDENTIFIER
        val dummyPosition = Position(line = 1, column = 1)

        val nameToken = Token(type = dummyType, content = variableName, position = dummyPosition)
        val valueToken = Token(type = dummyType, content = value, position = dummyPosition)
        val rootToken = Token(type = dummyType, content = "assignment", position = dummyPosition)

        val nameNode = ASTNode(token = nameToken, children = emptyList())
        val valueNode = ASTNode(token = valueToken, children = emptyList())

        return ASTNode(token = rootToken, children = listOf(nameNode, valueNode))
    }

    @Test
    fun `assign numeric value to variable`() {
        val node = createNode("x", "42")
        val result = AssignmentToExistingVar.interpret(node)
        assertEquals(42.0, result)
    }

    @Test
    fun `assign string value to variable`() {
        val node = createNode("greeting", "hello")
        val result = AssignmentToExistingVar.interpret(node)
        assertEquals("hello", result)
    }

    @Test
    fun `reassign existing variable`() {
        val first = createNode("y", "10")
        val second = createNode("y", "20")
        AssignmentToExistingVar.interpret(first)
        val result = AssignmentToExistingVar.interpret(second)
        assertEquals(20.0, result)
    }

    @Test
    fun `assign float value to variable`() {
        val node = createNode("pi", "3.14")
        val result = AssignmentToExistingVar.interpret(node)
        assertEquals(3.14, result)
    }

    @Test
    fun `assign non-numeric string that looks like number`() {
        val node = createNode("code", "007")
        val result = AssignmentToExistingVar.interpret(node)
        assertEquals(7.0, result) // if "007" is parsed as Double
    }

    @Test
    fun `assign malformed number string`() {
        val node = createNode("bad", "12abc")
        val result = AssignmentToExistingVar.interpret(node)
        assertEquals("12abc", result)
    }
}
