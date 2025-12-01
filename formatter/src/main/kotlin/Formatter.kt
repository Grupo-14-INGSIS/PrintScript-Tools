package formatter.src.main.kotlin

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.formatrule.FormatRule
import java.io.File

class Formatter() {

    fun loadRules(configFilePath: String): List<FormatRule> {
        return ConfigLoader(configFilePath).loadConfig()
    }

    fun execute(statements: List<Container>, configFile: File): List<Container> {
        val rules: List<FormatRule> = loadRules(configFile.path) // Call with path
        var currentStatements = statements
        try {
            for (rule in rules) {
                currentStatements = rule.format(currentStatements)
            }
        } catch (e: Exception) {
            ErrorReporter.report("formatting", e, currentStatements[0])
            throw e
        }
        return currentStatements
    }
}
