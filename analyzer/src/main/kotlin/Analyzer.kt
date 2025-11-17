package analyzer.src.main.kotlin

import progress.src.main.kotlin.MultiStepProgress
import lexer.src.main.kotlin.Lexer
import container.src.main.kotlin.Container
import parser.src.main.kotlin.Parser
import ast.src.main.kotlin.ASTNode
import linter.src.main.kotlin.Linter
import linter.src.main.kotlin.LintRule
import linter.src.main.kotlin.LintError
import linter.src.main.kotlin.config.ConfigFactory
import linter.src.main.kotlin.config.ConfigLoader
import linter.src.main.kotlin.rules.IdentifierNamingRule
import linter.src.main.kotlin.rules.PrintLnRule
import tokendata.src.main.kotlin.Position
import java.io.File

class Analyzer {

    // Modo validación: solo sintaxis y semántica
    fun executeValidation(args: List<String>) {
        if (args.isEmpty()) {
            println("Error: Must specify the source file.")
            println("Usage: validation <source_file> [version]")
            return
        }

        val sourceFile = args[0]
        val version = if (args.size > 1) args[1] else "1.0"

        val supportedVersions = setOf("1.0", "1.1")
        if (version !in supportedVersions) {
            println("Error: Unsupported version '$version'.")
            println("Supported versions: ${supportedVersions.joinToString(", ")}")
            return
        }

        val sourceFileObj = File(sourceFile)

        if (!sourceFileObj.exists()) {
            println("Error: The source file '$sourceFile' does not exist.")
            return
        }

        val source = sourceFileObj.readText()
        println("Starting validation of '$sourceFile' (PrintScript $version)")

        val progress = MultiStepProgress()
        progress.initialize(3)

        try {
            val lexerStep = progress.startStep("Performing lexical analysis")
            val lexer = Lexer.from(source)
            lexer.split()
            lexerStep.complete("Lexical analysis completed")

            val tokenStep = progress.startStep("Generating tokens")
            val tokens: Container = lexer.createToken(lexer.list)
            tokenStep.complete("${tokens.size()} tokens generated")

            val parserStep = progress.startStep("Validating syntax")
            val parser = Parser(tokens, version)
            val ast: ASTNode = parser.parse()

            // Verificar si el AST es inválido
            if (ast.content.isEmpty() && ast.children.isEmpty()) {
                parserStep.complete("Syntax validation failed")
                println("\nSYNTAX ERROR: Invalid syntax detected")

                // Intentar encontrar el último token válido para dar contexto
                if (tokens.size() > 0) {
                    val lastToken = tokens.get(tokens.size() - 1)
                    if (lastToken != null) {
                        val pos: Position = lastToken.position
                        println("Near line ${pos.line}, column ${pos.column}")
                        println("Last token: '${lastToken.content}' (${lastToken.type})")
                    }
                }
                return
            }

            parserStep.complete("Syntax validation completed")
            progress.complete()

            println("\nSUCCESS: File is syntactically and semantically valid")
        } catch (e: Exception) {
            println("\nERROR during validation: ${e.message}")

            // Intentar extraer información de posición si está disponible
            val stackTrace = e.stackTrace.firstOrNull()
            if (stackTrace != null) {
                println("Location: Line ${stackTrace.lineNumber}")
            }

            // Mostrar más detalles si es un error de parsing conocido
            when {
                e.message?.contains("syntax", ignoreCase = true) == true -> {
                    println("This appears to be a syntax error. Please check your code structure.")
                }
                e.message?.contains("unexpected", ignoreCase = true) == true -> {
                    println("Unexpected token found. Please review the syntax near the error location.")
                }
            }
        }
    }

    // Modo análisis: sintaxis + semántica + linting
    fun execute(args: List<String>) {
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
            println("Error: Unsupported version '$version'.")
            println("Supported versions: ${supportedVersions.joinToString(", ")}")
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
        progress.initialize(5)

        try {
            val lexerStep = progress.startStep("Performing lexical analysis")
            val lexer = Lexer.from(source)
            lexer.split()
            lexerStep.complete("Lexical analysis completed")

            val tokenStep = progress.startStep("Generating tokens")
            val tokens: Container = lexer.createToken(lexer.list)
            tokenStep.complete("${tokens.size()} tokens generated")

            val parserStep = progress.startStep("Building Abstract Syntax Tree")
            val parser = Parser(tokens, version)
            val ast: ASTNode = parser.parse()
            parserStep.complete("AST built successfully")

            val rulesStep = progress.startStep("Loading analysis rules")
            val lintRules: List<LintRule> = loadLintRules(configFile)
            rulesStep.complete("${lintRules.size} rule(s) loaded")

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
                    println("$error")
                }
            }
        } catch (e: Exception) {
            println("Error during analysis: ${e.message}")

            // Intentar extraer información de posición si está disponible
            val stackTrace = e.stackTrace.firstOrNull()
            if (stackTrace != null) {
                println("Location: Line ${stackTrace.lineNumber}")
            }

            // Mostrar más detalles del error
            when {
                e.message?.contains("syntax", ignoreCase = true) == true -> {
                    println("This appears to be a syntax error. Please check your code structure.")
                }
                e.message?.contains("config", ignoreCase = true) == true -> {
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
