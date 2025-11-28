package formatteraction.src.main.kotlin


import formatter.src.main.kotlin.Formatter
import lexer.src.main.kotlin.Lexer

import progress.src.main.kotlin.MultiStepProgress

import java.io.File
import java.io.FileWriter
import java.net.URL

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
            println(
                "Error: Unsupported version. Only 1.0 is admitted."
            )
            return
        }

        val sourceFileObj = File(sourceFile)
        val configFileObj: URL? = this::class.java.getResource(
            configFile
        )

        if (!sourceFileObj.exists()) {
            println(
                "Error: The source file '$sourceFile' does not exist."
            )
            return
        }

        if (configFileObj == null) {
            println(
                "Error: The configuration file '$configFile' does not exist."
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

            val lexerStep = progress.startStep("Lexing source file")
            val lexer = Lexer.from(source)
            val statements = lexer.lexIntoStatements()
            lexerStep.complete("Source file lexed into ${statements.size} statements")

            val formatStep = progress.startStep("Applying formatting rules")
            val formatter = Formatter()
            val formattedStatements = formatter.execute(statements, configFileObj)
            formatStep.complete("Formatting rules applied successfully")

            val saveStep = progress.startStep("Saving formatted output")
            val writer = FileWriter(sourceFile)
            for (statement in formattedStatements) {
                for (i in 0 until statement.size()) {
                    writer.append(statement.get(i)!!.content)
                }
            }
            writer.close() // Important: close the writer
            saveStep.complete("Formatted code saved to $sourceFile")

            progress.complete()
            println("\nSUCCESS: '$sourceFile' has been formatted.")
        } catch (e: Exception) {
            println(
                "Error during formatting: ${e.message}"
            )
            e.printStackTrace()
        }
        return
    }
}
