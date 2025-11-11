package lexer.src.test.kotlin

import lexer.src.main.kotlin.Lexer
import lexer.src.main.kotlin.StringCharSource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tokendata.src.main.kotlin.DataType

class TokenMap11Test {

    private fun lex(text: String): Lexer {
        val lexer = Lexer(StringCharSource(text), "1.1")
        lexer.split()
        return lexer
    }

    @Test
    fun `test classify readInput`() {
        val lexer = lex("readInput")
        val container = lexer.createToken(lexer.list)
        assertEquals(DataType.READ_INPUT, container.container[0].type)
    }

    @Test
    fun `test classify braces`() {
        val lexer = lex("{}}")
        val container = lexer.createToken(lexer.list)
        assertEquals(DataType.OPEN_BRACE, container.container[0].type)
        assertEquals(DataType.CLOSE_BRACE, container.container[1].type)
    }

    @Test
    fun `test line break mapping`() {
        val lexer = lex("\n ")
        val container = lexer.createToken(lexer.list)
        assertEquals(2, container.container.size)
        assertEquals(DataType.LINE_BREAK, container.container[0].type)
    }
}
