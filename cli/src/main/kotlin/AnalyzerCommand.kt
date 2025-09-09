package cli.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container
import linter.src.main.kotlin.LintError
import linter.src.main.kotlin.Linter
import linter.src.main.kotlin.LintRule
import lexer.src.mail.kotlin.Lexer
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
        println("Starting analysis of '$sourceFile' (PrintScript $version)")

        val progress = MultiStepProgress()
        progress.initialize(5) // lexer, tokens, parser, load rules, analyze

        try {
            // Paso 1: Análisis léxico
            val lexerStep = progress.startStep("Performing lexical analysis")
            val lexer = Lexer.from(source)
            lexer.split()
            lexerStep.complete("Lexical analysis completed")

            // Paso 2: Generación de tokens
            val tokenStep = progress.startStep("Generating tokens")
            val tokens: Container = lexer.createToken(lexer.list)
            tokenStep.complete("${tokens.size()} tokens generated")

            // Paso 3: Análisis sintáctico
            val parserStep = progress.startStep("Building Abstract Syntax Tree")
            val parser = Parser(tokens)
            val ast: ASTNode = parser.parse()
            parserStep.complete("AST built successfully")

            // Paso 4: Cargar reglas de análisis
            val rulesStep = progress.startStep("Loading analysis rules")
            val lintRules: List<LintRule> = loadLintRules(configFile)
            rulesStep.complete("${lintRules.size} rule(s) loaded")

            // Paso 5: Ejecutar análisis
            val analysisStep = progress.startStep("Executing static code analysis")
            val linter = Linter(lintRules)
            val lintErrors: List<LintError> = linter.all(ast)

            if (lintErrors.isEmpty()) {
                analysisStep.complete("Analysis completed")
                progress.complete()
                println("\nSUCCESS: No issues were found")
            } else {
                analysisStep.complete("Analysis completed")
                println("\nANALYSIS RESULTS: ${lintErrors.size} issue(s) found:")
                lintErrors.forEach { error ->
                    println("$error: $error")
                }
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
