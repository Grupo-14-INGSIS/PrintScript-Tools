package src.main.model.tools.cli.cli

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.Formatter
import formatter.src.main.kotlin.FormatterSetup
import formatter.src.main.kotlin.formatrule.FormatRule
import java.io.File
import java.io.FileWriter
import java.net.URL

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
            val configFileObj: URL? = this::class.java.getResource(configFile)

            // Check both source file and config
            if (!sourceFileObj.exists()) {
                println("Error: The source file '$sourceFile' does not exist.")
                return
            }

            if (configFileObj == null) {
                println("Error: The configuration file '$configFile' does not exist.")
                return
            }

            val source = sourceFileObj.readText()
            println("Starting formatting of '$sourceFile'.")

            // LINTER

            println("Executing code format")
            val formatter = Formatter(source, configFileObj)
            val setup: FormatterSetup = formatter.setup()
            var tokens: Container = setup.tokens
            val rules: List<FormatRule> = setup.rules

            var percentageCompleted: Int

            for (i in 0..rules.size) {
                tokens = formatter.execute(tokens, rules[i])
                percentageCompleted = i / rules.size
                print(percentageCompleted)
                print("%")
                if (i != rules.size) print(".....")
            }
            println()

            // Write to file
            val writer = FileWriter(sourceFile)
            for (i in 0..tokens.size()) {
                writer.append(tokens.get(i)!!.content)
            }

            println("Finished formatting.")
        } catch (e: Exception) {
            println("Error during formatting: ${e.message}")
            e.printStackTrace()
        }
    }
}
