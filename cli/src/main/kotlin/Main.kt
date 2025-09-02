package src.main.model.tools.cli.cli

class Main {
    fun main(args: Array<String>) {
        val cli = Cli(
            mapOf(
                "formatter" to FormatterCommand(),
                "analyzer" to AnalyzerCommand(),
                "validation" to ValidationCommand(),
                "execution" to ExecutionCommand()
            )
        )
        cli.run(args)
    }
}
