package src.test.model.tools.cli.cli

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import src.main.model.tools.cli.cli.AnalyzerCommand
import src.main.model.tools.cli.cli.Cli
import src.main.model.tools.cli.cli.ExecutionCommand
import src.main.model.tools.cli.cli.FormatterCommand
import src.main.model.tools.cli.cli.Main
import src.main.model.tools.cli.cli.ValidationCommand

class MainTest {
    @Test
    fun `should create main without exceptions`() {
        assertDoesNotThrow {
            val main = Main()
            assertNotNull(main)
        }
    }

    @Test
    fun `should create CLI with all commands`() {
        assertDoesNotThrow {
            val commands = mapOf(
                "formatter" to FormatterCommand(),
                "analyzer" to AnalyzerCommand(),
                "validation" to ValidationCommand(),
                "execution" to ExecutionCommand()
            )
            val cli = Cli(commands)
            assertNotNull(cli)
        }
    }
}
