package formatter.src.test.kotlin

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.Formatter
import lexer.src.main.kotlin.Lexer
import lexer.src.main.kotlin.StringCharSource
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.File

class MandatorySingleSpaceSeparationTEst {

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

        // 1. Lexer: split y tokenización
        val lexer = Lexer(StringCharSource(input))
        lexer.split()
        val tokens = lexer.createToken(lexer.list)

        println("=== TOKENS DESPUÉS DEL LEXER ===")
        for (i in 0 until tokens.size()) {
            val token = tokens.get(i)
            if (token != null) {
                println("Token $i: type=${token.type}, content='${token.content}', position=${token.position}")
            }
        }

        // 2. Formatter
        val formatter = Formatter()
        val formattedTokens = formatter.execute(tokens, configFile.toURI().toURL())

        println("\n=== TOKENS DESPUÉS DEL FORMATTER ===")
        for (i in 0 until formattedTokens.size()) {
            val token = formattedTokens.get(i)
            if (token != null) {
                println("Token $i: type=${token.type}, content='${token.content}', position=${token.position}")
            }
        }

        // 3. Reconstrucción
        val result = containerToString(formattedTokens)

        println("\n=== RESULTADO FINAL ===")
        println("Expected:\n$expected")
        println("\nGot:\n$result")

        // 4. Validación
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
