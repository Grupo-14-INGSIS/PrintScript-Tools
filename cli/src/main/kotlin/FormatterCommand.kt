package src.main.model.tools.cli.cli

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.Formatter
import java.io.File

class FormatterCommand : Command {

    override fun execute(args: List<String>) {
        if (args.size < 2) {
            println("Error: Must specify the source file and the format configuration file.")
            println("Usage: formatter <source_file> <configuration_file> [version]")
            return
        }

        val sourceFile = args[0]
        val configFile = args[1]
        val version = if (args.size > 2) args[2] else "1.0"

        if (version != "1.0") {
            println("Error: Unsupported version. Only 1.0 is admitted.")
            return
        }

        try {
            val sourceFileObj = File(sourceFile)
            val configFileObj = File(configFile)

            // Check both source file and config
            if (!sourceFileObj.exists()) {
                println("Error: The source file '$sourceFile' does not exist.")
                return
            }

            if (!configFileObj.exists()) {
                println("Error: The configuration file '$configFile' does not exist.")
                return
            }

            val source = sourceFileObj.readText()
            println("Starting formatting of '$sourceFile'.")

            // LINTER

            println("Executing code format")
            val formatter = Formatter(source, configFile)
            val result: Container = formatter.execute()

            // Write to file

            println("Finished formatting.")
        } catch (e: Exception) {
            println("Error during formatting: ${e.message}")
            e.printStackTrace()
        }
    }
}
