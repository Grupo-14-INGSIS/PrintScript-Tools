import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import src.main.model.structure.DataType
import src.main.model.structure.TokenMap

class TokenMapTest {

    @Test
    fun `test classify keywords`() {
        assertEquals(DataType.LET_KEYWORD, TokenMap.classifyTokenMap("let"))
    }

    @Test
    fun `test classify types`() {
        assertEquals(DataType.STRING_TYPE, TokenMap.classifyTokenMap("string"))
        assertEquals(DataType.NUMBER_TYPE, TokenMap.classifyTokenMap("number")) //segun la consigna los nombres son en minuscula
    }

    @Test
    fun `test classify operators`() {
        assertEquals(DataType.ASSIGNATION, TokenMap.classifyTokenMap("="))
        assertEquals(DataType.ADDITION, TokenMap.classifyTokenMap("+"))
        assertEquals(DataType.SUBTRACTION, TokenMap.classifyTokenMap("-"))
        assertEquals(DataType.MULTIPLICATION, TokenMap.classifyTokenMap("*"))
        assertEquals(DataType.DIVISION, TokenMap.classifyTokenMap("/"))
    }

    @Test
    fun `test classify punctuation`() {
        assertEquals(DataType.SPACE, TokenMap.classifyTokenMap(" "))
        assertEquals(DataType.COLON, TokenMap.classifyTokenMap(":"))
        assertEquals(DataType.SEMICOLON, TokenMap.classifyTokenMap(";"))
        assertEquals(DataType.LINE_BREAK, TokenMap.classifyTokenMap("\n"))
    }

    @Test
    fun `test classify println`() {
        assertEquals(DataType.PRINTLN, TokenMap.classifyTokenMap("println"))
    }

    @Test
    fun `test unknown token returns null`() {
        assertNull(TokenMap.classifyTokenMap("unknown"))
    }
}