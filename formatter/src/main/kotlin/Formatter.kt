package formatter.src.main.kotlin

import common.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class Formatter(var configFile: String) {

    private val tokenizer: Tokenizer = Tokenizer()
    private val config: ConfigLoader = ConfigLoader()

    fun execute(source: String): Int {
        val code: String = readFile(source) ?: return 1 // File not found
        var tokens: List<Token> = tokenizer.tokenize(code)

        val rules: List<FormatRule> = config.loadConfig(configFile)

        for (rule: FormatRule in rules) {
            tokens = rule.format(tokens)
        }

        writeFile(source, tokens)
        return 0
    }

    private fun readFile(file: String): String? {
    }

    private fun writeFile(file: String, tokens: List<Token>) {
    }
}
