package src.main.model.tools.cli.cli

import linter.Linter
import linter.LintRule
import src.main.model.tools.interpreter.lexer.Lexer
import parser.src.main.kotlin.Parser
import java.io.File

class AnalyzerCommand : Command {
    override fun execute(args: List<String>) {
        if (args.size < 2) {
            println("Error: Debe especificar el archivo fuente y el archivo de configuración de análisis")
            println("Uso: analyzer <archivo_fuente> <archivo_config_lint> [version]")
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
            println("Iniciando análisis de '$sourceFile'...")

            // Progreso: Análisis léxico y sintáctico
            print("Construyendo AST... ")
            val lexer = Lexer(source)
            lexer.splitString()
            val tokens = lexer.createToken(lexer.list)
            val parser = Parser(tokens)
            val ast = parser.parse()
            println("✓ Completado")

            // Progreso: Cargando reglas de linting
            print("Cargando reglas de análisis... ")
            val lintRules = loadLintRules(configFile)
            val linter = Linter(lintRules)
            println("✓ Completado")

            // Progreso: Ejecutando análisis
            print("Ejecutando análisis de código... ")
            val lintErrors = linter.all(ast)
            println("✓ Completado")

            if (lintErrors.isEmpty()) {
                println("\n✅ Análisis exitoso: No se encontraron problemas de código")
            } else {
                println("\n⚠️ Se encontraron ${lintErrors.size} problema(s) de código:")
                lintErrors.forEach { error -> println(error) }
            }
        } catch (e: Exception) {
            println("Error durante el análisis: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun loadLintRules(configFile: String): List<LintRule> {
        val loader = linter.config.ConfigLoader()
        val yamlMap = loader.loadYaml(configFile)

        val factory = linter.config.ConfigFactory()
        val config = factory.createConfig(yamlMap)

        val rules = mutableListOf<LintRule>()

        config.rules.identifierNaming?.let {
            rules += linter.rules.IdentifierNamingRule(it.style)
        }

        config.rules.printlnArg?.let {
            rules += linter.rules.PrintLnRule(it.enabled)
        }

        return rules
    }
}
