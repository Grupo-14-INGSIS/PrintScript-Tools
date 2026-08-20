package cli.src.main.kotlin

import cli.src.main.kotlin.command.CliCommand
import cli.src.main.kotlin.command.FormatterCommand
import cli.src.main.kotlin.command.AnalyzerCommand
import cli.src.main.kotlin.command.ValidationCommand
import cli.src.main.kotlin.command.ExecutionCommand

/**
 * CLI extensible basada en el patron Command / Plugins.
 * Permite registrar comandos dinamicamente y despacharlos de forma agnostica.
 */
class Cli(
    initialCommands: List<CliCommand> = listOf(
        FormatterCommand(),
        AnalyzerCommand(),
        ValidationCommand(),
        ExecutionCommand()
    )
) {
    private val commandMap = mutableMapOf<String, CliCommand>()

    init {
        initialCommands.forEach { registerCommand(it) }
    }

    fun registerCommand(command: CliCommand) {
        commandMap[command.name] = command
    }

    fun availableCommands(): List<String> = commandMap.keys.toList()

    fun run(args: List<String>) {
        if (args.isEmpty()) {
            val commandList = if (commandMap.isNotEmpty()) {
                commandMap.keys.joinToString(" | ")
            } else {
                "formatter | analyzer | validation | execution"
            }
            println("Must specify a command: $commandList")
            return
        }

        val commandName = args[0]
        val commandArgs = args.drop(1)

        val command = commandMap[commandName]
        if (command != null) {
            command.execute(commandArgs)
        } else {
            println("Unknown command: $commandName")
        }
    }
}
