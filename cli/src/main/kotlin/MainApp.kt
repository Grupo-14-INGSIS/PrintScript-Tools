import org.jline.reader.LineReaderBuilder
import org.jline.terminal.TerminalBuilder
import picocli.CommandLine.Command
import cli.src.main.kotlin.Cli

@Command(
    name = "CLIPS",
    mixinStandardHelpOptions = true,
    description = ["CLI principal de PrintScript - Grupo 14"]
)
class RootCommand : Runnable {
    val cli = Cli()

    fun startInteractiveMode() {
        val terminal = TerminalBuilder.builder()
            .system(true)
            .build()

        val reader = LineReaderBuilder.builder()
            .terminal(terminal)
            .build()

        println("=== CLIPS - CLI PrintScript - Modo Interactivo ===")
        println("Comandos disponibles: ${cli.availableCommands().joinToString(" | ")}")
        println("Escribe 'exit' para salir\n")

        while (true) {
            val line = try {
                reader.readLine("CLIPS> ")
            } catch (e: Exception) {
                break
            }

            val trimmedLine = line.trim()

            if (trimmedLine.equals("exit", ignoreCase = true)) {
                break
            }

            if (trimmedLine.isBlank()) {
                continue
            }

            val args = trimmedLine.split(" ").filter { it.isNotBlank() }

            try {
                cli.run(args)
            } catch (e: Exception) {
                System.err.println("Error: ${e.message}")
            }
        }

        terminal.close()
    }

    override fun run() {
        startInteractiveMode()
    }
}

fun main(args: Array<String>) {
    val app = RootCommand()

    if (args.isNotEmpty()) {
        try {
            app.cli.run(args.toList())
        } catch (e: Exception) {
            System.err.println("Error: ${e.message}")
            System.exit(1)
        }
        System.exit(0)
    } else {
        app.startInteractiveMode()
    }
}
