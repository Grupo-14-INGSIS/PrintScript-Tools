package cli.src.main.kotlin.command

import analyzer.Analyzer
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.types.file

class AnalyzerCommand(
    private val analyzer: Analyzer = Analyzer()
) : CliCommand(name = "analyzer", help = "Perform lexical, syntactic and static linting analysis") {

    val sourceFile by argument(name = "source_file", help = "Path to the source file")
        .file(canBeDir = false)

    val configFile by argument(name = "configuration_file", help = "Path to the analysis rules configuration file")
        .file(canBeDir = false)

    val version by argument(name = "version", help = "PrintScript version (1.0 or 1.1)")
        .default("1.0")

    override fun run() {
        analyzer.execute(sourceFile, configFile, version)
    }

    override fun execute(args: List<String>) {
        if (args.size < 2) {
            println("Error: Must specify the source file and the analysis configuration file.")
            println("Usage: analyzer <source_file> <configuration_file> [version]")
            return
        }
        super.execute(args)
    }
}
