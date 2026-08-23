package ast.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
import ast.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ASTTest {

    @Test
    fun `test Position data class properties and methods`() {
        val pos1 = Position(1, 5)
        val pos2 = Position(1, 5)
        val pos3 = Position(2, 10)

        assertEquals(1, pos1.line)
        assertEquals(5, pos1.column)
        assertEquals(pos1, pos2)
        assertEquals(pos1.hashCode(), pos2.hashCode())
        assertTrue(pos1 != pos3)
        assertTrue(pos1.toString().contains("1") && pos1.toString().contains("5"))

        val posCopy = pos1.copy(column = 20)
        assertEquals(1, posCopy.line)
        assertEquals(20, posCopy.column)

        val (line, col) = pos1
        assertEquals(1, line)
        assertEquals(5, col)
    }

    @Test
    fun `test ASTNode creation and properties`() {
        val pos = Position(2, 4)
        val child1 = ASTNode(ASTNodeType.IDENTIFIER, "x", Position(2, 4), emptyList())
        val child2 = ASTNode(ASTNodeType.NUMBER_LITERAL, "10", Position(2, 8), emptyList())
        val parent = ASTNode(ASTNodeType.DECLARATION, "=", pos, listOf(child1, child2))

        assertEquals(ASTNodeType.DECLARATION, parent.type)
        assertEquals("=", parent.content)
        assertEquals(pos, parent.position)
        assertEquals(2, parent.children.size)
        assertEquals(child1, parent.children[0])
        assertEquals(child2, parent.children[1])
    }

    @Test
    fun `test ASTNode toString without children`() {
        val node = ASTNode(ASTNodeType.NUMBER_LITERAL, "42", Position(1, 1), emptyList())
        val str = node.toString()
        assertTrue(str.contains("ASTNode(type=NUMBER_LITERAL, content='42')"))
    }

    @Test
    fun `test ASTNode toString with nested children`() {
        val leaf = ASTNode(ASTNodeType.STRING_LITERAL, "hello", Position(1, 5), emptyList())
        val root = ASTNode(ASTNodeType.PRINTLN, "println", Position(1, 1), listOf(leaf))
        val str = root.toString()
        assertTrue(str.contains("println"))
        assertTrue(str.contains("children=["))
        assertTrue(str.contains("hello"))
    }

    @Test
    fun `test all ASTNodeType enum values`() {
        for (type in ASTNodeType.values()) {
            assertNotNull(type.name)
            assertEquals(type, ASTNodeType.valueOf(type.name))
        }
    }
}
