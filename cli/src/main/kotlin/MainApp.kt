import cli.src.main.kotlin.Cli

class RootCommand : Runnable {
    val cli = Cli()

    fun startInteractiveMode() {
        println("=== CLIPS - CLI PrintScript - Modo Interactivo ===")
        println("Comandos disponibles: ${cli.availableCommands().joinToString(" | ")}")
        println("Escribe 'exit' para salir\n")

        while (true) {
            println("CLIPS> ")
            val line = readlnOrNull() ?: break
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
