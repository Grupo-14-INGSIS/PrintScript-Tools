
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import model.tools.interpreter.Lexer

class LexerUnitTest {

    @Test
    fun `test splitString with simple input`() {
        val lexer = Lexer("let x")
        lexer.splitString()

        assertEquals(listOf("let", " ", "x"), lexer.list)
    }

    @Test
    fun `test splitString with double quoted string`() {
        val lexer = Lexer("\"hello world\"")
        lexer.splitString()

        assertEquals(listOf("\"hello world\""), lexer.list)
    }

    @Test
    fun `test splitString with single quoted string`() {
        val lexer = Lexer("'hello world'")
        lexer.splitString()

        assertEquals(listOf("'hello world'"), lexer.list)
    }

    @Test
    fun `test splitString with arithmetic operators`() {
        val lexer = Lexer("(x + y) - z")
        lexer.splitString()

        assertEquals(listOf("(","x", " ", "+", " ", "y",")", " ", "-", " ", "z"), lexer.list)
    }

    @Test
    fun `test splitString with assignment and semicolon`() {
        val lexer = Lexer("x = 5;")
        lexer.splitString()

        assertEquals(listOf("x", " ", "=", " ", "5", ";"), lexer.list)
    }

    @Test
    fun `test splitString with colon for type declaration`() {
        val lexer = Lexer("x: number")
        lexer.splitString()

        assertEquals(listOf("x", ":", " ", "number"), lexer.list)
    }

    @Test
    fun `test splitString with newline`() {
        val lexer = Lexer("let x;\nlet y;")
        lexer.splitString()

        assertEquals(listOf("let", " ", "x", ";", "\n", "let", " ", "y", ";"), lexer.list)
    }

    @Test
    fun `test splitString with string containing spaces`() {
        val lexer = Lexer("\"hello world with spaces\"")
        lexer.splitString()

        assertEquals(listOf("\"hello world with spaces\""), lexer.list)
    }

    @Test
    fun `test splitString with mixed quotes`() {
        val lexer = Lexer("\"double\" 'single'")
        lexer.splitString()

        assertEquals(listOf("\"double\"", " ", "'single'"), lexer.list)
    }

    @Test
    fun `test splitString with numbers and decimals`() {
        val lexer = model.tools.interpreter.Lexer("123 45.67")
        lexer.splitString()

        assertEquals(listOf("123", " ", "45.67"), lexer.list)
    }
}





