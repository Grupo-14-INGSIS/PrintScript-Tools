package cli.src.main.kotlin.command

import analyzer.Analyzer
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.types.file

class ValidationCommand(
    private val analyzer: Analyzer = Analyzer()
) : CliCommand(name = "validation", help = "Validate syntax and semantics without linting") {

    val sourceFile by argument(name = "source_file", help = "Path to the source file to validate")
        .file(canBeDir = false)

    val version by argument(name = "version", help = "PrintScript version (1.0 or 1.1)")
        .default("1.0")

    override fun run() {
        analyzer.executeValidation(sourceFile, version)
    }

    override fun execute(args: List<String>) {
        if (args.isEmpty()) {
            println("Must specify the source file.")
            return
        }
        super.execute(args)
    }
}
