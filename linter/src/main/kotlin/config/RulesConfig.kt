package linter.src.main.kotlin.config

data class RulesConfig(
    val identifierNaming: IdentifierNamingConfig? = null,
    val printlnArg: PrintLnConfig? = null
)
