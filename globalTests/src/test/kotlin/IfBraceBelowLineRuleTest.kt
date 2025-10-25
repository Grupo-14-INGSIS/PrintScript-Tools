import container.src.main.kotlin.Container
import formatter.src.main.kotlin.Formatter
import lexer.src.main.kotlin.Lexer
import lexer.src.main.kotlin.StringCharSource
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class IfBraceBelowLineRuleTest {

    @Test
    fun `test moves opening brace to next line when if-brace-below-line is true`() {
        val input = """
            let something: boolean = true;
            if (something) {
                println("Entered if");
            }
        """.trimIndent()

        val expected = """
            let something: boolean = true;
            if (something)
            {
                println("Entered if");
            }
        """.trimIndent()

        val configContent = """{
          "if-brace-below-line": true
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
        debugTokens(formattedTokens)
        val result = containerToString(formattedTokens)

        println("\n=== RESULTADO FINAL ===")
        println("Expected:\n$expected")
        println("\nGot:\n$result")

        Assertions.assertEquals(expected, result.trim())
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
    private fun debugTokens(container: Container) {
        println("\n=== DEBUG TOKENS ===")
        for (i in 0 until container.size()) {
            val token = container.get(i)
            if (token != null) {
                println("[$i] Type: ${token.type}, Content: '${token.content.replace("\n", "\\n")}'")
            }
        }
    }
}
