package formatter.src.main.kotlin

import formatter.src.main.kotlin.formatrule.FormatRule
import formatter.src.main.kotlin.formatrule.mandatory.SpaceAroundOperatorRule
import formatter.src.main.kotlin.formatrule.mandatory.SpaceBetweenTokensRule
import formatter.src.main.kotlin.formatrule.mandatory.LineBreakAfterSemicolonRule
import formatter.src.main.kotlin.formatrule.mandatory.IfBraceOnSameLineRule
import formatter.src.main.kotlin.formatrule.optional.CharLimitPerLineRule
import formatter.src.main.kotlin.formatrule.optional.ClassNameCamelCaseRule
import formatter.src.main.kotlin.formatrule.optional.NoSpaceBeforeColonRule
import formatter.src.main.kotlin.formatrule.optional.NoSpaceAfterColonRule
import formatter.src.main.kotlin.formatrule.optional.AssignSpacingRule
import formatter.src.main.kotlin.formatrule.optional.LineBreakBeforePrintRule
import formatter.src.main.kotlin.formatrule.optional.IndentationRule
import org.yaml.snakeyaml.Yaml
import java.io.File

class ConfigLoader(
    private val configFile: String
) {

    fun loadConfig(version: String = "1.0"): List<FormatRule> {
        val configurableRules = createConfigurableRules(
            readConfig()
        )
        val mandatoryRules = createMandatoryRules(version)

        return (configurableRules + mandatoryRules).toList()
    }

    internal fun createMandatoryRules(version: String = "1.0"): List<FormatRule> {
        val baseRules = listOf(
            SpaceAroundOperatorRule(),
            SpaceBetweenTokensRule(),
            LineBreakAfterSemicolonRule(),
            IfBraceOnSameLineRule()
        )

        return when (version) {
            "1.0" -> baseRules
            "1.1" -> baseRules + listOf(
                IfBraceOnSameLineRule()
            )
            else -> baseRules
        }
    }

    internal fun createConfigurableRules(rules: Map<String, Any>): List<FormatRule> {
        return buildList {
            for ((ruleName, ruleValue) in rules) {
                val rule = when (ruleName) {
                    "NoSpaceBeforeColon" -> NoSpaceBeforeColonRule()
                    "NoSpaceAfterColon" -> NoSpaceAfterColonRule()
                    "assignSpacing" -> {
                        val config = ruleValue as? Map<*, *>
                        val spaceBefore = config?.get("before") as? Boolean ?: true
                        val spaceAfter = config?.get("after") as? Boolean ?: true
                        AssignSpacingRule(spaceBefore, spaceAfter)
                    }
                    "CharLimitPerLine" -> CharLimitPerLineRule()
                    "ClassNameCamel" -> ClassNameCamelCaseRule()
                    "lineBreakBeforePrint" -> {
                        val count = (ruleValue as? Number)?.toInt() ?: 1
                        LineBreakBeforePrintRule(count)
                    }
                    "indentSize" -> {
                        val size = (ruleValue as? Number)?.toInt() ?: 2
                        IndentationRule(size)
                    }
                    else -> null
                }
                rule?.let {
                    add(it)
                }
            }
        }
    }

    internal fun readConfig(): Map<String, Any> {
        val yaml = Yaml()
        val data = yaml.load<Map<String, Any>>(
            File(configFile).readText()
        )
        val rules = data["rules"] as? Map<String, Any> ?: return emptyMap()

        return buildMap {
            // Reglas de switch (boolean)
            val switchRules = rules["switch"] as? Map<String, Boolean> ?: emptyMap()
            for ((rule, enabled) in switchRules) {
                if (enabled) {
                    put(rule, true)
                }
            }

            // Reglas con valores (number/string/object)
            val valueRules = rules["setValue"] as? Map<String, Any> ?: emptyMap()
            for ((rule, value) in valueRules) {
                put(rule, value)
            }
        }
    }
}
