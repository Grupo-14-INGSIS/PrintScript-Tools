package executor.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import lexer.src.main.kotlin.Lexer
import parser.src.main.kotlin.Parser
import interpreter.src.main.kotlin.Interpreter
import progress.src.main.kotlin.MultiStepProgress
import inputprovider.src.main.kotlin.ConsoleInputProvider
import inputprovider.src.main.kotlin.InputProvider
import java.io.File

class Executor(
    private val inputProvider: InputProvider = ConsoleInputProvider(),
    private val printer: (Any?) -> Unit = ::println
) {

    fun execute(args: List<String>) {
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

        val progress = MultiStepProgress()
        progress.initialize(3) // Now 3 steps: Lexing, Parsing+Execution

        try {
            // Paso 1: Análisis léxico
            val lexerStep = progress.startStep("Performing lexical analysis")
            val lexer = Lexer.from(source)
            val statements = lexer.lexIntoStatements().toList()
            lexerStep.complete("Lexical analysis completed: ${statements.size} statements found")

            // Pasos 2 y 3: Parseo y Ejecución por cada sentencia
            val executionStep = progress.startStep("Parsing and executing program")
            val interpreter = Interpreter(version, inputProvider) // Default printer used here

            val originalOut = System.out
            val capturedOutput = mutableListOf<String>()
            val printStream = object : java.io.PrintStream(originalOut) {
                override fun println(x: Any?) {
                    capturedOutput.add(x.toString())
                }
            }
            System.setOut(printStream)

            try {
                for (statement in statements) {
                    val parser = Parser(statement, version) // Parser takes a single statement
                    val ast: ASTNode = parser.parse()
                    interpreter.interpret(ast)
                }
            } finally {
                System.setOut(originalOut) // Restore System.out
            }

            capturedOutput.forEach { println(it) } // Print captured output to actual console
            executionStep.complete("Program executed successfully")

            progress.complete()
        } catch (e: Exception) {
            ErrorReporter.report("execution", e, null)
        }
    }
}
