package cli.src.main.kotlin

object Main {
    @JvmStatic // estatica para poder compilar y ejecutar directo de aca
    fun main(args: Array<String>) {
        val cli = Cli(
            mapOf(
                "formatter" to FormatterCommand(),
                "analyzer" to AnalyzerCommand(),
                "execution" to ExecutionCommand(),
                "validation" to ValidationCommand()
            )
        )
        cli.run(args)
    }
}
