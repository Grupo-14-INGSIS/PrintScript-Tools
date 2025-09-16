package linter.src.main.kotlin.config

data class RulesConfig(
    val identifier_format: IdentifierNamingConfig? = null,
    val printlnArg: PrintLnConfig? = null
)
