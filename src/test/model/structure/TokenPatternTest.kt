package src.test.model.structure

import src.main.model.structure.DataType
import src.main.model.structure.TokenPattern
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class TokenPatternTest {

    @Test
    fun `test classify string literals with double quotes`() {
        assertEquals(DataType.STRING_LITERAL, TokenPattern.classifyTokenPattern("\"hello\""))
        assertEquals(DataType.STRING_LITERAL, TokenPattern.classifyTokenPattern("\"hello world\""))
        assertEquals(DataType.STRING_LITERAL, TokenPattern.classifyTokenPattern("\"\""))
    }

    @Test
    fun `test classify string literals with single quotes`() {
        assertEquals(DataType.STRING_LITERAL, TokenPattern.classifyTokenPattern("'hello'"))
        assertEquals(DataType.STRING_LITERAL, TokenPattern.classifyTokenPattern("'hello world'"))
        assertEquals(DataType.STRING_LITERAL, TokenPattern.classifyTokenPattern("''"))
    }

    @Test
    fun `test classify number literals integers`() {
        assertEquals(DataType.NUMBER_LITERAL, TokenPattern.classifyTokenPattern("123"))
        assertEquals(DataType.NUMBER_LITERAL, TokenPattern.classifyTokenPattern("0"))
        assertEquals(DataType.NUMBER_LITERAL, TokenPattern.classifyTokenPattern("999"))
    }

    @Test
    fun `test classify number literals decimals`() {
        assertEquals(DataType.NUMBER_LITERAL, TokenPattern.classifyTokenPattern("123.45"))
        assertEquals(DataType.NUMBER_LITERAL, TokenPattern.classifyTokenPattern("0.5"))
        assertEquals(DataType.NUMBER_LITERAL, TokenPattern.classifyTokenPattern("999.999"))
    }

    @Test
    fun `test classify identifiers`() {
        assertEquals(DataType.IDENTIFIER, TokenPattern.classifyTokenPattern("variableName"))
        assertEquals(DataType.IDENTIFIER, TokenPattern.classifyTokenPattern("_privateVar"))
        assertEquals(DataType.IDENTIFIER, TokenPattern.classifyTokenPattern("var123"))
        assertEquals(DataType.IDENTIFIER, TokenPattern.classifyTokenPattern("camelCase"))
    }

    @Test
    fun `test invalid patterns return null`() {
        assertNull(TokenPattern.classifyTokenPattern("123abc")) // número seguido de letras
        assertNull(TokenPattern.classifyTokenPattern("\"unclosed string"))
        assertNull(TokenPattern.classifyTokenPattern("'unclosed string"))
        assertNull(TokenPattern.classifyTokenPattern("123.45.67")) // múltiples puntos
    }
}