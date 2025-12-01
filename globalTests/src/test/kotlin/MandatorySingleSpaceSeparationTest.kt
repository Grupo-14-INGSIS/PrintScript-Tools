package formatter.src.test.kotlin

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.Formatter
import lexer.src.main.kotlin.Lexer
import lexer.src.main.kotlin.StringCharSource
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.File

class MandatorySingleSpaceSeparationTest {

    @Test
    fun `test mandatory-single-space-separation normalizes all spacing`() {
        val input = """
            let something:      string="a really cool thing";
            println(something);
        """.trimIndent()

        val expected = """
            let something : string = "a really cool thing";
            println ( something );
        """.trimIndent()

        val configContent = """{
          "mandatory-single-space-separation": true
        }"""
        val configFile = File.createTempFile("test_config", ".json").apply {
            writeText(configContent)
            deleteOnExit()
        }

        val lexer = Lexer(StringCharSource(input))
        val statements = lexer.lexIntoStatements()

        println("=== TOKENS DESPUÉS DEL LEXER ===")
        statements.forEachIndexed { index, statement ->
            println("  Statement $index:")
            statement.container.forEach { token ->
                println("    Token: type=${token.type}, content='${token.content}', position=${token.position}")
            }
        }
        val formatter = Formatter()
        val formattedStatements = formatter.execute(statements, configFile)

        println("\n=== TOKENS DESPUÉS DEL FORMATTER ===")
        formattedStatements.forEachIndexed { index, statement ->
            println("  Statement $index:")
            statement.container.forEach { token ->
                println("    Token: type=${token.type}, content='${token.content}', position=${token.position}")
            }
        }

        val result = statementsToString(formattedStatements)

        println("\n=== RESULTADO FINAL ===")
        println("Expected:\n$expected")
        println("\nGot:\n$result")

        assertEquals(expected, result.trim())
    }

    private fun statementsToString(statements: List<Container>): String {
        return statements.joinToString(separator = "") { statement ->
            statement.container.joinToString(separator = "") { it.content }
        }
    }
}
