package cli.src.main.kotlin

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.core.PrintHelpMessage
import com.github.ajalt.clikt.core.subcommands
import cli.src.main.kotlin.command.AnalyzerCommand
import cli.src.main.kotlin.command.ExecutionCommand
import cli.src.main.kotlin.command.FormatterCommand
import cli.src.main.kotlin.command.ValidationCommand

/**
 * CLI extensible basada en Clikt con arquitectura de subcomandos.
 * Permite registrar comandos dinamicamente y despacharlos de forma agnostica.
 */
class Cli(
    initialCommands: List<CliktCommand> = listOf(
        FormatterCommand(),
        AnalyzerCommand(),
        ValidationCommand(),
        ExecutionCommand()
    )
) : CliktCommand(
    name = "clips",
    help = "CLIPS - PrintScript Command Line Interface"
) {
    private val commandMap = mutableMapOf<String, CliktCommand>()

    init {
        initialCommands.forEach { registerCommand(it) }
    }

    fun registerCommand(command: CliktCommand) {
        val key = if (command is cli.src.main.kotlin.command.CliCommand) command.name else command.commandName
        commandMap[key] = command
        subcommands(command)
    }

    fun availableCommands(): List<String> = commandMap.keys.toList()

    override fun run() = Unit

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
        val command = commandMap[commandName]
        if (command == null) {
            println("Unknown command: $commandName")
            return
        }

        if (command is cli.src.main.kotlin.command.CliCommand) {
            command.execute(args.drop(1))
        } else {
            try {
                parse(args)
            } catch (e: PrintHelpMessage) {
                println(command.getFormattedHelp())
            } catch (e: CliktError) {
                println(e.message ?: "Unknown error")
            }
        }
    }
}
