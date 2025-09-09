package src.main.model.tools.cli.cli

import MultiStepProgress
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

        if (version !in listOf("1.0", "1.1")) {
            println("Error: Unsupported version. Only 1.0 is admitted.")
            return
        }

        val sourceFileObj = File(sourceFile)
        val configFileObj = File(configFile)

        if (!sourceFileObj.exists()) {
            println("Error: The source file '$sourceFile' does not exist.")
            return
        }

        if (!configFileObj.exists()) {
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
            val formatter = Formatter(source, configFile)
            val result: Container = formatter.execute()
            formatStep.complete("Formatting rules applied successfully")

            // Paso 4: Guardar resultado (opcional)
            val saveStep = progress.startStep("Preparing formatted output")
            // Aquí podrías escribir el resultado a un archivo o mostrarlo
            saveStep.complete("Formatted code ready")

            progress.complete()
            println("\nFormatted code:")
            println("=".repeat(50))
            // Mostrar el resultado formateado
            println("=".repeat(50)) // no funciona el * como en python para repetri un string
        } catch (e: Exception) {
            println("Error during formatting: ${e.message}")
            e.printStackTrace()
        }
    }
}
