package formatter.src.main.kotlin

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.formatrule.FormatRule
import java.net.URL

class Formatter() {

    fun executeOne(tokens: Container, rule: FormatRule): Container {
        return rule.format(tokens)
    }

    fun loadRules(configFile: URL): List<FormatRule> {
        return ConfigLoader(configFile.path).loadConfig()
    }

    fun execute(tokens: Container, configFile: URL): Container {
        val rules: List<FormatRule> = loadRules(configFile)
        var tokens = tokens
        for (rule: FormatRule in rules) {
            tokens = executeOne(tokens, rule)
        }
        return tokens
    }
}
