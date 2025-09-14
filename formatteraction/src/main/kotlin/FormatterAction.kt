package formatteraction.src.main.kotlin

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.Formatter
import lexer.src.main.kotlin.Lexer
import formatter.src.main.kotlin.formatrule.FormatRule
import progress.src.main.kotlin.MultiStepProgress

import java.io.File
import java.io.FileWriter
import java.net.URL

class FormatterAction {

    fun execute(args: List<String>) {
        if (args.size < 2) {
            println("Error: Must specify the source file and the format configuration file.")
            println("Usage: formatter <source_file> <configuration_file> [version]")
            return
        }

        val sourceFile = args[0]
        val configFile = args[1]
        val version = if (args.size > 2) args[2] else "1.0"

        if (version !in listOf("1.0", "1.1")) {
            println("Error: Unsupported version. Only 1.0 is admitted.")
            return
        }

        val sourceFileObj = File(sourceFile)
        val configFileObj: URL? = this::class.java.getResource(configFile)

        if (!sourceFileObj.exists()) {
            println("Error: The source file '$sourceFile' does not exist.")
            return
        }

        if (configFileObj == null) {
            println("Error: The configuration file '$configFile' does not exist.")
            return
        }

        val source = sourceFileObj.readText()
        println("Starting formatting of '$sourceFile' (PrintScript $version)")

        val progress = MultiStepProgress()
        progress.initialize(4)

        try {
            // Paso 1: Validar archivos
            val validateStep = progress.startStep("Validating input files")
            val sourceSize = (source.length / 1024.0).let { if (it < 1) "${source.length} bytes" else "%.1f KB".format(it) }
            validateStep.complete("Source file loaded ($sourceSize)")

            // Paso 2: Cargar configuración
            val configStep = progress.startStep("Loading formatting configuration")
            // Aquí cargarías la configuración del formatter
            configStep.complete("Configuration loaded from $configFile")

            // Paso 3: Formatear código
            val formatStep = progress.startStep("Applying formatting rules")
            val lexer = Lexer.from(source)
            lexer.split()
            var tokens: Container = lexer.createToken(lexer.list)
            val formatter = Formatter()
            var percentageCompleted: Int
            val rules: List<FormatRule> = formatter.loadRules(configFileObj)
            for (i in 0 until rules.size) {
                tokens = formatter.executeOne(tokens, rules[i])
                percentageCompleted = i * 100 / rules.size
                print(percentageCompleted)
                print("%")
                if (i < rules.size) print(".....")
            }
            println("100%")
            formatStep.complete("Formatting rules applied successfully")

            // Paso 4: Guardar resultado (opcional)
            val saveStep = progress.startStep("Preparing formatted output")
            val writer = FileWriter(sourceFile)
            for (i in 0 until tokens.size()) {
                writer.append(tokens.get(i)!!.content)
            }
            saveStep.complete("Formatted code ready")

            progress.complete()
            println("\nFormatted code:")
            println("=".repeat(50))
            for (i in 0 until tokens.size()) {
                print(tokens.get(i)!!.content)
            }
            println()
            println("=".repeat(50)) // no funciona el * como en python para repetri un string
        } catch (e: Exception) {
            println("Error during formatting: ${e.message}")
            e.printStackTrace()
        }
    }
}
