package formatter.src.main.kotlin

import formatter.src.main.kotlin.formatrule.FormatRule
import formatter.src.main.kotlin.formatrule.mandatory.SpaceAroundOperatorRule
import formatter.src.main.kotlin.formatrule.mandatory.SpaceBetweenTokensRule
import formatter.src.main.kotlin.formatrule.mandatory.LineBreakAfterSemicolonRule
import formatter.src.main.kotlin.formatrule.optional.AssignSpacingRule
import formatter.src.main.kotlin.formatrule.optional.NoSpaceBeforeColonRule
import formatter.src.main.kotlin.formatrule.optional.NoSpaceAfterColonRule
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
        // Reglas que SIEMPRE se aplican, no se configuran
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

                    // CON espacios: x = 5
                    "SpaceBeforeEquals", "SpaceAfterEquals" -> {
                        if (ruleValue as Boolean) {
                            AssignSpacingRule(spaceBefore = true, spaceAfter = true)
                        } else {
                            null
                        }
                    }

                    // SIN espacios: x=5
                    "NoSpaceBeforeEquals", "NoSpaceAfterEquals" -> {
                        if (ruleValue as Boolean) {
                            AssignSpacingRule(spaceBefore = false, spaceAfter = false)
                        } else {
                            null
                        }
                    }

                    // ========== ESPACIADO ALREDEDOR DE ":" ==========

                    // SIN espacio ANTES de ":"
                    "NoSpaceBeforeColon" -> {
                        if (ruleValue as Boolean) {
                            NoSpaceBeforeColonRule()
                        } else {
                            null
                        }
                    }

                    // SIN espacio DESPUÉS de ":"
                    "NoSpaceAfterColon" -> {
                        if (ruleValue as Boolean) {
                            NoSpaceAfterColonRule()
                        } else {
                            null
                        }
                    }

                    // ========== SALTOS DE LÍNEA ANTES DE PRINTLN ==========

                    "lineBreakBeforePrint" -> {
                        val count = (ruleValue as? Number)?.toInt() ?: 1
                        LineBreakBeforePrintRule(count)
                    }

                    // ========== INDENTACIÓN ==========

                    "indentSize" -> {
                        val size = (ruleValue as? Number)?.toInt() ?: 2
                        IndentationRule(size)
                    }

                    // ========== LLAVE DEL IF ==========

                    "ifBraceSameLine" -> {
                        if (ruleValue as Boolean) {
                            IfBraceOnSameLineRule()
                        } else {
                            IfBraceBelowLineRule()
                        }
                    }

                    // Ignorar reglas que ya están en mandatory
                    "SpaceBetweenTokens", "SpaceAroundOperator", "LineBreakAfterSemicolon" -> null

                    else -> null
                }

                rule?.let { add(it) }
            }
        }
    }

    internal fun readConfig(): Map<String, Any> {
        val yaml = Yaml()
        val data = yaml.load<Map<String, Any>>(File(configFile).readText())
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
