package formatter.src.test.kotlin

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.Formatter
import lexer.src.main.kotlin.Lexer
import lexer.src.main.kotlin.StringCharSource
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.File

class LineBreakBeforePrintlnTest {

    @Test
    fun `test line-breaks-before-println inserts blank line before println`() {
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
          "line-breaks-before-println": 1
        }"""
        val configFile = File.createTempFile("test_config", ".json").apply {
            writeText(configContent)
            deleteOnExit()
        }

        val lexer = Lexer(StringCharSource(input))
        val statements = lexer.lexIntoStatements()

        var flatContainer = Container()
        for (statement in statements) {
            for (token in statement.container) {
                flatContainer = flatContainer.addContainer(token)
            }
        }

        val formatter = Formatter()
        val formattedTokens = formatter.execute(flatContainer, configFile.toURI().toURL())
        val result = containerToString(formattedTokens)

        println("\n=== RESULTADO FINAL ===")
        println("Expected:\n$expected")
        println("\nGot:\n$result")

        assertEquals(expected, result.trim())
    }

    @Test
    fun `test line-breaks-before-println deletes blank line before println`() {
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
          "line-breaks-before-println": 0
        }"""
        val configFile = File.createTempFile("test_config", ".json").apply {
            writeText(configContent)
            deleteOnExit()
        }

        val lexer = Lexer(StringCharSource(input))
        val statements = lexer.lexIntoStatements()

        var flatContainer = Container()
        for (statement in statements) {
            for (token in statement.container) {
                flatContainer = flatContainer.addContainer(token)
            }
        }

        val formatter = Formatter()
        val formattedTokens = formatter.execute(flatContainer, configFile.toURI().toURL())
        val result = containerToString(formattedTokens)

        println("\n=== RESULTADO FINAL ===")
        println("Expected:\n$expected")
        println("\nGot:\n$result")

        assertEquals(expected, result.trim())
    }

    private fun containerToString(container: Container): String {
        val result = StringBuilder()
        for (i in 0 until container.size()) {
            val token = container.get(i)
            if (token != null) {
                result.append(token.content)
            }
        }
        return result.toString()
    }
}
