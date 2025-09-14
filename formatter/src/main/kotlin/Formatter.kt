package formatter.src.main.kotlin

import container.src.main.kotlin.Container
import lexer.src.main.kotlin.Lexer
import formatter.src.main.kotlin.formatrule.FormatRule
import java.net.URL

class Formatter(
    source: String,
    configFile: URL
) {

    private val lexer: Lexer = Lexer.from(source)
    private val config: ConfigLoader = ConfigLoader(configFile.path)

    fun setup(): FormatterSetup {
        lexer.split()
        val tokens: Container = lexer.createToken(lexer.list)
        val rules: List<FormatRule> = config.loadConfig()
        return FormatterSetup(tokens, rules)
    }

    fun execute(tokens: Container, rule: FormatRule): Container {
        return rule.format(tokens)
    }

    fun execute(): Container {
        val setup = setup()
        var tokens: Container = setup.getTokens() // ya no puedo acceder directo
        for (rule: FormatRule in setup.getRules()) {
            tokens = execute(tokens, rule)
        }
        return tokens
    }
}
