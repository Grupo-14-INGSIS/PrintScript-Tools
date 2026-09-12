package linter.src.main.kotlin

import linter.src.main.kotlin.rules.IdentifierNamingRule
import linter.src.main.kotlin.rules.IfWithoutElseRule
import linter.src.main.kotlin.rules.PrintLnRule
import linter.src.main.kotlin.rules.ReadInputRule


/**
 * Registro desacoplado de factorias de reglas del linter.
 * Permite resolver reglas a partir del mapa de configuracion YAML
 * cumpliendo con el principio Open/Closed (OCP).
 */
object LintRuleRegistry {

    private val factories = mutableMapOf<String, LintRuleFactory>()

    init {
        registerDefaults()
    }

    private fun registerDefaults() {
        register("identifier_format") { config ->
            val style = config["style"] as? String ?: "camelCase"
            IdentifierNamingRule(style)
        }

        val printlnFactory = LintRuleFactory { config ->
            val enabled = config["enabled"] as? Boolean ?: true
            if (enabled) PrintLnRule(true) else null
        }
        register("mandatory-variable-or-literal-in-println", printlnFactory)
        register("mandatory_variable_or_literal_in_println", printlnFactory)

        val readInputFactory = LintRuleFactory { config ->
            val enabled = config["enabled"] as? Boolean ?: true
            if (enabled) ReadInputRule(true) else null
        }
        register("mandatory-variable-or-literal-in-readInput", readInputFactory)
        register("mandatory_variable_or_literal_in_readInput", readInputFactory)

        val ifWithoutElseFactory = LintRuleFactory { config ->
            val enabled = config["enabled"] as? Boolean ?: true
            if (enabled) IfWithoutElseRule() else null
        }
        register("if-without-else", ifWithoutElseFactory)
        register("if_without_else", ifWithoutElseFactory)
    }

    fun register(ruleName: String, factory: LintRuleFactory) {
        factories[ruleName] = factory
    }

    fun createRules(yamlMap: Map<String, Any>): List<LintRule> {
        val rulesMap = if (yamlMap.containsKey("rules")) {
            @Suppress("UNCHECKED_CAST")
            yamlMap["rules"] as? Map<String, Any> ?: emptyMap()
        } else {
            yamlMap
        }

        val rules = mutableListOf<LintRule>()
        rulesMap.forEach { (name, value) ->
            if (value is Map<*, *>) {
                @Suppress("UNCHECKED_CAST")
                val rule = factories[name]?.create(value as Map<String, Any>)
                if (rule != null) {
                    rules.add(rule)
                }
            }
        }
        return rules
    }
}
