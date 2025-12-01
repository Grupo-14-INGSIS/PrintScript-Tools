package linter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import linter.src.main.kotlin.Linter
import linter.src.main.kotlin.rules.IdentifierNamingRule
import linter.src.main.kotlin.rules.PrintLnRule
import linter.src.main.kotlin.rules.ReadInputRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

class TCKTest {

    private fun identifierNode(name: String) =
        ASTNode(
            type = DataType.IDENTIFIER,
            content = name,
            position = Position(1, 5),
            children = emptyList()
        )

    private fun getInvalidCamelCaseAst(): ASTNode {
        return ASTNode(
            type = DataType.DECLARATION,
            content = "let",
            position = Position(1, 1),
            children = listOf(
                identifierNode("my_var"),
                ASTNode(
                    type = DataType.STRING_TYPE,
                    content = "string",
                    position = Position(1, 13),
                    children = emptyList()
                )
            )
        )
    }

    private fun getValidCamelCaseAst(): ASTNode {
        return ASTNode(
            type = DataType.DECLARATION,
            content = "let",
            position = Position(1, 1),
            children = listOf(
                identifierNode("myVar"),
                ASTNode(
                    type = DataType.STRING_TYPE,
                    content = "string",
                    position = Position(1, 11),
                    children = emptyList()
                )
            )
        )
    }

    private fun getValidSnakeCaseAst(): ASTNode {
        return getInvalidCamelCaseAst() // The AST is the same, only the rule changes
    }

    private fun getInvalidSnakeCaseAst(): ASTNode {
        return getValidCamelCaseAst() // The AST is the same, only the rule changes
    }

    private fun getPrintlnWithExpressionAst(): ASTNode {
        return ASTNode(
            type = DataType.PRINTLN,
            content = "println",
            position = Position(1, 1),
            children = listOf(
                ASTNode(
                    type = DataType.ADDITION,
                    content = "+",
                    position = Position(1, 11),
                    children = listOf(
                        ASTNode(DataType.IDENTIFIER, "b", Position(1, 13), emptyList()),
                        ASTNode(DataType.IDENTIFIER, "a", Position(1, 9), emptyList())
                    )
                )
            )
        )
    }

    private fun getReadInputWithExpressionAst(): ASTNode {
        return ASTNode(
            type = DataType.READ_INPUT,
            content = "readInput",
            position = Position(1, 1),
            children = listOf(
                ASTNode(
                    type = DataType.ADDITION,
                    content = "+",
                    position = Position(1, 12),
                    children = listOf(
                        ASTNode(DataType.IDENTIFIER, "b", Position(1, 14), emptyList()),
                        ASTNode(DataType.IDENTIFIER, "a", Position(1, 10), emptyList())
                    )
                )
            )
        )
    }

    @Test
    fun `tck test invalid-mandatory-camel-case-identifiers`() {
        val ast = getInvalidCamelCaseAst()
        val rule = IdentifierNamingRule("camelCase")
        val linter = Linter(listOf(rule))
        val errors = linter.all(ast)
        assertTrue(errors.isNotEmpty(), "Expected linting errors, but found none.")
        assertEquals("Identifier 'my_var' does not match camelCase style", errors.first().message)
    }

    @Test
    fun `tck test valid-mandatory-camel-case-identifiers`() {
        val ast = getValidCamelCaseAst()
        val rule = IdentifierNamingRule("camelCase")
        val linter = Linter(listOf(rule))
        val errors = linter.all(ast)
        assertTrue(errors.isEmpty(), "Expected no linting errors, but found ${errors.size}.")
    }

    @Test
    fun `tck test invalid-mandatory-snake-case-identifiers`() {
        val ast = getInvalidSnakeCaseAst()
        val rule = IdentifierNamingRule("snake_case")
        val linter = Linter(listOf(rule))
        val errors = linter.all(ast)
        assertTrue(errors.isNotEmpty(), "Expected linting errors, but found none.")
        assertEquals("Identifier 'myVar' does not match snake_case style", errors.first().message)
    }

    @Test
    fun `tck test valid-mandatory-snake-case-identifiers`() {
        val ast = getValidSnakeCaseAst()
        val rule = IdentifierNamingRule("snake_case")
        val linter = Linter(listOf(rule))
        val errors = linter.all(ast)
        assertTrue(errors.isEmpty(), "Expected no linting errors, but found ${errors.size}.")
    }

    @Test
    fun `tck test invalid-println-with-expression`() {
        val ast = getPrintlnWithExpressionAst()
        val rule = PrintLnRule(true)
        val linter = Linter(listOf(rule))
        val errors = linter.all(ast)
        assertTrue(errors.isNotEmpty(), "Expected linting errors, but found none.")
    }

    @Test
    fun `tck test invalid-read-input-with-expression`() {
        val ast = getReadInputWithExpressionAst()
        val rule = ReadInputRule(true)
        val linter = Linter(listOf(rule))
        val errors = linter.all(ast)
        assertTrue(errors.isNotEmpty(), "Expected linting errors, but found none.")
    }

    @Test
    fun `tck test valid-no-rules`() {
        val ast = getInvalidCamelCaseAst() // Use any AST
        val linter = Linter(emptyList()) // No rules
        val errors = linter.all(ast)
        assertTrue(errors.isEmpty(), "Expected no linting errors when no rules are applied.")
    }
}
