package linter.src.main.kotlin.config

data class RulesConfig(
    @Suppress("ConstructorParameterNaming")
    val identifier_format: IdentifierNamingConfig? = null,
    @Suppress("ConstructorParameterNaming")
    val mandatory_variable_or_literal_in_println: PrintLnConfig? = null,
    @Suppress("ConstructorParameterNaming")
    val mandatory_variable_or_literal_in_readInput: ReadInputConfig? = null
)
