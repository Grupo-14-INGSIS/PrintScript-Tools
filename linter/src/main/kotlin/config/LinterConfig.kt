package linter.src.main.kotlin.config

data class LinterConfig(
    val rules: RulesConfig
)



/*
//CASO DE USO
val config = loadConfig("config.yaml")

val rules = mutableListOf<LintRule>()
config.rules.identifierNaming?.let { rules += IdentifierNamingRule(it.style) }
config.rules.printlnArg?.let { rules += PrintlnArgRule(it.enabled) }

val linter = Linter(rules)
val errors = linter.all(rootAST)

 */
