package cli.src.main.kotlin

import analyzer.src.main.kotlin.Analyzer

class ValidationCommand : Command {
    override fun execute(args: List<String>) {
        val analyzer = Analyzer()
        analyzer.executeValidation(args)
    }
}
