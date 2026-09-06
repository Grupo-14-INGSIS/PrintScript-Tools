package cli.src.main.kotlin.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.types.file
import executor.Executor
import inputprovider.src.main.kotlin.ConsoleInputProvider
import inputprovider.src.main.kotlin.InputProvider

class ExecutionCommand(
    private val inputProvider: InputProvider = ConsoleInputProvider(),
    private val printer: (Any?) -> Unit = ::println
) : CliCommand(name = "execution", help = "Execute a PrintScript script") {

    val sourceFile by argument(name = "source_file", help = "Path to the script file")
        .file(canBeDir = false)

    val version by argument(name = "version", help = "PrintScript version (1.0 or 1.1)")
        .default("1.0")

    override fun run() {
        val executor = Executor(inputProvider, printer)
        executor.execute(sourceFile, version)
    }

    override fun execute(args: List<String>) {
        if (args.isEmpty()) {
            println("Must specify the source file.")
            return
        }
        super.execute(args)
    }
}
