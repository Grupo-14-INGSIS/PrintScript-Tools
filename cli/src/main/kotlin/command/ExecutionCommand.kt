package cli.src.main.kotlin.command

import executor.src.main.kotlin.Executor
import inputprovider.src.main.kotlin.ConsoleInputProvider
import inputprovider.src.main.kotlin.InputProvider

class ExecutionCommand(
    private val inputProvider: InputProvider = ConsoleInputProvider(),
    private val printer: (Any?) -> Unit = ::println
) : CliCommand {
    override val name: String = "execution"
    override val description: String = "Execute a PrintScript script"
    override fun execute(args: List<String>) {
        val executor = Executor(inputProvider, printer)
        executor.execute(args)
    }
}
