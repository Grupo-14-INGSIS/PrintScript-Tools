package linter.src.main.kotlin.config

data class RulesConfig(
    val identifier_format: IdentifierNamingConfig? = null,
    val mandatory_variable_or_literal_in_println: PrintLnConfig? = null,
    val mandatory_variable_or_literal_in_readInput: ReadInputConfig? = null
)
