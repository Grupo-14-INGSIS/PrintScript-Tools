import cli.src.main.kotlin.AnalyzerCommand
import org.jline.reader.LineReaderBuilder
import org.jline.terminal.TerminalBuilder
import picocli.CommandLine.Command
import cli.src.main.kotlin.Cli
import cli.src.main.kotlin.ExecutionCommand
import cli.src.main.kotlin.FormatterCommand
import cli.src.main.kotlin.ValidationCommand

// Guardar cambios con ./gradlew :mainApp:installDist

// Ejecutar con .\mainApp\build\install\mainApp\bin\mainApp.bat


@Command(
    name = "CLIPS",
    // Esto crea automaticamente los comandos --help y --version
    mixinStandardHelpOptions = true,
    description = ["CLI principal de PrintScript - Grupo 14"]
)
class RootCommand : Runnable {
    val cli = Cli(
        mapOf(
            "formatter" to FormatterCommand(),
            "analyzer" to AnalyzerCommand(),
            "validation" to ValidationCommand(),
            "execution" to ExecutionCommand()
        )
    )

    fun startInteractiveMode() {
        val terminal = TerminalBuilder.builder()
            .system(true)
            .build()

        val reader = LineReaderBuilder.builder()
            .terminal(terminal)
            .build()

        println("=== CLIPS - CLI PrintScript - Modo Interactivo ===")
        println("Comandos disponibles: formatter | analyzer | validation | execution")
        println("Escribe 'exit' para salir\n")

        while (true) {
            val line = try {
                reader.readLine("CLIPS> ")
            } catch (e: Exception) {
                break
            }

            val trimmedLine = line.trim()

            // Comando para salir
            if (trimmedLine.equals("exit", ignoreCase = true)) {
                break
            }

            // Saltar líneas vacías
            if (trimmedLine.isBlank()) {
                continue
            }

            val args = trimmedLine.split(" ").filter { it.isNotBlank() }

            try {
                // CLI usa Array<String>, no List<String>
                cli.run(args.toTypedArray())
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
            app.cli.run(args)
        } catch (e: Exception) {
            System.err.println("Error: ${e.message}")
            System.exit(1)
        }
        System.exit(0)
    } else {
        // Modo interactivo REPL
        app.startInteractiveMode()
    }
}

