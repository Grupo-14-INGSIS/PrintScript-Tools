package runner.src.main.kotlin

import cli.src.main.kotlin.command.AnalyzerCommand
import cli.src.main.kotlin.command.ExecutionCommand
import cli.src.main.kotlin.command.FormatterCommand
import cli.src.main.kotlin.command.ValidationCommand
import inputprovider.src.main.kotlin.ConsoleInputProvider
import inputprovider.src.main.kotlin.InputProvider

class Runner {

    fun executionCommand(
        args: List<String>,
        inputProvider: InputProvider = ConsoleInputProvider(),
        printer: (Any?) -> Unit = ::println
    ) {
        ExecutionCommand(inputProvider, printer).execute(args)
    }

    fun analyzerCommand(args: List<String>) {
        AnalyzerCommand().execute(args)
    }

    fun formatterCommand(args: List<String>) {
        FormatterCommand().execute(args)
    }

    fun validationCommand(args: List<String>) {
        ValidationCommand().execute(args)
    }
}
