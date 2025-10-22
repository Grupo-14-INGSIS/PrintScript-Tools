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
        lexer.split()
        val tokens = lexer.createToken(lexer.list)

        val formatter = Formatter()
        val formattedTokens = formatter.execute(tokens, configFile.toURI().toURL())
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
