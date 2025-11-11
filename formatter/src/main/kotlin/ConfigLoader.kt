package formatter.src.main.kotlin

import formatter.src.main.kotlin.formatrule.FormatRule
import formatter.src.main.kotlin.formatrule.mandatory.SpaceAroundOperatorRule
import formatter.src.main.kotlin.formatrule.mandatory.SpaceBetweenTokensRule
import formatter.src.main.kotlin.formatrule.mandatory.LineBreakAfterSemicolonRule
import formatter.src.main.kotlin.formatrule.optional.AssignSpacingRule
import formatter.src.main.kotlin.formatrule.optional.NoSpaceBeforeColonRule
import formatter.src.main.kotlin.formatrule.optional.NoSpaceAfterColonRule
import formatter.src.main.kotlin.formatrule.optional.SpaceBeforeColonRule
import formatter.src.main.kotlin.formatrule.optional.SpaceAfterColonRule
import formatter.src.main.kotlin.formatrule.optional.LineBreakBeforePrintRule
import formatter.src.main.kotlin.formatrule.optional.IndentationRule
import formatter.src.main.kotlin.formatrule.optional.IfBraceOnSameLineRule
import formatter.src.main.kotlin.formatrule.optional.IfBraceBelowLineRule
import formatter.src.main.kotlin.formatrule.optional.LineBreakAfterPrintRule
import org.yaml.snakeyaml.Yaml
import java.io.File

class ConfigLoader(private val configFile: String) {

    fun loadConfig(version: String = "1.0"): List<FormatRule> {
        val config = readConfig()
        val mandatoryRules = extractMandatoryRulesFromConfig(config)
        val configurableRules = createConfigurableRules(config)
        return (mandatoryRules + configurableRules).toList()
    }

    internal fun extractMandatoryRulesFromConfig(config: Map<String, Any>): List<FormatRule> {
        return buildList {
            if (config["mandatory-single-space-separation"] == true) {
                add(SpaceBetweenTokensRule(enabled = true))
            }
            if (config["mandatory-space-surrounding-operations"] == true) {
                add(SpaceAroundOperatorRule())
            }
            if (config["mandatory-line-break-after-statement"] == true) {
                add(LineBreakAfterSemicolonRule(enabled = true))
            }
        }
    }

    internal fun createConfigurableRules(config: Map<String, Any>): List<FormatRule> {
        return buildList {
            config.forEach { (ruleName, ruleValue) ->
                val rule = when (ruleName) {
                    "enforce-spacing-around-equals", "assign-spacing-surrounding-equals" -> {
                        if (ruleValue as Boolean) AssignSpacingRule(true, true) else null
                    }
                    "enforce-no-spacing-around-equals", "assign-no-spacing-surrounding-equals" -> {
                        if (ruleValue as Boolean) AssignSpacingRule(false, false) else null
                    }
                    "enforce-spacing-before-colon-in-declaration" -> {
                        if (ruleValue as Boolean) SpaceBeforeColonRule() else NoSpaceBeforeColonRule()
                    }
                    "enforce-spacing-after-colon-in-declaration" -> {
                        if (ruleValue as Boolean) SpaceAfterColonRule() else NoSpaceAfterColonRule()
                    }
                    "line-breaks-after-println" -> {
                        val count = (ruleValue as? Number)?.toInt() ?: 0
                        LineBreakAfterPrintRule(count)
                    }
                    "line-breaks-before-println" -> {
                        val count = (ruleValue as? Number)?.toInt() ?: 0
                        LineBreakBeforePrintRule(count)
                    }
                    "indent-inside-if" -> {
                        val size = (ruleValue as? Number)?.toInt() ?: 2
                        IndentationRule(size)
                    }
                    "if-brace-same-line" -> {
                        if (ruleValue as Boolean) IfBraceOnSameLineRule() else null
                    }
                    "if-brace-below-line" -> {
                        if (ruleValue as Boolean) IfBraceBelowLineRule() else null
                    }
                    "mandatory-single-space-separation",
                    "mandatory-space-surrounding-operations",
                    "mandatory-line-break-after-statement" -> null
                    else -> null
                }
                rule?.let { add(it) }
            }
        }
    }

    internal fun readConfig(): Map<String, Any> {
        val yaml = Yaml()
        val data = yaml.load<Map<String, Any>>(File(configFile).readText())
        return data
    }
}
