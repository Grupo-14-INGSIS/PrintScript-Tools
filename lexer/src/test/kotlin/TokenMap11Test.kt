package lexer.src.test.kotlin

import container.src.main.kotlin.Container
import lexer.src.main.kotlin.Lexer
import lexer.src.main.kotlin.StringCharSource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tokendata.src.main.kotlin.DataType

class TokenMap11Test {

    private fun lexToContainer(text: String): Container {
        val lexer = Lexer(StringCharSource(text), "1.1")
        return lexer.lexIntoStatements().first()
    }

    @Test
    fun `test classify readInput`() {
        val container = lexToContainer("readInput")
        assertEquals(DataType.READ_INPUT, container.container[0].type)
    }

    @Test
    fun `test classify braces`() {
        val container = lexToContainer("{}}")
        assertEquals(DataType.OPEN_BRACE, container.container[0].type)
        assertEquals(DataType.CLOSE_BRACE, container.container[1].type)
    }

    @Test
    fun `test line break mapping`() {
        val container = lexToContainer("\n ")
        assertEquals(2, container.container.size)
        assertEquals(DataType.LINE_BREAK, container.container[0].type)
    }
}
