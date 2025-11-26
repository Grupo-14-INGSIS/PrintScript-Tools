package executor.src.main.kotlin

import ast.src.main.kotlin.ASTNode

import lexer.src.main.kotlin.Lexer
import parser.src.main.kotlin.Parser
import interpreter.src.main.kotlin.Interpreter
import progress.src.main.kotlin.MultiStepProgress
import inputprovider.src.main.kotlin.ConsoleInputProvider
import java.io.File

class Executor {

    fun execute(args: List<String>) {
        if (args.isEmpty()) {
            println(
                "Must specify the source file."
            )
            println(
                "Usage: execution <source_file> [version]"
            )
            return
        }

        val sourceFile = args[0]

        val version = if (args.size > 1) args[1] else "1.0"

        if (version !in listOf("1.0", "1.1")) {
            println(
                "Error: Unsupported version. Only 1.0 and 1.1 are supported."
            )
            return
        }

        val file = File(sourceFile)
        if (!file.exists()) {
            println(
                "Error: The source file '$sourceFile' does not exist."
            )
            return
        }

        val source = file.readText()
        println(
            "Starting execution of '$sourceFile' with PrintScript $version"
        )

        val progress = MultiStepProgress()
        progress.initialize(4)

        try {
            // Paso 1: Análisis léxico y de tokens
            val lexerStep = progress.startStep("Performing lexical and token analysis")
            val lexer = Lexer.from(source)
            val statements = lexer.lexIntoStatements() // Returns List<Container>
            lexerStep.complete("Lexical and token analysis completed: ${statements.size} statements found")

            // Pasos 2, 3 y 4: Parseo, y Ejecución por cada sentencia
            val executionStep = progress.startStep("Parsing, and executing program")
            val inputProvider = ConsoleInputProvider()
            val interpreter = Interpreter(version, inputProvider)
            val finalOutput = mutableListOf<String>()

            for (statement in statements) {
                // No se necesita un paso de "generación de tokens" porque ya están en el container
                // Análisis sintáctico
                val parser = Parser(statement)
                val ast: ASTNode = parser.parse()

                // Ejecución
                val executionOutput = interpreter.executeAST(ast)
                finalOutput.addAll(executionOutput)
            }

            finalOutput.forEach { println(it) }
            executionStep.complete("Program executed successfully")

            progress.complete()
        } catch (e: Exception) {
            println(
                "Error during execution: ${e.message}"
            )
            e.printStackTrace()
        }
    }
}
