package cli.src.main.kotlin.command

import analyzer.src.main.kotlin.Analyzer

class AnalyzerCommand(
    private val analyzer: Analyzer = Analyzer()
) : CliCommand {
    override val name: String = "analyzer"
    override val description: String = "Perform lexical, syntactic and static linting analysis"
    override fun execute(args: List<String>) = analyzer.execute(args)
}
