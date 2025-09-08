package src.main.model.tools.cli.cli

object Main {
    @JvmStatic // estatica para poder compilar y ejecutar directo de aca
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
