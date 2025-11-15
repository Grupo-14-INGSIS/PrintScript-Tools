package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class InterpreterPrintTest {
    @Test
    fun `print number literal outputs correct value`() {
        val interpreter = Interpreter()
        val node = ASTNode(
            DataType.PRINTLN,
            "println",
            Position(1, 0),
            listOf(ASTNode(DataType.NUMBER_LITERAL, "42", Position(1, 1), emptyList()))
        )

        val output = captureStdOut { interpreter.interpret(node) }
        assertEquals("42", output.trim())
    }

    @Test
    fun `print string literal outputs correct value`() {
        val interpreter = Interpreter()
        val node = ASTNode(
            DataType.PRINTLN,
            "println",
            Position(1, 0),
            listOf(
                ASTNode(DataType.STRING_LITERAL, "hello", Position(1, 1), emptyList())
            )
        )

        val output = captureStdOut { interpreter.interpret(node) }
        assertEquals("hello", output.trim())
    }

    private fun captureStdOut(block: () -> Unit): String {
        val stream = ByteArrayOutputStream()
        val original = System.out
        System.setOut(PrintStream(stream))
        block()
        System.setOut(original)
        return stream.toString()
    }
}
