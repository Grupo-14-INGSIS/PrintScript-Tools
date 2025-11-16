import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import linter.src.main.kotlin.rules.IdentifierNamingRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class IdentifierNamingRuleTest {


    private fun node(name: String, line: Int = 1, column: Int = 1, children: List<ASTNode> = emptyList()) =
        ASTNode(DataType.IDENTIFIER, name, Position(line, column), children)


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
        val root = node("InvalidName")
        val errors = rule.apply(root)
        assertEquals(1, errors.size)
        assertEquals("Identifier 'InvalidName' does not match snake_case style", errors[0].message)
    }


    @Test
    fun `default style - accepts anything`() {
        val rule = IdentifierNamingRule("unknownStyle")
        val root = node("ANYTHING_GOES")
        val errors = rule.apply(root)
        assertEquals(emptyList<Any>(), errors)
    }


    @Test
    fun `nested identifiers - all validated`() {
        val rule = IdentifierNamingRule("camelCase")
        val child1 = node("validChild")
        val child2 = node("Invalid_Child")
        val root = node("validRoot", children = listOf(child1, child2))
        val errors = rule.apply(root)
        assertEquals(1, errors.size)
        assertEquals("Identifier 'Invalid_Child' does not match camelCase style", errors[0].message)
    }
}
