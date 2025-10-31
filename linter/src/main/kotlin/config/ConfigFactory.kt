package linter.src.main.kotlin.config

class ConfigFactory {
    fun createConfig(yamlMap: Map<String, Any>): LinterConfig {
        val rulesMap = if (yamlMap.containsKey("rules")) {
            yamlMap["rules"] as? Map<String, Any> ?: emptyMap()
        } else {
            yamlMap // usar el mapa completo como reglas
        }
        //
        val identifierConfig = (rulesMap["identifier_format"] as? Map<String, Any>)?.let {
            IdentifierNamingConfig(it["style"] as? String ?: "camelCase")
        }

        val printlnConfig = (rulesMap["mandatory_variable_or_literal_in_println"] as? Map<String, Any>)?.let {
            PrintLnConfig(it["enabled"] as? Boolean ?: true)
        }

        return LinterConfig(
            rules = RulesConfig(
                identifier_format = identifierConfig,
                mandatory_variable_or_literal_in_println = printlnConfig
            )
        )
    }
}
