package linter.config

data class RulesConfig(
    val identifierNaming: IdentifierNamingConfig? = null,
    val printlnArg: PrintLnConfig? = null
)
