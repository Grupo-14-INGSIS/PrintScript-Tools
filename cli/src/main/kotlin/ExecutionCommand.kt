package src.main.model.tools.cli.cli

import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container
import parser.src.main.kotlin.Parser
import src.main.model.tools.interpreter.interpreter.ConsoleInputProvider
import src.main.model.tools.interpreter.interpreter.Interpreter
import src.main.model.tools.interpreter.lexer.Lexer
import java.io.File

class ExecutionCommand : Command {
    override fun execute(args: List<String>) {
        if (args.isEmpty()) {
            println("Must specify the source file.")
            println("Usage: execution <source_file> [version]")
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
        println("Starting execution of '$sourceFile' with PrintScript $version")

        /*
        No debieran haber salidas sin error, ya que de lo contrario
        se mezclarían con la ejecución del código
         */

        try {
            // Paso 1: Parsear a AST
            val lexer = Lexer.from(source)
            lexer.split()
            val tokens: Container = lexer.createToken(lexer.list)

            // Paso 2: analisis sintactivo
            val parser = Parser(tokens)
            val ast: ASTNode = parser.parse()

            // Paso 3
            val inputProvider = ConsoleInputProvider()
            val interpreter = Interpreter(version, inputProvider)

            executeAST(interpreter, ast)

            println("Execution completed successfully")
        } catch (e: Exception) {
            println("Error during execution: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun executeAST(interpreter: Interpreter, ast: ASTNode) {
        val queue = ArrayDeque<ASTNode>()
        queue.add(ast)

        while (queue.isNotEmpty()) {
            val currentNode = queue.removeFirst()

            // que accion conlleva el nodo
            val action = try {
                interpreter.determineAction(currentNode)
            } catch (e: Exception) {
                println("Unknown action for token '${currentNode.token.content}': ${e.message}")
                continue
            }

            // llevo a cabo esa accion
            try {
                val result = interpreter.interpret(currentNode, action)
                if (result != null) {
                    println("Result of '${action.name}': $result")
                }
            } catch (e: Exception) {
                println("Error interpreting node '${currentNode.token.content}': ${e.message}")
            }

            // si existen hijos -> encolarlso
            currentNode.children.forEach { child ->
                queue.add(child)
            }
        }
    }
}
