package linter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
import tokendata.src.main.kotlin.Position
import linter.src.main.kotlin.rules.IdentifierNamingRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class IdentifierNamingRuleTest {

    private fun node(name: String, line: Int = 1, column: Int = 1, children: List<ASTNode> = emptyList()) =
        ASTNode(ASTNodeType.IDENTIFIER, name, Position(line, column), children)

    @Test
    fun `camelCase - valid identifier`() {
        val rule = IdentifierNamingRule("camelCase")
        val root = node("validName")
        val errors = rule.apply(root)
        assertEquals(emptyList<Any>(), errors)
    }

    @Test
    fun `camelCase - invalid identifier`() {
        val rule = IdentifierNamingRule("camelCase")
        val root = node("Invalid_name")
        val errors = rule.apply(root)
        assertEquals(1, errors.size)
        assertEquals("Identifier 'Invalid_name' does not match camelCase style", errors[0].message)
    }

    @Test
    fun `snake_case - valid identifier`() {
        val rule = IdentifierNamingRule("snake_case")
        val root = node("valid_name_123")
        val errors = rule.apply(root)
        assertEquals(emptyList<Any>(), errors)
    }

    @Test
    fun `snake_case - invalid identifier`() {
        val rule = IdentifierNamingRule("snake_case")
        val root = node("invalidName")
        val errors = rule.apply(root)
        assertEquals(1, errors.size)
        assertEquals("Identifier 'invalidName' does not match snake_case style", errors[0].message)
    }

    @Test
    fun `unknown style - reports nothing`() {
        val rule = IdentifierNamingRule("UNKNOWN_STYLE")
        val root = node("anythingGoes_123")
        val errors = rule.apply(root)
        assertEquals(emptyList<Any>(), errors)
    }

    @Test
    fun `recursive traversal identifies multiple violations`() {
        val rule = IdentifierNamingRule("camelCase")
        val child1 = node("bad_one")
        val child2 = node("BadTwo")
        val root = node("validParent", children = listOf(child1, child2))

        val errors = rule.apply(root)
        assertEquals(2, errors.size)
        val msgs = errors.map { it.message }
        assertEquals(
            listOf(
                "Identifier 'bad_one' does not match camelCase style",
                "Identifier 'BadTwo' does not match camelCase style"
            ),
            msgs
        )
    }
}
