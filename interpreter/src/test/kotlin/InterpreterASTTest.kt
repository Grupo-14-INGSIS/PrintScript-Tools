package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test

class InterpreterASTTest {

    @Test
    fun `test executeAST`() {
        val outContent = java.io.ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(java.io.PrintStream(outContent))

        val interpreter = Interpreter("1.1") { println(it) }
        val ast = ASTNode(
            DataType.SCRIPT,
            "script",
            Position(1, 0),
            listOf(
                ASTNode(
                    DataType.PRINTLN,
                    "println",
                    Position(1, 0),
                    listOf(ASTNode(DataType.STRING_LITERAL, "hello", Position(1, 1), emptyList()))
                ),
                ASTNode(
                    DataType.PRINTLN,
                    "println",
                    Position(2, 0),
                    listOf(ASTNode(DataType.STRING_LITERAL, "world", Position(2, 1), emptyList()))
                )
            )
        )
        val outputs = interpreter.executeAST(ast)
        assertEquals(listOf("hello", "world"), outputs)
        assertEquals("hello\nworld\n", outContent.toString().replace("\r\n", "\n"))

        System.setOut(originalOut)
    }
}
