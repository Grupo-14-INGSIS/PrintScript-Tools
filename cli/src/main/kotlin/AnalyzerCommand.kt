package src.main.model.tools.cli.cli

import linter.Linter
import linter.LintRule
import src.main.model.tools.interpreter.lexer.Lexer
import parser.src.main.kotlin.Parser
import java.io.File

class AnalyzerCommand : Command {
    override fun execute(args: List<String>) {
        if (args.size < 2) {
            println("Error: Must specify the source file and the analysis configuration file.")
            println("Usage: analyzer <source_file> <configuration_file> [version]")
            return
        }

        val sourceFile = args[0]
        val configFile = args[1]
        val version = if (args.size > 2) args[2] else "1.0"

        val supportedVersions = setOf("1.0", "1.1")
        if (version !in supportedVersions) {
            println("Error: Unsupported version.")
            return
        }

        try {
            val sourceFileObj = File(sourceFile)
            val configFileObj = File(configFile)

            if (!sourceFileObj.exists()) {
                println("Error: The source file '$sourceFile' does not exist.")
                return
            }

            if (!configFileObj.exists()) {
                println("Error: The configuration file '$configFile' does not exist.")
                return
            }

            val source = sourceFileObj.readText()
            println("Starting formatting of '$sourceFile'.")

            // Progreso: Análisis léxico y sintáctico
            print("Building AST")
            val lexer = Lexer.from(source)
            lexer.split()
            val tokens = lexer.createToken(lexer.list)
            val parser = Parser(tokens)
            val ast = parser.parse()

            // Progreso: Cargando reglas de linting
            print("Loading analysis rules.")
            val lintRules = loadLintRules(configFile)
            val linter = Linter(lintRules)

            print("Executing code analysis.")
            val lintErrors = linter.all(ast)

            if (lintErrors.isEmpty()) {
                println("\nSUCCESS: No errors were found.")
            } else {
                println("FAILED: ${lintErrors.size} error(s) were found:")
                lintErrors.forEach { error -> println("* $error") }
            }
        } catch (e: Exception) {
            println("Error during analysis: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun loadLintRules(configFile: String): List<LintRule> {
        val loader = linter.config.ConfigLoader()
        val yamlMap = loader.loadYaml(configFile)

        val factory = linter.config.ConfigFactory()
        val config = factory.createConfig(yamlMap)

        val rules = mutableListOf<LintRule>()

        config.rules.identifierNaming?.let {
            rules += linter.rules.IdentifierNamingRule(it.style)
        }

        config.rules.printlnArg?.let {
            rules += linter.rules.PrintLnRule(it.enabled)
        }

        return rules
    }
}
