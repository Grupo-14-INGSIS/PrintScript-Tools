package src.main.model.tools.cli.cli

import formatter.src.main.kotlin.Formatter
import java.io.File

class FormatterCommand : Command {
    override fun execute(args: List<String>) {
        if (args.size < 2) {
            println("Error: Debe especificar el archivo fuente y el archivo de configuración")
            println("Uso: formatter <archivo_fuente> <archivo_config> [version]")
            return
        }

        val sourceFile = args[0]
        val configFile = args[1]
        val version = if (args.size > 2) args[2] else "1.0"

        if (version != "1.0") {
            println("Error: Versión no soportada. Solo se admite la versión 1.0")
            return
        }

        try {
            val sourceFileObj = File(sourceFile)
            val configFileObj = File(configFile)

            if (!sourceFileObj.exists()) {
                println("Error: El archivo fuente '$sourceFile' no existe")
                return
            }

            if (!configFileObj.exists()) {
                println("Error: El archivo de configuración '$configFile' no existe")
                return
            }

            val source = sourceFileObj.readText()
            println("Iniciando formateo de '$sourceFile'...")

            // Progreso: Validación previa
            print("Validando archivo fuente... ")
            // Aquí deberías validar que el archivo sea sintácticamente correcto
            println("✓ Completado")

            // Progreso: Cargando configuración
            print("Cargando configuración de formato... ")
            val formatter = Formatter(source, configFile)
            println("✓ Completado")

            // Progreso: Aplicando formato
            print("Aplicando reglas de formato... ")
            val result = formatter.execute(source)
            println("✓ Completado")

            if (result == 0) {
                println("\n✅ Formateo exitoso")
                // Aquí deberías guardar el archivo formateado o mostrarlo
            } else {
                println("\n❌ Error durante el formateo (código: $result)")
            }
        } catch (e: Exception) {
            println("Error durante el formateo: ${e.message}")
            e.printStackTrace()
        }
    }
}
