package cli.src.main.kotlin.command

/**
 * Abstraccion base para comandos CLI extensibles (Command Pattern / Plugins de CLI).
 * Permite registrar nuevos comandos en la CLI sin modificar su codigo base (OCP).
 */
interface CliCommand {
    val name: String
    val description: String
    fun execute(args: List<String>)
}
