package linter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import linter.src.main.kotlin.LintError
import linter.src.main.kotlin.LintRule
import linter.src.main.kotlin.LintRuleRegistry
import linter.src.main.kotlin.rules.IdentifierNamingRule
import linter.src.main.kotlin.rules.PrintLnRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LintRuleRegistryTest {

    @Test
    fun `LintRuleRegistry creates rules from configuration map`() {
        val config = mapOf(
            "rules" to mapOf(
                "identifier_format" to mapOf("style" to "snake_case"),
                "mandatory_variable_or_literal_in_println" to mapOf("enabled" to true),
                "mandatory_variable_or_literal_in_readInput" to mapOf("enabled" to true),
                "if_without_else" to mapOf("enabled" to true)
            )
        )

        val rules = LintRuleRegistry.createRules(config)
        assertEquals(4, rules.size)
        assertTrue(rules.any { it is IdentifierNamingRule })
        assertTrue(rules.any { it is PrintLnRule })
    }

    @Test
    fun `LintRuleRegistry handles disabled rules and unknown rules`() {
        val config = mapOf(
            "rules" to mapOf(
                "mandatory-variable-or-literal-in-println" to mapOf("enabled" to false),
                "unknown_rule" to mapOf("enabled" to true)
            )
        )

        val rules = LintRuleRegistry.createRules(config)
        assertEquals(0, rules.size)
    }

    @Test
    fun `LintRuleRegistry allows custom rule registration`() {
        val dummyRule = object : LintRule {
            override fun apply(root: ASTNode): List<LintError> = emptyList()
        }
        LintRuleRegistry.register("custom_lint_rule") { dummyRule }

        val config = mapOf("custom_lint_rule" to mapOf("enabled" to true))
        val rules = LintRuleRegistry.createRules(config)
        assertEquals(1, rules.size)
    }
}
