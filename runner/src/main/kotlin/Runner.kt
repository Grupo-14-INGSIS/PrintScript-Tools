package runner.src.main.kotlin

import analyzer.src.main.kotlin.Analyzer
import executor.src.main.kotlin.Executor
import formatteraction.src.main.kotlin.FormatterAction
import inputprovider.src.main.kotlin.ConsoleInputProvider
import inputprovider.src.main.kotlin.InputProvider

class Runner {

    fun executionCommand(args: List<String>, inputProvider: InputProvider = ConsoleInputProvider()) {
        val executor = Executor(inputProvider)
        executor.execute(args)
    }

    fun analyzerCommand(args: List<String>) {
        val analyzer = Analyzer()
        analyzer.execute(args)
    }

    fun formatterCommand(args: List<String>) {
        val formatter = FormatterAction()
        formatter.execute(args)
    }

    fun validationCommand(args: List<String>) {
        val analyzer = Analyzer()
        analyzer.executeValidation(args)
    }
}
