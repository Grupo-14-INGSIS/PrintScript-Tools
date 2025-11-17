package cli.src.main.kotlin

object Main {
    @JvmStatic // estatica para poder compilar y ejecutar directo de aca
    fun main(args: Array<String>) {
        val cli = Cli(
            mapOf(
                "formatter" to FormatterCommand(), // ./gradlew :mainApp:run --args="formatter test.ps format_rules.yaml 1.0"
                "analyzer" to AnalyzerCommand(),
                "execution" to ExecutionCommand(), // ./gradlew :mainApp:run --args="execution test.ps 1.0"
                "validation" to ValidationCommand() // ./gradlew :mainApp:run --args='validation test.ps 1.0
            )
        )
        cli.run(args)
    }
}
