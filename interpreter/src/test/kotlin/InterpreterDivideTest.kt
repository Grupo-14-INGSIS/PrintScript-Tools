package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InterpreterDivideTest {
    @Test
    fun `divide two int tokens returns correct result`() {
        val interpreter = Interpreter()
        val left = ASTNode(DataType.NUMBER_LITERAL, "16", Position(1, 1), emptyList())
        val right = ASTNode(DataType.NUMBER_LITERAL, "4", Position(1, 2), emptyList())
        val parent = ASTNode(DataType.DIVISION, "/", Position(1, 0), listOf(left, right))

        val result = interpreter.interpret(parent)
        assertEquals(4.0, result)
    }

    @Test
    fun `divide two double tokens returns correct result`() {
        val interpreter = Interpreter()
        val left = ASTNode(DataType.NUMBER_LITERAL, "16.0", Position(1, 1), emptyList())
        val right = ASTNode(DataType.NUMBER_LITERAL, "4.0", Position(1, 2), emptyList())
        val parent = ASTNode(DataType.DIVISION, "/", Position(1, 0), listOf(left, right))

        val result = interpreter.interpret(parent)
        assertEquals(4.0, result)
    }

    @Test
    fun `divide int and double tokens returns correct result`() {
        val interpreter = Interpreter()
        val left = ASTNode(DataType.NUMBER_LITERAL, "16", Position(1, 1), emptyList())
        val right = ASTNode(DataType.NUMBER_LITERAL, "4.0", Position(1, 2), emptyList())
        val parent = ASTNode(DataType.DIVISION, "/", Position(1, 0), listOf(left, right))

        val result = interpreter.interpret(parent)
        assertEquals(4.0, result)
    }

    @Test
    fun `divide with non-numeric token throws exception`() {
        val interpreter = Interpreter()
        val left = ASTNode(DataType.STRING_LITERAL, "hola", Position(1, 1), emptyList())
        val right = ASTNode(DataType.NUMBER_LITERAL, "3", Position(1, 2), emptyList())
        val parent = ASTNode(DataType.DIVISION, "/", Position(1, 0), listOf(left, right))

        assertThrows<IllegalArgumentException> {
            interpreter.interpret(parent)
        }
    }

    @Test
    fun `divide by zero throws exception`() {
        val interpreter = Interpreter()
        val left = ASTNode(DataType.NUMBER_LITERAL, "5684095", Position(1, 1), emptyList())
        val right = ASTNode(DataType.NUMBER_LITERAL, "0", Position(1, 2), emptyList())
        val parent = ASTNode(DataType.DIVISION, "/", Position(1, 0), listOf(left, right))

        assertThrows<IllegalArgumentException> {
            interpreter.interpret(parent)
        }
    }
}
