import cli.src.main.kotlin.Cli

class printTest {

    @Test
    fun printTest() {
        val cli = Cli()
        cli.run(listOf("execution", "test3.ps"))
    }

}
