package cli.src.main.kotlin.command

import analyzer.src.main.kotlin.Analyzer

class ValidationCommand(
    private val analyzer: Analyzer = Analyzer()
) : CliCommand {
    override val name: String = "validation"
    override val description: String = "Validate syntax and semantics without linting"
    override fun execute(args: List<String>) = analyzer.executeValidation(args)
}
