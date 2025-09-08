package src.main.model.tools.cli.cli

import src.main.model.tools.interpreter.lexer.Lexer
import parser.src.main.kotlin.Parser
import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container
import linter.LintError
import linter.Linter
import linter.LintRule
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

        if (version != "1.0") {
            println("Error: Unsupported version. Only 1.0 is admitted.")
            return
        }

        try {
            val file = File(sourceFile)
            if (!file.exists()) {
                println("Error: The source file '$sourceFile' does not exist.")
                return
            }

            val source = file.readText()
            //println("Iniciando validación de '$sourceFile'...")

            // Progreso: Análisis léxico
            print("Analyzing")
            val lexer = Lexer.from(source)
            lexer.split()
            val tokens: Container = lexer.createToken(lexer.list)

            // Progreso: Análisis sintáctico
            val parser = Parser(tokens)
            val ast: ASTNode = parser.parse()

            // Progreso: Análisis semántico (usando Linter)

            // ⚠️ Por ahora sin reglas concretas, lista vacía
            val rules: List<LintRule> = emptyList()
            val linter = Linter(rules)
            val lintErrors: List<LintError> = linter.all(ast)

            if (lintErrors.isEmpty()) {
                println("\nSUCCESS: No errors were found")
            } else {
                println("\nFAILED: ${lintErrors.size} error(s) were found:")
                lintErrors.forEach { error: LintError ->
                    println(error.toString())
                }
            }
        } catch (e: Exception) {
            println("Error durante validation: ${e.message}")
            e.printStackTrace()
        }
    }
}
