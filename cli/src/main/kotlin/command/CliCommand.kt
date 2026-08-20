package cli.src.main.kotlin.command

/**
 * Abstracción base para comandos CLI extensibles (Command Pattern / Plugins de CLI).
 * Permite registrar nuevos comandos en la CLI sin modificar su código base (OCP).
 */
interface CliCommand {
    val name: String
    val description: String
    fun execute(args: List<String>)
}
