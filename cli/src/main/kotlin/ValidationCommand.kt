package src.main.model.tools.cli.cli

import src.main.model.tools.interpreter.lexer.Lexer
import parser.src.main.kotlin.Parser
import common.src.main.kotlin.ASTNode
import linter.Linter
import linter.LintRule
import java.io.File

class ValidationCommand : Command {
    override fun execute(args: List<String>) {
        if (args.isEmpty()) {
            println("Error: Debe especificar el archivo fuente")
            println("Uso: validation <archivo_fuente> [version]")
            return
        }

        val sourceFile = args[0]
        val version = if (args.size > 1) args[1] else "1.0"

        if (version != "1.0") {
            println("Error: Versión no soportada. Solo se admite la versión 1.0")
            return
        }

        try {
            val file = File(sourceFile)
            if (!file.exists()) {
                println("Error: El archivo '$sourceFile' no existe")
                return
            }

            val source = file.readText()
            println("Iniciando validación de '$sourceFile'...")

            // Progreso: Análisis léxico
            print("Realizando análisis léxico... ")
            val lexer = Lexer(source)
            lexer.splitString()
            val tokens = lexer.createToken(lexer.list)
            println("✓ Completado")

            // Progreso: Análisis sintáctico
            print("Realizando análisis sintáctico... ")
            val parser = Parser(tokens)
            val ast: ASTNode = parser.parse()
            println("✓ Completado")

            // Progreso: Análisis semántico (usando Linter)
            print("Realizando análisis semántico... ")

            // ⚠️ Por ahora sin reglas concretas, lista vacía
            val rules: List<LintRule> = emptyList()
            val linter = Linter(rules)
            val lintErrors = linter.all(ast)
            println("✓ Completado")

            if (lintErrors.isEmpty()) {
                println("\n✅ Validación exitosa: No se encontraron errores")
            } else {
                println("\n❌ Se encontraron ${lintErrors.size} error(es):")
                lintErrors.forEach { error: linter.LintError ->
                    println(error.toString())
                }
            }
        } catch (e: Exception) {
            println("Error durante la validación: ${e.message}")
            e.printStackTrace()
        }
    }
}
