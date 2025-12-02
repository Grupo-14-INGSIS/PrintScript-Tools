package lexer.src.test.kotlin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import lexer.src.main.kotlin.*

class LexerUnitTest {

    private fun lex(text: String): List<String> {
        val lexer = Lexer(StringCharSource(text))
        return lexer.split().toList() // Convert sequence to list for assertions
    }

    @Test
    fun `split should handle simple input`() {
        val pieces = lex("let x")
        assertEquals(listOf("let", " ", "x"), pieces)
    }

    @Test
    fun `split should handle double quoted string`() {
        val pieces = lex("\"hello world\"")
        assertEquals(listOf("\"hello world\""), pieces)
    }

    @Test
    fun `split should handle single quoted string`() {
        val pieces = lex("'hello world'")
        assertEquals(listOf("'hello world'"), pieces)
    }

    @Test
    fun `split should handle arithmetic operators`() {
        val pieces = lex("x + y - z")
        assertEquals(listOf("x", " ", "+", " ", "y", " ", "-", " ", "z"), pieces)
    }

    @Test
    fun `split should handle assignment and semicolon`() {
        val pieces = lex("x = 5;")
        assertEquals(listOf("x", " ", "=", " ", "5", ";"), pieces)
    }

    @Test
    fun `split should handle colon for type declaration`() {
        val pieces = lex("x: number")
        assertEquals(listOf("x", ":", " ", "number"), pieces)
    }

    @Test
    fun `split should handle newline`() {
        val pieces = lex("let x;\nlet y;")
        assertEquals(listOf("let", " ", "x", ";", "\n", "let", " ", "y", ";"), pieces)
    }

    @Test
    fun `split should handle string with spaces`() {
        val pieces = lex("\"hello world with spaces\"")
        assertEquals(listOf("\"hello world with spaces\""), pieces)
    }

    @Test
    fun `split should handle mixed quotes`() {
        val pieces = lex("\"double\" 'single'")
        assertEquals(listOf("\"double\"", " ", "'single'"), pieces)
    }

    @Test
    fun `split should handle numbers and decimals`() {
        val pieces = lex("123 45.67")
        assertEquals(listOf("123", " ", "45.67"), pieces)
    }
}
