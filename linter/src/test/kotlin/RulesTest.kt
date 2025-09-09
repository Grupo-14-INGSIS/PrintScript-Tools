package linter.src.test.kotlin

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.Position
import tokendata.src.main.kotlin.DataType
import linter.src.main.kotlin.rules.*

class RulesTest {

    @Test
    fun `valid println with identifier passes`() {
        val node = ASTNode(
            DataType.PRINTLN,
            "println",
            Position(1, 1),
            listOf(
                ASTNode(DataType.IDENTIFIER, "x", Position(1, 9), emptyList())
            )
        )

        val rule = PrintLnRule()
        val errors = rule.apply(node)
        assertTrue(errors.isEmpty())
    }

    @Test
    fun `valid println with literal passes`() {
        val node = ASTNode(
            DataType.PRINTLN,
            "println",
            Position(2, 1),
            listOf(
                ASTNode(DataType.STRING_LITERAL, "\"hello\"", Position(2, 9), emptyList())
            )
        )

        val rule = PrintLnRule()
        val errors = rule.apply(node)
        assertTrue(errors.isEmpty())
    }

    @Test
    fun `invalid println with unsupported type fails`() {
        val node = ASTNode(
            DataType.PRINTLN,
            "println",
            Position(3, 1),
            listOf(
                ASTNode(DataType.ASSIGNATION, "=", Position(3, 9), emptyList())
            )
        )

        val rule = PrintLnRule()
        val errors = rule.apply(node)
        assertEquals(1, errors.size)
        assertEquals("println argument must be a literal or identifier", errors[0].message)
        assertEquals(Position(3, 1), errors[0].position)
    }

    @Test
    fun `disabled rule returns no errors`() {
        val node = ASTNode(
            DataType.PRINTLN,
            "println",
            Position(4, 1),
            listOf(
                ASTNode(DataType.ASSIGNATION, "=", Position(4, 9), emptyList())
            )
        )

        val rule = PrintLnRule(enabled = false)
        val errors = rule.apply(node)
        assertTrue(errors.isEmpty())
    }


    @Test
    fun `valid camelCase identifier passes`() {
        val node = ASTNode(DataType.IDENTIFIER, "myVariable", Position(1, 1), emptyList())

        val rule = IdentifierNamingRule("camelCase")
        val errors = rule.apply(node)
        assertTrue(errors.isEmpty())
    }

    @Test
    fun `invalid camelCase identifier fails`() {
        val node = ASTNode(
            DataType.IDENTIFIER,
            "My_variable",
            Position(2, 1),
            emptyList()
        )

        val rule = IdentifierNamingRule("camelCase")
        val errors = rule.apply(node)
        assertEquals(1, errors.size)
        assertEquals("Identifier 'My_variable' does not match camelCase style", errors[0].message)
        assertEquals(Position(2, 1), errors[0].position)
    }

    @Test
    fun `valid snake_case identifier passes`() {
        val node = ASTNode(DataType.IDENTIFIER, "my_variable_1", Position(3, 1), emptyList())

        val rule = IdentifierNamingRule("snake_case")
        val errors = rule.apply(node)
        assertTrue(errors.isEmpty())
    }

    @Test
    fun `invalid snake_case identifier fails`() {
        val node = ASTNode(DataType.IDENTIFIER, "myVariable", Position(4, 1), emptyList())

        val rule = IdentifierNamingRule("snake_case")
        val errors = rule.apply(node)
        assertEquals(1, errors.size)
        assertEquals("Identifier 'myVariable' does not match snake_case style", errors[0].message)
        assertEquals(Position(4, 1), errors[0].position)
    }

    @Test
    fun `unknown style accepts all identifiers`() {
        val node = ASTNode(DataType.IDENTIFIER, "ANY_STYLE", Position(5, 1), emptyList())

        val rule = IdentifierNamingRule("PascalCase")
        val errors = rule.apply(node)
        assertTrue(errors.isEmpty())
    }
}
