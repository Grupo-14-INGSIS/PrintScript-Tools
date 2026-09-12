package formatter.src.test.kotlin

import formatter.src.main.kotlin.FormatRuleRegistry
import formatter.src.main.kotlin.formatrule.FormatRule
import formatter.src.main.kotlin.formatrule.optional.AssignSpacingRule
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FormatRuleRegistryTest {

    @Test
    fun `FormatRuleRegistry creates default mandatory and configurable rules`() {
        val mandatoryRule = FormatRuleRegistry.createMandatory("mandatory-single-space-separation", true)
        assertNotNull(mandatoryRule)

        val configurableRule = FormatRuleRegistry.createConfigurable("enforce-spacing-around-equals", true)
        assertNotNull(configurableRule)
        assertTrue(configurableRule is AssignSpacingRule)
    }

    @Test
    fun `FormatRuleRegistry returns null for unknown or disabled rules`() {
        assertNull(FormatRuleRegistry.createMandatory("unknown-rule", true))
        assertNull(FormatRuleRegistry.createConfigurable("unknown-rule", true))
        assertNull(FormatRuleRegistry.createMandatory("mandatory-single-space-separation", false))
    }

    @Test
    fun `FormatRuleRegistry allows registering new custom rule`() {
        val dummyRule = object : FormatRule {
            override fun format(statements: List<container.src.main.kotlin.Container>) = statements
        }
        FormatRuleRegistry.registerConfigurable("custom-rule") { dummyRule }

        val created = FormatRuleRegistry.createConfigurable("custom-rule", true)
        assertNotNull(created)
    }
}
