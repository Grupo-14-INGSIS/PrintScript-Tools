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
import org.yaml.snakeyaml.Yaml
import java.io.File

class ConfigLoader(private val configFile: String) {

    fun loadConfig(version: String = "1.0"): List<FormatRule> {
        val configurableRules = createConfigurableRules(readConfig())
        val mandatoryRules = createMandatoryRules(version)

        return (mandatoryRules + configurableRules).toList()
    }

    internal fun createMandatoryRules(version: String = "1.0"): List<FormatRule> {
        return listOf(
            SpaceAroundOperatorRule(),
            SpaceBetweenTokensRule(),
            LineBreakAfterSemicolonRule()
        )
    }

    internal fun createConfigurableRules(config: Map<String, Any>): List<FormatRule> {
        return buildList {
            config.forEach { (ruleName, ruleValue) ->
                val rule = when (ruleName) {
                    // ========== ESPACIADO ALREDEDOR DE "=" ==========
                    "enforce-spacing-around-equals", "assign-spacing-surrounding-equals" -> {
                        if (ruleValue as Boolean) {
                            AssignSpacingRule(spaceBefore = true, spaceAfter = true)
                        } else {
                            null
                        }
                    }

                    "enforce-no-spacing-around-equals", "assign-no-spacing-surrounding-equals" -> {
                        if (ruleValue as Boolean) {
                            AssignSpacingRule(spaceBefore = false, spaceAfter = false)
                        } else {
                            null
                        }
                    }

                    // ========== ESPACIADO ALREDEDOR DE ":" ==========
                    "enforce-spacing-before-colon-in-declaration" -> {
                        if (ruleValue as Boolean) {
                            SpaceBeforeColonRule()
                        } else {
                            NoSpaceBeforeColonRule()
                        }
                    }

                    "enforce-spacing-after-colon-in-declaration" -> {
                        if (ruleValue as Boolean) {
                            SpaceAfterColonRule()
                        } else {
                            NoSpaceAfterColonRule()
                        }
                    }

                    // ========== SALTOS DE LÍNEA ANTES DE PRINTLN ==========
                    "line-breaks-after-println" -> {
                        val count = (ruleValue as? Number)?.toInt() ?: 0
                        if (count > 0) {
                            LineBreakBeforePrintRule(count)
                        } else {
                            null
                        }
                    }

                    // ========== INDENTACIÓN ==========
                    "indent-inside-if" -> {
                        val size = (ruleValue as? Number)?.toInt() ?: 2
                        IndentationRule(size)
                    }

                    // ========== LLAVE DEL IF ==========
                    "if-brace-same-line" -> {
                        if (ruleValue as Boolean) {
                            IfBraceOnSameLineRule()
                        } else {
                            null
                        }
                    }

                    "if-brace-below-line" -> {
                        if (ruleValue as Boolean) {
                            IfBraceBelowLineRule()
                        } else {
                            null
                        }
                    }

                    // Ignorar reglas mandatory que vienen en el config
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

        // Los tests pasan el JSON directamente como primer nivel
        return data
    }
}
