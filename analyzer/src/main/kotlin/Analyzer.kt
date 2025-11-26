package analyzer.src.main.kotlin

import progress.src.main.kotlin.MultiStepProgress
import lexer.src.main.kotlin.Lexer

import parser.src.main.kotlin.Parser

import linter.src.main.kotlin.Linter
import linter.src.main.kotlin.LintRule

import linter.src.main.kotlin.config.ConfigFactory
import linter.src.main.kotlin.config.ConfigLoader
import linter.src.main.kotlin.rules.IdentifierNamingRule
import linter.src.main.kotlin.rules.PrintLnRule
import java.io.File

class Analyzer {
    fun execute(args: List<String>) {
        if (args.size < 2) {
            println(
                "Error: Must specify the source file and the analysis configuration file."
            )
            println(
                "Usage: analyzer <source_file> <configuration_file> [version]"
            )
            return
        }

        val sourceFile = args[0]
        val configFile = args[1]
        val version = if (args.size > 2) args[2] else "1.0"

        val supportedVersions = setOf("1.0", "1.1")
        if (version !in supportedVersions) {
            println(
                "Error: Unsupported version."
            )
            return
        }

        val sourceFileObj = File(sourceFile)
        val configFileObj = File(configFile)

        if (!sourceFileObj.exists()) {
            println(
                "Error: The source file '$sourceFile' does not exist."
            )
            return
        }

        if (!configFileObj.exists()) {
            println(
                "Error: The configuration file '$configFile' does not exist."
            )
            return
        }

        val source = sourceFileObj.readText()
        println(
            "Starting analysis of '$sourceFile' (PrintScript $version)"
        )

        val progress = MultiStepProgress()
        progress.initialize(3)

        try {
            val lexerStep = progress.startStep("Performing lexical analysis")
            val lexer = Lexer.from(source)
            val statements = lexer.lexIntoStatements()
            lexerStep.complete("Lexical analysis completed")

            val rulesStep = progress.startStep("Loading analysis rules")
            val lintRules: List<LintRule> = loadLintRules(configFile)
            rulesStep.complete("${lintRules.size} rule(s) loaded")
            val analysisStep = progress.startStep("Executing static code analysis")
            val linter = Linter(lintRules)

            val asts = statements.map { statement ->
                val parser = Parser(statement, version)
                parser.parse()
            }
            val allLintErrors = linter.lint(asts)
            analysisStep.complete("Analysis completed")

            progress.complete()

            if (allLintErrors.isEmpty()) {
                println("\nSUCCESS: No issues were found")
            } else {
                println("\nANALYSIS RESULTS: ${allLintErrors.size} issue(s) found:")
                allLintErrors.forEach { error ->
                    println("$error: $error")
                }
            }
        } catch (e: Exception) {
            println("Error during analysis: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun loadLintRules(configFile: String): List<LintRule> {
        val loader = ConfigLoader()
        val yamlMap = loader.loadYaml(configFile)

        val factory = ConfigFactory()
        val config = factory.createConfig(yamlMap)

        val rules = mutableListOf<LintRule>()

        config.rules.identifier_format?.let {
            rules += IdentifierNamingRule(
                it.style
            )
        }

        config.rules.mandatory_variable_or_literal_in_println?.let {
            rules += PrintLnRule(
                it.enabled
            )
        }

        return rules
    }
}
