package formatter.src.main.kotlin

import container.src.main.kotlin.Container
import src.main.model.tools.interpreter.lexer.Lexer
import formatter.src.main.kotlin.formatrule.FormatRule

class Formatter(
    source: String,
    configFile: String
) {

    private val lexer: Lexer = Lexer.from(source)
    private val config: ConfigLoader = ConfigLoader(configFile)

    fun execute(): Container {
        val rules: List<FormatRule> = config.loadConfig()
        lexer.split()
        var tokens: Container = lexer.createToken(lexer.list)
        for (rule: FormatRule in rules) {
            tokens = rule.format(tokens)
        }
        return tokens
    }
}
