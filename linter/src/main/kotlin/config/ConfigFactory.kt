package linter.src.main.kotlin.config

import linter.src.main.kotlin.LintRule
import linter.src.main.kotlin.LintRuleRegistry

class ConfigFactory {

    fun createRules(yamlMap: Map<String, Any>): List<LintRule> =
        LintRuleRegistry.createRules(yamlMap)

    fun createConfig(yamlMap: Map<String, Any>): LinterConfig {
        val rulesMap = if (yamlMap.containsKey("rules")) {
            yamlMap["rules"] as? Map<String, Any> ?: emptyMap()
        } else {
            yamlMap // usar el mapa completo como reglas
        }

        val identifierConfig = (rulesMap["identifier_format"] as? Map<String, Any>)?.let {
            IdentifierNamingConfig(it["style"] as? String ?: "camelCase")
        }

        val rawPrintln = rulesMap["mandatory-variable-or-literal-in-println"]
            ?: rulesMap["mandatory_variable_or_literal_in_println"]
        val printlnConfig = (rawPrintln as? Map<String, Any>)?.let {
            PrintLnConfig(it["enabled"] as? Boolean ?: true)
        }

        val rawReadInput = rulesMap["mandatory-variable-or-literal-in-readInput"]
            ?: rulesMap["mandatory_variable_or_literal_in_readInput"]
        val readInputConfig = (rawReadInput as? Map<String, Any>)?.let {
            ReadInputConfig(it["enabled"] as? Boolean ?: true)
        }

        return LinterConfig(
            rules = RulesConfig(
                identifier_format = identifierConfig,
                mandatory_variable_or_literal_in_println = printlnConfig,
                mandatory_variable_or_literal_in_readInput = readInputConfig
            )
        )
    }
}
