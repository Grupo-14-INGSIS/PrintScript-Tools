package cli.src.main.kotlin.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.core.PrintHelpMessage

/**
 * Abstraccion base para comandos CLI extensibles basada en CliktCommand.
 * Permite registrar nuevos comandos en la CLI sin modificar su codigo base (OCP).
 */
abstract class CliCommand(
    name: String? = null,
    open val help: String = ""
) : CliktCommand(name = name, help = help) {
    open val name: String get() = commandName
    open val description: String get() = help

    override fun run() = Unit

    open fun execute(args: List<String>) {
        if (args.isEmpty()) {
            println("Must specify the required arguments.")
            return
        }
        try {
            parse(args)
        } catch (e: PrintHelpMessage) {
            println(getFormattedHelp())
        } catch (e: CliktError) {
            println(e.message ?: "Error executing command")
        }
    }
}
