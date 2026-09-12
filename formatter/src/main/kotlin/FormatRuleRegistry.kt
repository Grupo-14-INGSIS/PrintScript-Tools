package formatter.src.main.kotlin

import formatter.src.main.kotlin.formatrule.FormatRule
import formatter.src.main.kotlin.formatrule.mandatory.LineBreakAfterSemicolonRule
import formatter.src.main.kotlin.formatrule.mandatory.SpaceAroundOperatorRule
import formatter.src.main.kotlin.formatrule.mandatory.SpaceBetweenTokensRule
import formatter.src.main.kotlin.formatrule.optional.AssignSpacingRule
import formatter.src.main.kotlin.formatrule.optional.IfBraceBelowLineRule
import formatter.src.main.kotlin.formatrule.optional.IfBraceOnSameLineRule
import formatter.src.main.kotlin.formatrule.optional.IndentationRule
import formatter.src.main.kotlin.formatrule.optional.LineBreakAfterPrintRule
import formatter.src.main.kotlin.formatrule.optional.LineBreakBeforePrintRule
import formatter.src.main.kotlin.formatrule.optional.NoSpaceAfterColonRule
import formatter.src.main.kotlin.formatrule.optional.NoSpaceBeforeColonRule
import formatter.src.main.kotlin.formatrule.optional.SpaceAfterColonRule
import formatter.src.main.kotlin.formatrule.optional.SpaceBeforeColonRule

/**
 * Registro desacoplado de factorias de reglas de formateo.
 * Permite registrar y resolver reglas dinamicamente segun la configuracion,
 * cumpliendo con el principio Open/Closed (OCP).
 */
object FormatRuleRegistry {

    private val configurableFactories = mutableMapOf<String, FormatRuleFactory>()
    private val mandatoryFactories = mutableMapOf<String, FormatRuleFactory>()

    init {
        registerDefaults()
    }

    private fun registerDefaults() {
        registerMandatory("mandatory-single-space-separation") { value ->
            if (value == true) SpaceBetweenTokensRule(enabled = true) else null
        }
        registerMandatory("mandatory-space-surrounding-operations") { value ->
            if (value == true) SpaceAroundOperatorRule() else null
        }
        registerMandatory("mandatory-line-break-after-statement") { value ->
            if (value == true) LineBreakAfterSemicolonRule(enabled = true) else null
        }

        registerConfigurable("enforce-spacing-around-equals") { value ->
            if (value as? Boolean == true) AssignSpacingRule(true, true) else null
        }
        registerConfigurable("assign-spacing-surrounding-equals") { value ->
            if (value as? Boolean == true) AssignSpacingRule(true, true) else null
        }
        registerConfigurable("enforce-no-spacing-around-equals") { value ->
            if (value as? Boolean == true) AssignSpacingRule(false, false) else null
        }
        registerConfigurable("assign-no-spacing-surrounding-equals") { value ->
            if (value as? Boolean == true) AssignSpacingRule(false, false) else null
        }
        registerConfigurable("enforce-spacing-before-colon-in-declaration") { value ->
            if (value as? Boolean == true) SpaceBeforeColonRule() else NoSpaceBeforeColonRule()
        }
        registerConfigurable("enforce-spacing-after-colon-in-declaration") { value ->
            if (value as? Boolean == true) SpaceAfterColonRule() else NoSpaceAfterColonRule()
        }
        registerConfigurable("line-breaks-after-println") { value ->
            val count = (value as? Number)?.toInt() ?: 0
            LineBreakAfterPrintRule(count)
        }
        registerConfigurable("line-breaks-before-println") { value ->
            val count = (value as? Number)?.toInt() ?: 0
            LineBreakBeforePrintRule(count)
        }
        registerConfigurable("indent-inside-if") { value ->
            val size = (value as? Number)?.toInt() ?: 2
            IndentationRule(size)
        }
        registerConfigurable("if-brace-same-line") { value ->
            if (value as? Boolean == true) IfBraceOnSameLineRule() else null
        }
        registerConfigurable("if-brace-below-line") { value ->
            if (value as? Boolean == true) IfBraceBelowLineRule() else null
        }
    }

    fun registerConfigurable(ruleName: String, factory: FormatRuleFactory) {
        configurableFactories[ruleName] = factory
    }

    fun registerMandatory(ruleName: String, factory: FormatRuleFactory) {
        mandatoryFactories[ruleName] = factory
    }

    fun createConfigurable(ruleName: String, value: Any): FormatRule? {
        return configurableFactories[ruleName]?.create(value)
    }

    fun createMandatory(ruleName: String, value: Any): FormatRule? {
        return mandatoryFactories[ruleName]?.create(value)
    }
}
