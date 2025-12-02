package interpreter.src.test.kotlin

import interpreter.src.main.kotlin.formatNumber
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UtilTest {

    @Test
    fun `test formatNumber with double`() {
        val number = 42.0
        assertEquals("42", number.formatNumber())
    }

    @Test
    fun `test formatNumber with double with decimals`() {
        val number = 42.5
        assertEquals("42.5", number.formatNumber())
    }

    @Test
    fun `test formatNumber with string`() {
        val string = "\"hello\""
        assertEquals("hello", string.formatNumber())
    }

    @Test
    fun `test formatNumber with other`() {
        val other = 123
        assertEquals("123", other.formatNumber())
    }
}
