package src.main.model.tools.cli.cli

import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container
import parser.src.main.kotlin.Parser
import src.main.model.tools.interpreter.lexer.Lexer
import java.io.File

class ExecutionCommand : Command {
    override fun execute(args: List<String>) {
        if (args.isEmpty()) {
            println("Must specify the source file.")
            println("Usage: execution <source_file>")
            return
        }

        val sourceFile = args[0]
        val file = File(sourceFile)
        if (!file.exists()) {
            println("Error: The source file '$sourceFile' does not exist.")
            return
        }

        val source = file.readText()
        println("Starting execution of '$sourceFile'...")

        /*
        No debieran haber salidas sin error, ya que de lo contrario
        se mezclarían con la ejecución del código
         */

        try {
            // Paso 1: Parsear a AST
            val lexer = Lexer(source)
            lexer.splitString()
            val tokens: Container = lexer.createToken(lexer.list)
            val parser = Parser(tokens)
            val ast: ASTNode = parser.parse()

            // Paso 2: Interpretar AST
//            print("Ejecutando código... ")
//            val interpreter = Interpreter()
//            val success = interpreter.interpret(ast, )
//            println("✓ Completado")
//
//            if (success) {
//                println("\n✅ Ejecución exitosa")
//            } else {
//                println("\n❌ Ejecución con errores")
//            }
        } catch (e: Exception) {
            println("Error during execution: ${e.message}")
            e.printStackTrace()
        }
    }
}
