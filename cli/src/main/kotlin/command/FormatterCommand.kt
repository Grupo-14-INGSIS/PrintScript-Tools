package cli.src.main.kotlin.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.types.file
import formatteraction.FormatterAction

class FormatterCommand(
    private val formatterAction: FormatterAction = FormatterAction()
) : CliCommand(name = "formatter", help = "Format a PrintScript source file according to rules") {

    val sourceFile by argument(name = "source_file", help = "Path to the source file to format")
        .file(canBeDir = false)

    val configFile by argument(name = "configuration_file", help = "Path to the format configuration file")
        .file(canBeDir = false)

    val version by argument(name = "version", help = "PrintScript version (1.0 or 1.1)")
        .default("1.0")

    override fun run() {
        formatterAction.execute(sourceFile, configFile, version)
    }

    override fun execute(args: List<String>) {
        if (args.size < 2) {
            println("Error: Must specify the source file and the format configuration file.")
            println("Usage: formatter <source_file> <configuration_file> [version]")
            return
        }
        super.execute(args)
    }
}
