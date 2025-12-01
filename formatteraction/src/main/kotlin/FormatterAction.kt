package formatteraction.src.main.kotlin

import formatter.src.main.kotlin.Formatter
import lexer.src.main.kotlin.Lexer
import progress.src.main.kotlin.MultiStepProgress

import java.io.File
import java.io.FileWriter

class FormatterAction {

    fun execute(args: List<String>) {
        if (args.size < 2) {
            println(
                "Error: Must specify the source file and the format configuration file."
            )
            println(
                "Usage: formatter <source_file> <configuration_file> [version]"
            )
            return
        }

        val sourceFile = args[0]
        val configFile = args[1]
        val version = if (args.size > 2) args[2] else "1.0"

        if (version !in listOf("1.0", "1.1")) {
            println("Error: Unsupported version. Only 1.0 and 1.1 are admitted.")
            return
        }

        val sourceFileObj = File(sourceFile)
        val configFileObj: File = File(configFile)

        if (!configFileObj.exists()) {
            println("Error: The configuration file '$configFile' does not exist.")
            return
        }

        if (!sourceFileObj.exists()) {
            println(
                "Error: The source file '$sourceFile' does not exist."
            )
            return
        }

        val source = sourceFileObj.readText()
        println(
            "Starting formatting of '$sourceFile' (PrintScript $version)"
        )

        val progress = MultiStepProgress()
        progress.initialize(4)

        try {
            val validateStep = progress.startStep("Validating input files")
            val sourceSize = (source.length / 1024.0).let {
                if (it < 1) {
                    "${source.length} bytes"
                } else {
                    "%.1f KB".format(it)
                }
            }
            validateStep.complete("Source file loaded ($sourceSize)")

            val configStep = progress.startStep(
                "Loading formatting configuration"
            )
            configStep.complete(
                "Configuration loaded from $configFile"
            )

            val lexerStep = progress.startStep("Lexing source file")
            val lexer = Lexer.from(source)
            val statements = lexer.lexIntoStatements()
            lexerStep.complete("Source file lexed into ${statements.size} statements")

            val formatStep = progress.startStep("Applying formatting rules")
            val formatter = Formatter()
            val formattedStatements = formatter.execute(statements, configFileObj)
            formatStep.complete(
                "Formatting rules applied successfully"
            )

            val saveStep = progress.startStep("Preparing formatted output")

            FileWriter(sourceFile).use { writer ->
                formattedStatements.forEach { container ->
                    container.container.forEach { token ->
                        writer.append(token.content)
                    }
                }
            }

            saveStep.complete("Formatted code ready")


            progress.complete()
        } catch (e: Exception) {
            println(
                "Error during formatting: ${e.message}"
            )
            e.printStackTrace()
        }
        return
    }
}
