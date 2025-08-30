package linter.config

import linter.LinterConfig

class ConfigFactory {
    fun createConfig(yamlMap: Map<String, Any>): LinterConfig {
        val rulesMap = yamlMap["rules"] as? Map<String, Any> ?: emptyMap()

        val identifierConfig = (rulesMap["identifierNaming"] as? Map<String, Any>)?.let {
            IdentifierNamingConfig(it["style"] as? String ?: "camelCase")
        }

        val printlnConfig = (rulesMap["printlnArg"] as? Map<String, Any>)?.let {
            PrintLnConfig(it["enabled"] as? Boolean ?: true)
        }

        return LinterConfig(
            rules = RulesConfig(
                identifierNaming = identifierConfig,
                printlnArg = printlnConfig
            )
        )
    }
}
