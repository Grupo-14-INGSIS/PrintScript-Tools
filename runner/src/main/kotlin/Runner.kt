package org.example

import analyzer.src.main.kotlin.Analyzer
import executor.src.main.kotlin.Executor
import formatteraction.src.main.kotlin.FormatterAction

class Runner {

    fun executionCommand(args: List<String>) {
        val executor = Executor()
        executor.execute(args)
    }

    fun analyzerCommand(args: List<String>) {
        val analyzer = Analyzer()
        analyzer.execute(args)
    }

    fun formatterCommand(args: List<String>){
        val formatter = FormatterAction()
        formatter.execute(args)
    }

    fun validationCommand(args: List<String>){
        val analyzer = Analyzer()
        analyzer.executeValidation(args)
    }
}
