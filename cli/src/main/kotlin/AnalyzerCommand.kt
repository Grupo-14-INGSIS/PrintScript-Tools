package cli.src.main.kotlin

import analyzer.src.main.kotlin.Analyzer

class AnalyzerCommand : Command {
    override fun execute(args: List<String>) {
        val analyzer = Analyzer()
        analyzer.execute(args)
    }
}
