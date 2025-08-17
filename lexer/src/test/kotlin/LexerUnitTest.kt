
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import src.main.model.tools.interpreter.lexer.Lexer

class LexerUnitTest {

    @Test
    fun `test splitString with simple input`() {
        val lexer = Lexer("let x")
        lexer.splitString()

        assertEquals(listOf("let", " ", "x"), lexer.pieces)
    }

    @Test
    fun `test splitString with double quoted string`() {
        val lexer = Lexer("\"hello world\"")
        lexer.splitString()

        assertEquals(listOf("\"hello world\""), lexer.pieces)
    }

    @Test
    fun `test splitString with single quoted string`() {
        val lexer = Lexer("'hello world'")
        lexer.splitString()

        assertEquals(listOf("'hello world'"), lexer.pieces)
    }

    @Test
    fun `test splitString with arithmetic operators`() {
        val lexer = Lexer("x + y - z")
        lexer.splitString()

        assertEquals(listOf("x", " ", "+", " ", "y", " ", "-", " ", "z"), lexer.pieces)
    }

    @Test
    fun `test splitString with assignment and semicolon`() {
        val lexer = Lexer("x = 5;")
        lexer.splitString()

        assertEquals(listOf("x", " ", "=", " ", "5", ";"), lexer.pieces)
    }

    @Test
    fun `test splitString with colon for type declaration`() {
        val lexer = Lexer("x: number")
        lexer.splitString()

        assertEquals(listOf("x", ":", " ", "number"), lexer.pieces)
    }

    @Test
    fun `test splitString with newline`() {
        val lexer = Lexer("let x;\nlet y;")
        lexer.splitString()

        assertEquals(listOf("let", " ", "x", ";", "\n", "let", " ", "y", ";"), lexer.pieces)
    }

    @Test
    fun `test splitString with string containing spaces`() {
        val lexer = Lexer("\"hello world with spaces\"")
        lexer.splitString()

        assertEquals(listOf("\"hello world with spaces\""), lexer.pieces)
    }

    @Test
    fun `test splitString with mixed quotes`() {
        val lexer = Lexer("\"double\" 'single'")
        lexer.splitString()

        assertEquals(listOf("\"double\"", " ", "'single'"), lexer.pieces)
    }

    @Test
    fun `test splitString with numbers and decimals`() {
        val lexer = Lexer("123 45.67")
        lexer.splitString()

        assertEquals(listOf("123", " ", "45.67"), lexer.pieces)
    }
}





