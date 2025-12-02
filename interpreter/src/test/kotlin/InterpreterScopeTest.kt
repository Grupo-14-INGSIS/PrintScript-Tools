package interpreter.src.test.kotlin

import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InterpreterScopeTest {

    @Test
    fun `test enter and exit scope`() {
        val interpreter = Interpreter("1.1")
        interpreter.declareVariable("x", 10.0, "number")
        interpreter.enterScope()
        interpreter.declareVariable("x", 20.0, "number")
        assertEquals(20.0, interpreter.resolveVariable("x"))
        interpreter.exitScope()
        assertEquals(10.0, interpreter.resolveVariable("x"))
    }

    @Test
    fun `test declare constant`() {
        val interpreter = Interpreter("1.1")
        interpreter.declareConstant("c", 100.0, "number")
        assertEquals(100.0, interpreter.resolveVariable("c"))
    }

    @Test
    fun `test declare existing constant throws exception`() {
        val interpreter = Interpreter("1.1")
        interpreter.declareConstant("c", 100.0, "number")
        assertThrows<IllegalStateException> {
            interpreter.declareConstant("c", 200.0, "number")
        }
    }
}
