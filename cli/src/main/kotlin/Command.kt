package cli.src.main.kotlin

interface Command {
    fun execute(args: List<String>)
}
