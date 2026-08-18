package lexer.src.test.kotlin

import lexer.src.main.kotlin.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

class TokenPluginTest {

    @Test
    fun `test ExactMatchTokenPlugin matches keywords and symbols`() {
        val plugin = ExactMatchTokenPlugin(
            mapOf(
                "let" to DataType.LET_KEYWORD,
                "=" to DataType.ASSIGNATION
            )
        )

        val tokenLet = plugin.match("let", Position(0, 0))
        assertNotNull(tokenLet)
        assertEquals(DataType.LET_KEYWORD, tokenLet?.type)
        assertEquals("let", tokenLet?.content)

        val tokenAssign = plugin.match("=", Position(0, 4))
        assertNotNull(tokenAssign)
        assertEquals(DataType.ASSIGNATION, tokenAssign?.type)

        assertNull(plugin.match("unknown", Position(0, 0)))
    }

    @Test
    fun `test RegexTokenPlugin matches numbers and strings`() {
        val numberPlugin = RegexTokenPlugin(Regex("^[0-9]+(\\.[0-9]+)?\$"), DataType.NUMBER_LITERAL)
        val stringPlugin = RegexTokenPlugin(Regex("^[\"'].*[\"']\$"), DataType.STRING_LITERAL)

        val numberToken = numberPlugin.match("123.45", Position(1, 0))
        assertNotNull(numberToken)
        assertEquals(DataType.NUMBER_LITERAL, numberToken?.type)
        assertEquals("123.45", numberToken?.content)

        val stringToken = stringPlugin.match("\"hello\"", Position(1, 0))
        assertNotNull(stringToken)
        assertEquals(DataType.STRING_LITERAL, stringToken?.type)
        assertEquals("\"hello\"", stringToken?.content)

        assertNull(numberPlugin.match("notANumber", Position(0, 0)))
        assertNull(stringPlugin.match("unclosed", Position(0, 0)))
    }

    @Test
    fun `test custom TokenPlugin injection into Lexer`() {
        // Plugin personalizado para reconocer una palabra clave personalizada "customKeyword"
        val customPlugin = object : TokenPlugin {
            override fun match(piece: String, position: Position): Token? {
                return if (piece == "customKeyword") {
                    Token(DataType.IDENTIFIER, "CUSTOM_MATCH", position)
                } else {
                    null
                }
            }
        }

        val plugins = listOf(customPlugin) + TokenPluginFactory.createPlugins("1.0")
        val lexer = Lexer(StringCharSource("customKeyword = 10;"), plugins)

        val container = lexer.lexIntoStatements().first()
        assertEquals("CUSTOM_MATCH", container.container[0].content)
    }

    @Test
    fun `test TokenPluginFactory returns appropriate plugins for v10 and v11`() {
        val pluginsV10 = TokenPluginFactory.createPlugins("1.0")
        val pluginsV11 = TokenPluginFactory.createPlugins("1.1")

        val exactMatchV10 = pluginsV10.filterIsInstance<ExactMatchTokenPlugin>().first()
        val exactMatchV11 = pluginsV11.filterIsInstance<ExactMatchTokenPlugin>().first()

        assertNotNull(exactMatchV10.match("let", Position(0, 0)))
        assertNull(exactMatchV10.match("const", Position(0, 0)))
        assertNull(exactMatchV10.match("if", Position(0, 0)))

        assertNotNull(exactMatchV11.match("let", Position(0, 0)))
        assertNotNull(exactMatchV11.match("const", Position(0, 0)))
        assertNotNull(exactMatchV11.match("if", Position(0, 0)))
        assertNotNull(exactMatchV11.match("else", Position(0, 0)))
        assertNotNull(exactMatchV11.match("readInput", Position(0, 0)))
    }
}
