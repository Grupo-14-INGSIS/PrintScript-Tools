package cli.src.main.kotlin

import executor.src.main.kotlin.Executor

class ExecutionCommand : Command {

    override fun execute(args: List<String>) {
        val executor = Executor()
        executor.execute(args)
    }
}
