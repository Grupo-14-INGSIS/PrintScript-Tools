package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test

class InterpreterAddTest {
    @Test
    fun `add two int tokens returns correct result`() {
        val interpreter = Interpreter()
        val left = ASTNode(DataType.NUMBER_LITERAL, "16", Position(1, 1), emptyList())
        val right = ASTNode(DataType.NUMBER_LITERAL, "4", Position(1, 2), emptyList())
        val parent = ASTNode(DataType.ADDITION, "+", Position(1, 0), listOf(left, right))

        val result = interpreter.interpret(parent)
        assertEquals(20, result)
    }

    @Test
    fun `add two double tokens returns correct result`() {
        val interpreter = Interpreter()
        val left = ASTNode(DataType.NUMBER_LITERAL, "16.0", Position(1, 1), emptyList())
        val right = ASTNode(DataType.NUMBER_LITERAL, "4.0", Position(1, 2), emptyList())
        val parent = ASTNode(DataType.ADDITION, "+", Position(1, 0), listOf(left, right))

        val result = interpreter.interpret(parent)
        assertEquals(20.0, result)
    }

    @Test
    fun `add int and double tokens returns correct result`() {
        val interpreter = Interpreter()
        val left = ASTNode(DataType.NUMBER_LITERAL, "16", Position(1, 1), emptyList())
        val right = ASTNode(DataType.NUMBER_LITERAL, "4.0", Position(1, 2), emptyList())
        val parent = ASTNode(DataType.ADDITION, "+", Position(1, 0), listOf(left, right))

        val result = interpreter.interpret(parent)
        assertEquals(20.0, result)
    }

    @Test
    fun `add with non-numeric token returns concatenation`() {
        val interpreter = Interpreter()
        val left = ASTNode(DataType.STRING_LITERAL, "hola", Position(1, 1), emptyList())
        val right = ASTNode(DataType.NUMBER_LITERAL, "3", Position(1, 2), emptyList())
        val parent = ASTNode(DataType.ADDITION, "+", Position(1, 0), listOf(left, right))

        val result = interpreter.interpret(parent)
        assertEquals("hola3", result)
    }

    @Test
    fun `add with ambiguous non-numeric token returns concatenation`() {
        val interpreter = Interpreter()
        val left = ASTNode(DataType.STRING_LITERAL, "3a", Position(1, 1), emptyList())
        val right = ASTNode(DataType.NUMBER_LITERAL, "7", Position(1, 2), emptyList())
        val parent = ASTNode(DataType.ADDITION, "+", Position(1, 0), listOf(left, right))

        val result = interpreter.interpret(parent)
        assertEquals("3a7", result)
    }

    @Test
    fun `add with empty string and number returns concatenation`() {
        val interpreter = Interpreter()
        val left = ASTNode(DataType.STRING_LITERAL, "", Position(1, 1), emptyList())
        val right = ASTNode(DataType.NUMBER_LITERAL, "42", Position(1, 2), emptyList())
        val parent = ASTNode(DataType.ADDITION, "+", Position(1, 0), listOf(left, right))

        val result = interpreter.interpret(parent)
        assertEquals("42", result)
    }

    @Test
    fun `add two strings returns concatenation`() {
        val interpreter = Interpreter()
        val left = ASTNode(DataType.STRING_LITERAL, "foo", Position(1, 1), emptyList())
        val right = ASTNode(DataType.STRING_LITERAL, "bar", Position(1, 2), emptyList())
        val parent = ASTNode(DataType.ADDITION, "+", Position(1, 0), listOf(left, right))

        val result = interpreter.interpret(parent)
        assertEquals("foobar", result)
    }
}
