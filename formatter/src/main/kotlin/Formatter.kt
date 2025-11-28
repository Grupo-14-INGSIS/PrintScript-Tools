package formatter.src.main.kotlin

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.formatrule.FormatRule
import java.net.URL

class Formatter() {

    fun loadRules(configFile: URL): List<FormatRule> {
        return ConfigLoader(configFile.path).loadConfig()
    }

    fun execute(statements: List<Container>, configFile: URL): List<Container> {
        val rules: List<FormatRule> = loadRules(configFile)
        var currentStatements = statements
        for (rule in rules) {
            currentStatements = rule.format(currentStatements)
        }
        return currentStatements
    }
}
