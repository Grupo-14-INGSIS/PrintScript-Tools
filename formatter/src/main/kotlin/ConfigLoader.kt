package formatter.src.main.kotlin

import formatter.src.main.kotlin.formatrule.FormatRule
import formatter.src.main.kotlin.formatrule.mandatory.SpaceAroundOperatorRule
import formatter.src.main.kotlin.formatrule.mandatory.SpaceBetweenTokensRule
import formatter.src.main.kotlin.formatrule.mandatory.LineBreakAfterSemicolonRule
import formatter.src.main.kotlin.formatrule.mandatory.IfBraceOnSameLineRule
import formatter.src.main.kotlin.formatrule.mandatory.SpaceAroundEqualsRule
import formatter.src.main.kotlin.formatrule.optional.CharLimitPerLineRule
import formatter.src.main.kotlin.formatrule.optional.ClassNameCamelCaseRule
import formatter.src.main.kotlin.formatrule.optional.NoSpaceBeforeColonRule
import formatter.src.main.kotlin.formatrule.optional.NoSpaceAfterColonRule
import formatter.src.main.kotlin.formatrule.optional.LineBreakBeforePrintRule
import formatter.src.main.kotlin.formatrule.optional.IndentationRule
import org.yaml.snakeyaml.Yaml
import java.io.File

class ConfigLoader(
    private val configFile: String
) {

    fun loadConfig(version: String = "1.0"): List<FormatRule> {
        val config = readConfig()

        // Detectar versión desde el config
        val detectedVersion = detectVersion(config, version)

        val configurableRules = createConfigurableRules(config)
        val mandatoryRules = createMandatoryRules(detectedVersion)

        return (configurableRules + mandatoryRules).toList()
    }

    private fun detectVersion(config: Map<String, Any>, defaultVersion: String): String {
        // Si el config tiene "version" explícita, usarla
        val explicitVersion = config["version"] as? String
        if (explicitVersion != null) return explicitVersion

        // Si no, inferir de las reglas activadas
        return when {
            config["enforce-spacing-around-equals"] == true ->
                "1.0 - assign-spacing-surrounding-equals"
            else -> defaultVersion
        }
    }

    internal fun createMandatoryRules(version: String = "1.0"): List<FormatRule> {
        val baseRules = listOf(
            SpaceAroundOperatorRule(),
            SpaceBetweenTokensRule(),
            LineBreakAfterSemicolonRule(),
            IfBraceOnSameLineRule()
        )

        return when {
            version.contains("assign-spacing-surrounding-equals") -> baseRules + listOf(
                SpaceAroundEqualsRule()
            )
            version == "1.1" -> baseRules + listOf(
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
                rule?.let { add(it) }
            }
        }
    }

    internal fun readConfig(): Map<String, Any> {
        val yaml = Yaml()
        val data = yaml.load<Map<String, Any>>(
            File(configFile).readText()
        ) ?: return emptyMap()

        // Si tiene la estructura de "rules", procesarla
        if (data.containsKey("rules")) {
            val rules = data["rules"] as? Map<String, Any> ?: return emptyMap()
            val processedRules = buildMap {
                val switchRules = rules["switch"] as? Map<String, Boolean> ?: emptyMap()
                for ((rule, enabled) in switchRules) {
                    if (enabled) put(rule, true)
                }

                val valueRules = rules["setValue"] as? Map<String, Any> ?: emptyMap()
                for ((rule, value) in valueRules) {
                    put(rule, value)
                }
            }

            // Mantener la versión si existe
            val version = data["version"]
            return if (version != null) {
                processedRules + ("version" to version)
            } else {
                processedRules
            }
        }

        // Si no tiene "rules", devolver el data tal cual (formato del test)
        return data
    }
}
