package src.main.model.tools.cli.cli

class Cli(private val commands: Map<String, Command>) {

    fun run(args: Array<String>) {
        if (args.isEmpty()) {
            println("Debe especificar un comando: formatter | analyzer | validation | execution")
            return
        }

        val commandName = args[0]
        val commandArgs = args.drop(1)

        val command = commands[commandName]
        if (command != null) {
            command.execute(commandArgs)
        } else {
            println("Comando desconocido: $commandName")
        }
    }
}
