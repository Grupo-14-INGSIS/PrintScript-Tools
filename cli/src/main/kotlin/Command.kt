package src.main.model.tools.cli.cli

interface Command {
    fun execute(args: List<String>)
}
