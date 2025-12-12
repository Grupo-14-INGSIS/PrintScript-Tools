package cli.src.main.kotlin

import runner.src.main.kotlin.Runner

class Cli {
    private val runner = Runner()
    fun run(args: List<String>) {
        if (args.isEmpty()) {
            println("Must specify a command: formatter | analyzer | validation | execution")
            return
        }

        val commandName = args[0]
        val commandArgs = args.drop(1)

        when (commandName) {
            "formatter" -> runner.formatterCommand(commandArgs)
            "analyzer" -> runner.analyzerCommand(commandArgs)
            "execution" -> runner.executionCommand(commandArgs)
            "validation" -> runner.validationCommand(commandArgs)
            else -> println("Unknown command: $commandName")
        }
    }
}
