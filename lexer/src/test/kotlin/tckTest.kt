package lexer.src.test.kotlin

import lexer.src.main.kotlin.Lexer
import lexer.src.main.kotlin.StringCharSource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tokendata.src.main.kotlin.DataType

class tckTest {

    private fun lex(text: String): Lexer {
        val lexer = Lexer(StringCharSource(text), "1.1")
        lexer.split()
        return lexer
    }

    @Test
    fun `test classify keywords`() {
        val lexer = lex("readInput")
        val container = lexer.createToken(lexer.list)
        assertEquals(DataType.READ_INPUT, container.container[0].type)
    }
}
