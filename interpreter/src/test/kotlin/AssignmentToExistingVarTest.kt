package interpreter.src.test.kotlin

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import interpreter.src.main.kotlin.AssignmentToExistingVar
import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

class AssignmentToExistingVarTest {

    private fun createNode(variableName: String, value: String): ASTNode {
        val dummyType = DataType.IDENTIFIER
        val dummyPosition = Position(line = 1, column = 1)

        val nameNode = ASTNode(type = dummyType, content = variableName, position = dummyPosition, children = emptyList())
        val valueNode = ASTNode(type = dummyType, content = value, position = dummyPosition, children = emptyList())

        return ASTNode(type = dummyType, content = "assignment", position = dummyPosition, children = listOf(nameNode, valueNode))
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
