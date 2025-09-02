package formatter.src.main.kotlin

import common.src.main.kotlin.Container
import src.main.model.tools.interpreter.lexer.Lexer
import formatter.src.main.kotlin.formatrule.FormatRule

class Formatter(
    source: String,
    configFile: String
) {

    private val lexer: Lexer = Lexer(source)
    private val config: ConfigLoader = ConfigLoader(configFile)

    fun execute(): Container {
        val rules: List<FormatRule> = config.loadConfig()
        lexer.splitString()
        val tokens: Container = lexer.createToken(lexer.list)
        var result: Boolean
        for (rule: FormatRule in rules) {
            if(!rule.format(tokens)) {// An error has occurred
                return Container()
            }
        }
        return tokens
    }
}
