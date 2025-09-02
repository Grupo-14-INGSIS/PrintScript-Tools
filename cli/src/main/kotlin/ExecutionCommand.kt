package src.main.model.tools.cli.cli

import common.src.main.kotlin.ASTNode
import parser.src.main.kotlin.Parser
import src.main.model.tools.interpreter.lexer.Lexer
import java.io.File

class ExecutionCommand : Command {
    override fun execute(args: List<String>) {
        if (args.isEmpty()) {
            println("Error: Debe especificar el archivo fuente")
            println("Uso: execution <archivo_fuente>")
            return
        }

        val sourceFile = args[0]
        val file = File(sourceFile)
        if (!file.exists()) {
            println("Error: El archivo '$sourceFile' no existe")
            return
        }

        val source = file.readText()
        println("Iniciando ejecución de '$sourceFile'...")

        try {
            // Paso 1: Parsear a AST
            print("Parseando fuente... ")
            val lexer = Lexer(source)
            lexer.splitString()
            val tokens = lexer.createToken(lexer.list)
            val parser = Parser(tokens)
            val ast: ASTNode = parser.parse()
            println("✓ Completado")

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
            println("Error durante la ejecución: ${e.message}")
            e.printStackTrace()
        }
    }
}
