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
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

class ConfigLoader(private val configFile: String) {

    private val mapper = jacksonObjectMapper()

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
                    // Espaciado alrededor de "=" - CON espacios
                    "assign-spacing-surrounding-equals" -> {
                        if (ruleValue as Boolean) {
                            AssignSpacingRule(spaceBefore = true, spaceAfter = true)
                        } else {
                            null
                        }
                    }

                    // Espaciado alrededor de "=" - SIN espacios
                    "enforce-no-spacing-around-equals" -> {
                        if (ruleValue as Boolean) {
                            AssignSpacingRule(spaceBefore = false, spaceAfter = false)
                        } else {
                            null
                        }
                    }

                    // Espacio ANTES de ":"
                    "enforce-decl-spacing-before-colon" -> {
                        if (ruleValue as Boolean) {
                            // true = permitir espacios = NO agregar regla que los elimina
                            null
                        } else {
                            // false = NO permitir espacios = agregar regla que los elimina
                            NoSpaceBeforeColonRule()
                        }
                    }

                    // Espacio DESPUÉS de ":"
                    "enforce-decl-spacing-after-colon" -> {
                        if (ruleValue as Boolean) {
                            // true = permitir espacios = NO agregar regla que los elimina
                            null
                        } else {
                            // false = NO permitir espacios = agregar regla que los elimina
                            NoSpaceAfterColonRule()
                        }
                    }

                    // 1 salto de línea antes de println
                    "print-1-line-breaks-after" -> {
                        if (ruleValue as Boolean) {
                            LineBreakBeforePrintRule(1)
                        } else {
                            null
                        }
                    }

                    // 2 saltos de línea antes de println
                    "print-2-line-breaks-after" -> {
                        if (ruleValue as Boolean) {
                            LineBreakBeforePrintRule(2)
                        } else {
                            null
                        }
                    }

                    // Llave del if en la MISMA línea
                    "if-brace-same-line" -> {
                        if (ruleValue as Boolean) {
                            IfBraceOnSameLineRule()
                        } else {
                            null
                        }
                    }

                    // Llave del if en línea SEPARADA
                    "if-brace-below-line" -> {
                        if (ruleValue as Boolean) {
                            IfBraceBelowLineRule()
                        } else {
                            null
                        }
                    }

                    // Indentación con 2 espacios
                    "if-indent-inside-2" -> {
                        if (ruleValue as Boolean) {
                            IndentationRule(2)
                        } else {
                            null
                        }
                    }

                    // Indentación con tamaño configurable
                    "indent-inside-if-statement" -> {
                        val size = (ruleValue as? Number)?.toInt() ?: 2
                        IndentationRule(size)
                    }

                    // Espaciado de un solo token (ya está en mandatory)
                    "enforce-single-space-separation" -> null

                    // Espaciado alrededor de operaciones (ya está en mandatory)
                    "enforce-space-surrounding-operations" -> null

                    else -> null
                }

                rule?.let { add(it) }
            }
        }
    }

    internal fun readConfig(): Map<String, Any> {
        val text = File(configFile).readText()
        return mapper.readValue(text)
    }
}
