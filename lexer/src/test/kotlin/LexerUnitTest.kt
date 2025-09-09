package lexer.src.test.kotlin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import lexer.src.main.kotlin.*

class LexerUnitTest {

    private fun lex(text: String): Lexer {
        val lexer = Lexer(StringCharSource(text))
        lexer.split()
        return lexer
    }

    @Test
    fun `split should handle simple input`() {
        val lexer = lex("let x")
        assertEquals(listOf("let", " ", "x"), lexer.list)
    }

    @Test
    fun `split should handle double quoted string`() {
        val lexer = lex("\"hello world\"")
        assertEquals(listOf("\"hello world\""), lexer.list)
    }

    @Test
    fun `split should handle single quoted string`() {
        val lexer = lex("'hello world'")
        assertEquals(listOf("'hello world'"), lexer.list)
    }

    @Test
    fun `split should handle arithmetic operators`() {
        val lexer = lex("x + y - z")
        assertEquals(listOf("x", " ", "+", " ", "y", " ", "-", " ", "z"), lexer.list)
    }

    @Test
    fun `split should handle assignment and semicolon`() {
        val lexer = lex("x = 5;")
        assertEquals(listOf("x", " ", "=", " ", "5", ";"), lexer.list)
    }

    @Test
    fun `split should handle colon for type declaration`() {
        val lexer = lex("x: number")
        assertEquals(listOf("x", ":", " ", "number"), lexer.list)
    }

    @Test
    fun `split should handle newline`() {
        val lexer = lex("let x;\nlet y;")
        assertEquals(listOf("let", " ", "x", ";", "\n", "let", " ", "y", ";"), lexer.list)
    }

    @Test
    fun `split should handle string with spaces`() {
        val lexer = lex("\"hello world with spaces\"")
        assertEquals(listOf("\"hello world with spaces\""), lexer.list)
    }

    @Test
    fun `split should handle mixed quotes`() {
        val lexer = lex("\"double\" 'single'")
        assertEquals(listOf("\"double\"", " ", "'single'"), lexer.list)
    }

    @Test
    fun `split should handle numbers and decimals`() {
        val lexer = lex("123 45.67")
        assertEquals(listOf("123", " ", "45.67"), lexer.list)
    }
}
