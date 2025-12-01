package cli.src.main.kotlin

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val cli = Cli()
        cli.run(args.toList())
    }
}
