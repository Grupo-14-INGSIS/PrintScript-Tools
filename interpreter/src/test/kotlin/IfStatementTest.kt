package intepreter.src.test.kotlin

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import interpreter.src.main.kotlin.Interpreter
import interpreter.src.main.kotlin.IfStatement

class IfStatementTest {

    @Test
    fun IfStatementTest1() {
        val interpreter = Interpreter()
        val ifStatement = IfStatement(interpreter)
        // val output = ifStatement.interpret(ast)
    }
}
