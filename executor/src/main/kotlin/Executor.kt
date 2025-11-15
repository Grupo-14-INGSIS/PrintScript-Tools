package executor.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container
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
            // Paso 1: Análisis léxico
            val lexerStep = progress.startStep(
                "Performing lexical analysis"
            )
            val lexer = Lexer.from(
                source
            )
            lexer.split()
            lexerStep.complete(
                "Lexical analysis completed"
            )

            // Paso 2: Generación de tokens
            val tokenStep = progress.startStep(
                "Generating tokens"
            )
            val tokens: Container = lexer.createToken(
                lexer.list
            )
            tokenStep.complete(
                "${tokens.size()} tokens generated"
            )

            // Paso 3: Análisis sintáctico
            val parserStep = progress.startStep(
                "Parsing source code..."
            )
            val parser = Parser(
                tokens
            )
            val ast: ASTNode = parser.parse()
            parserStep.complete(
                "AST built successfully"
            )

            // Paso 4: Ejecución
            val executionStep = progress.startStep(
                "Executing program"
            )
            val inputProvider = ConsoleInputProvider()
            val interpreter = Interpreter(
                version,
                inputProvider
            )
            val executionOutput = interpreter.executeAST(
                ast
            )
            executionOutput.forEach { println(it) }
            executionStep.complete(
                "Program executed successfully"
            )

            progress.complete()
        } catch (e: Exception) {
            println(
                "Error during execution: ${e.message}"
            )
            e.printStackTrace()
        }
    }
}
