package lexer.src.test.kotlin

import lexer.src.main.kotlin.TokenMap
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tokendata.src.main.kotlin.DataType

class tckTest {

    @Test
    fun `test classify keywords`() {
        assertEquals(DataType.READ_INPUT, TokenMap.classifyTokenMap("readInput", "1.1"))
    }
}
