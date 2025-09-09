package interpreter.src.test.kotlin

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import interpreter.src.main.kotlin.Actions

class ActionsTest {

    @Test
    fun `enum contains expected base actions`() {
        val expected = listOf(
            Actions.ADD,
            Actions.SUBTRACT,
            Actions.MULTIPLY,
            Actions.DIVIDE,
            Actions.PRINT,
            Actions.VAR_DECLARATION,
            Actions.VAR_DECLARATION_AND_ASSIGNMENT,
            Actions.ASSIGNMENT_TO_EXISTING_VAR
        )
        assertTrue(expected.all { it in Actions.entries })
    }

    @Test
    fun `enum contains expected v1_1 actions`() {
        val expected = listOf(
            Actions.CONST_DECLARATION,
            Actions.CONST_DECLARATION_AND_ASSIGNMENT,
            Actions.IF_STATEMENT,
            Actions.ELSE_STATEMENT,
            Actions.READ_INPUT,
            Actions.READ_ENV,
            Actions.BOOLEAN_OPERATION
        )
        assertTrue(expected.all { it in Actions.entries })
    }

    @Test
    fun `valueOf returns correct enum`() {
        val action = Actions.valueOf("PRINT")
        assertEquals(Actions.PRINT, action)
    }

    @Test
    fun `valueOf throws on invalid name`() {
        assertThrows(IllegalArgumentException::class.java) {
            Actions.valueOf("INVALID_ACTION")
        }
    }

    @Test
    fun `enum values are unique`() {
        val names = Actions.entries.map { it.name }
        assertEquals(names.size, names.toSet().size)
    }

    @Test
    fun `enum can be used in map keys`() {
        val map = mapOf(
            Actions.ADD to "+",
            Actions.SUBTRACT to "-",
            Actions.PRINT to "print"
        )
        assertEquals("+", map[Actions.ADD])
        assertEquals("print", map[Actions.PRINT])
    }
}
