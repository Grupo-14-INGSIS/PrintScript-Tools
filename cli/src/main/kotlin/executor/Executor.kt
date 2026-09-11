package executor

import ast.src.main.kotlin.ASTNode
import lexer.src.main.kotlin.Lexer
import parser.src.main.kotlin.Parser
import interpreter.src.main.kotlin.Interpreter
import progress.MultiStepProgress
import inputprovider.src.main.kotlin.ConsoleInputProvider
import inputprovider.src.main.kotlin.InputProvider
import cli.src.main.kotlin.version.VersionRegistry
import java.io.File

class Executor(
    private val inputProvider: InputProvider = ConsoleInputProvider(),
    private val printer: (Any?) -> Unit = ::println
) {

    fun execute(sourceFile: File, version: String = "1.0") {
        if (!VersionRegistry.isSupported(version)) {
            val supported = VersionRegistry.supportedVersions().joinToString(" and ")
            println("Error: Unsupported version. Only $supported are supported.")
            return
        }

        if (!sourceFile.exists()) {
            println("Error: The source file '${sourceFile.path}' does not exist.")
            return
        }

        val source = sourceFile.readText()
        println("Starting execution of '${sourceFile.path}' with PrintScript $version")

        val progress = MultiStepProgress()
        progress.initialize(3) // Now 3 steps: Lexing, Parsing+Execution

        try {
            // Paso 1: Análisis léxico
            val lexerStep = progress.startStep("Performing lexical analysis")
            val lexer = Lexer.from(source, version)
            val statements = lexer.lexIntoStatements().toList()
            lexerStep.complete("Lexical analysis completed: ${statements.size} statements found")

            // Pasos 2 y 3: Parseo y Ejecución por cada sentencia
            val executionStep = progress.startStep("Parsing and executing program")
            val interpreter = Interpreter(version, inputProvider, printer) // Default printer used here

            for (statement in statements) {
                val parser = Parser(statement, version) // Parser takes a single statement
                val ast: ASTNode = parser.parse()
                interpreter.interpret(ast)
            }

            executionStep.complete("Program executed successfully")

            progress.complete()
        } catch (e: Exception) {
            progress.stop()
            ErrorReporter.report("execution", e, null)
        }
    }

    fun execute(args: List<String>) {
        if (args.isEmpty()) {
            println("Must specify the source file.")
            println("Usage: execution <source_file> [version]")
            return
        }

        val sourceFile = args[0]
        val version = if (args.size > 1) args[1] else "1.0"
        execute(File(sourceFile), version)
    }
}
