package formatter.src.test.kotlin

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.Formatter
import lexer.src.main.kotlin.Lexer
import lexer.src.main.kotlin.StringCharSource
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.File

class LineBreakAfterPrintlnTest {

    @Test
    fun `test line-breaks-after-println inserts blank line before second println`() {
        val input = """
            let something:string = "a really cool thing";
            println(something);
            println("in the way she moves");
        """.trimIndent()

        val expected = """
            let something:string = "a really cool thing";
            println(something);

            println("in the way she moves");
        """.trimIndent()

        val configContent = """{
          "line-breaks-after-println": 1
        }"""
        val configFile = File.createTempFile("test_config", ".json").apply {
            writeText(configContent)
            deleteOnExit()
        }

        val lexer = Lexer(StringCharSource(input))
        val statements = lexer.lexIntoStatements().toList()

        val formatter = Formatter()
        val formattedStatements = formatter.execute(statements, configFile)
        val result = statementsToString(formattedStatements)

        println("\n=== RESULTADO FINAL ===")
        println("Expected:\n$expected")
        println("\nGot:\n$result")

        assertEquals(expected, result.trim())
    }

    @Test
    fun `test line-breaks-after-println deletes blank line before second println`() {
        val input = """
            let something:string = "a really cool thing";
            println(something);

            println("in the way she moves");
        """.trimIndent()

        val expected = """
            let something:string = "a really cool thing";
            println(something);
            println("in the way she moves");
        """.trimIndent()

        val configContent = """{
          "line-breaks-after-println": 0
        }"""
        val configFile = File.createTempFile("test_config", ".json").apply {
            writeText(configContent)
            deleteOnExit()
        }

        val lexer = Lexer(StringCharSource(input))
        val statements = lexer.lexIntoStatements().toList()

        val formatter = Formatter()
        val formattedStatements = formatter.execute(statements, configFile)
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
