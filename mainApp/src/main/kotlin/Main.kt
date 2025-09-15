import org.jline.reader.LineReaderBuilder
import org.jline.reader.impl.DefaultParser
import org.jline.terminal.TerminalBuilder
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.shell.jline3.PicocliCommands
import picocli.shell.jline3.PicocliJLineCompleter

@Command(
    name = "miapp",
    mixinStandardHelpOptions = true,
    description = ["CLI principal de mi multi-proyecto"]
)
class RootCommand : Runnable {
    override fun run() {
        println("Usa --help para ver subcomandos.")
    }
}

fun interactive(cmd: CommandLine) {
    val terminal = TerminalBuilder.builder()
        .system(true)
        .build()

    // Registrar comandos de picocli
    val picocliCommands = PicocliCommands(cmd)

    // Autocompletado de picocli
    val completer = PicocliJLineCompleter(
        cmd.commandSpec
    )

    val parser = DefaultParser() // Tokenizador de argumentos

    val reader = LineReaderBuilder.builder()
        .terminal(terminal)
        .completer(completer)
        .parser(parser)
        .build()

    // Bucle interactivo estilo REPL
    while (true) {
        val line = try {
            reader.readLine("miapp> ")
        } catch (e: Exception) {
            break // si se cierra con Ctrl+D
        }

        if (line.trim() == "exit") break
        if (line.isBlank()) continue

        try {
            val argv = parser.parse(line, 0).words().toTypedArray()
            cmd.execute(*argv)
        } catch (ex: Exception) {
            println("Error: ${ex.message}")
        }
    }
}

fun main(args: Array<String>) {
    val root = RootCommand()
    val cmd = CommandLine(root)

    // Subcomandos
    cmd.addSubcommand("hello", HelloCommand())

    if (args.isNotEmpty()) {
        // Modo normal: ejecutar con args
        val exitCode = cmd.execute(*args)
        System.exit(exitCode)
    } else {
        // Modo interactivo REPL
        interactive(cmd)
    }
}

