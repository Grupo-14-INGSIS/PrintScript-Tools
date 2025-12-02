package analyzer.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import progress.src.main.kotlin.MultiStepProgress
import lexer.src.main.kotlin.Lexer
import parser.src.main.kotlin.Parser
import linter.src.main.kotlin.Linter
import linter.src.main.kotlin.LintRule
import linter.src.main.kotlin.config.ConfigFactory
import linter.src.main.kotlin.config.ConfigLoader
import linter.src.main.kotlin.rules.IdentifierNamingRule
import linter.src.main.kotlin.rules.PrintLnRule
import tokendata.src.main.kotlin.DataType
import java.io.File

class Analyzer {

    // Modo validación: solo sintaxis y semántica
    fun executeValidation(args: List<String>) {
        executeAnalysis(args, includeLinting = false)
    }

    // Modo análisis: sintaxis + semántica + linting
    fun execute(args: List<String>) {
        executeAnalysis(args, includeLinting = true)
    }

    private fun executeAnalysis(args: List<String>, includeLinting: Boolean) {
        // Validar argumentos según el modo
        if (includeLinting) {
            if (args.size < 2) {
                println("Error: Must specify the source file and the analysis configuration file.")
                println("Usage: analyzer <source_file> <configuration_file> [version]")
                return
            }
        } else {
            if (args.isEmpty()) {
                println("Error: Must specify the source file.")
                println("Usage: validation <source_file> [version]")
                return
            }
        }

        val sourceFile = args[0]
        val configFile = if (includeLinting && args.size > 1) args[1] else null
        val versionIndex = if (includeLinting) 2 else 1
        val version = if (args.size > versionIndex) args[versionIndex] else "1.0"

        val supportedVersions = setOf("1.0", "1.1")
        if (version !in supportedVersions) {
            println("Error: Unsupported version '$version'.")
            println("Supported versions: ${supportedVersions.joinToString(", ")}")
            return
        }

        // Validar archivos
        val sourceFileObj = File(sourceFile)
        if (!sourceFileObj.exists()) {
            println("Error: The source file '$sourceFile' does not exist.")
            return
        }

        if (includeLinting && configFile != null) {
            val configFileObj = File(configFile)
            if (!configFileObj.exists()) {
                println("Error: The configuration file '$configFile' does not exist.")
                return
            }
        }

        val source = sourceFileObj.readText()
        val mode = if (includeLinting) "analysis" else "validation"
        println("Starting $mode of '$sourceFile' (PrintScript $version)")

        val stepsCount = if (includeLinting) 4 else 3
        val progress = MultiStepProgress()
        progress.initialize(stepsCount)

        var hasError = false

        try {
            // Paso 1: Análisis léxico
            val lexerStep = progress.startStep("Performing lexical analysis")
            val lexer = Lexer.from(source)
            val statements = lexer.lexIntoStatements()
            lexerStep.complete("Lexical analysis completed: ${statements.size} statements found")

            // Paso 2: Cargar reglas (solo si se incluye linting)
            var lintRules: List<LintRule>? = null
            if (includeLinting && configFile != null) {
                val rulesStep = progress.startStep("Loading analysis rules")
                lintRules = loadLintRules(configFile)
                rulesStep.complete("${lintRules.size} rule(s) loaded")
            }

            // Paso 3: Parsing y validación sintáctica
            val parsingStep = progress.startStep("Validating syntax")
            val asts = mutableListOf<ASTNode>()

            for (statement in statements) {
                val parser = Parser(statement, version)
                val ast: ASTNode = parser.parse()

                if (ast.type == DataType.INVALID) {
                    hasError = true
                    parsingStep.complete("Syntax validation failed for statement")
                    println("\nSYNTAX ERROR: Invalid syntax detected in statement")
                    ErrorReporter.report(mode, Exception("Invalid AST for statement"), statement)
                    break
                }
                asts.add(ast)
            }

            if (!hasError) {
                parsingStep.complete("Syntax validation completed")

                // Paso 4: Linting (solo si se incluye)
                if (includeLinting && lintRules != null) {
                    val lintingStep = progress.startStep("Executing static code analysis")
                    val linter = Linter(lintRules)
                    val allLintErrors = linter.lint(asts)
                    lintingStep.complete("Analysis completed")

                    progress.complete()

                    if (allLintErrors.isEmpty()) {
                        println("\nSUCCESS: No issues were found")
                    } else {
                        println("\nANALYSIS RESULTS: ${allLintErrors.size} issue(s) found:")
                        allLintErrors.forEach { error ->
                            println("  - $error")
                        }
                    }
                } else {
                    progress.complete()
                    println("\nSUCCESS: File is syntactically and semantically valid")
                }
            }
        } catch (e: Exception) {
            ErrorReporter.report(mode, e, null)

            when {
                e.message?.contains("syntax", ignoreCase = true) == true -> {
                    println("This appears to be a syntax error. Please check your code structure.")
                }
                e.message?.contains("unexpected", ignoreCase = true) == true -> {
                    println("Unexpected token found. Please review the syntax near the error location.")
                }
                includeLinting && e.message?.contains("config", ignoreCase = true) == true -> {
                    println("Configuration file error. Please verify the YAML format.")
                }
            }
        }
    }

    private fun loadLintRules(configFile: String): List<LintRule> {
        val loader = ConfigLoader()
        val yamlMap = loader.loadYaml(configFile)

        val factory = ConfigFactory()
        val config = factory.createConfig(yamlMap)

        val rules = mutableListOf<LintRule>()

        config.rules.identifier_format?.let {
            rules += IdentifierNamingRule(it.style)
        }

        config.rules.mandatory_variable_or_literal_in_println?.let {
            rules += PrintLnRule(it.enabled)
        }

        return rules
    }
}
