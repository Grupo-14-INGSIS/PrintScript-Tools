package cli.src.main.kotlin

import lexer.src.main.kotlin.Lexer
import parser.src.main.kotlin.Parser
import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container
import linter.src.main.kotlin.LintError
import linter.src.main.kotlin.Linter
import linter.src.main.kotlin.LintRule
import java.io.File

class ValidationCommand : Command {
    override fun execute(args: List<String>) {
        if (args.isEmpty()) {
            println("Error: Must specify the source file.")
            println("Usage: validation <archivo_fuente> [version]")
            return
        }

        val sourceFile = args[0]
        val version = if (args.size > 1) args[1] else "1.0"

        if (version !in listOf("1.0", "1.1")) {
            println("Error: Unsupported version. Only 1.0 and 1.1 are supported.")
            return
        }

        val file = File(sourceFile)
        if (!file.exists()) {
            println("Error: The source file '$sourceFile' does not exist.")
            return
        }

        val source = file.readText()
        println("Starting validation of '$sourceFile' (PrintScript $version)")

        val progress = MultiStepProgress()
        progress.initialize(4) // lexer, tokens, parser, validation

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

            // Paso 4: Validación semántica
            val validationStep = progress.startStep("Performing semantic validation")
            val rules: List<LintRule> = emptyList() // Sin reglas por ahora
            val linter = Linter(rules)
            val lintErrors: List<LintError> = linter.all(ast)

            if (lintErrors.isEmpty()) {
                validationStep.complete("No errors found")
                progress.complete()
                println("\nSUCCESS: Code validation passed")
            } else {
                validationStep.fail("${lintErrors.size} error(s) found")
                println("\nFAILED: ${lintErrors.size} error(s) were found:")
                lintErrors.forEach { error -> println("$error") }
            }
        } catch (e: Exception) {
            println("Error during validation: ${e.message}")
            e.printStackTrace()
        }
    }
}
