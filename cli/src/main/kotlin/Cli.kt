package cli.src.main.kotlin

class Cli(private val commands: Map<String, Command>) {

    fun run(args: Array<String>) {
        if (args.isEmpty()) {
            println("Must specify a command: formatter | analyzer | validation | execution")
            return
        }

        val commandName = args[0]
        val commandArgs = args.drop(1)

        val command = commands[commandName]
        if (command != null) {
            command.execute(commandArgs)
        } else {
            println("Unknown command: $commandName")
        }
    }
}
