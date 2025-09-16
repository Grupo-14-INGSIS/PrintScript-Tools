package linter.src.main.kotlin.config

class ConfigFactory {
    fun createConfig(yamlMap: Map<String, Any>): LinterConfig {
        val rulesMap = yamlMap["rules"] as? Map<String, Any> ?: emptyMap()

        val identifierConfig = (rulesMap["identifier_format"] as? Map<String, Any>)?.let {
            IdentifierNamingConfig(it["style"] as? String ?: "camelCase")
        }

        val printlnConfig = (rulesMap["printlnArg"] as? Map<String, Any>)?.let {
            PrintLnConfig(it["enabled"] as? Boolean ?: true)
        }

        return LinterConfig(
            rules = RulesConfig(
                identifier_format = identifierConfig,
                printlnArg = printlnConfig
            )
        )
    }
}
