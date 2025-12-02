package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class InterpreterMiscTest {

    @Test
    fun `test block statement`() {
        val interpreter = Interpreter("1.1")
        val blockNode = ASTNode(
            DataType.BLOCK,
            "",
            Position(1, 0),
            listOf(
                ASTNode(
                    DataType.DECLARATION,
                    "=",
                    Position(2, 0),
                    listOf(
                        ASTNode(
                            DataType.LET_KEYWORD,
                            "x",
                            Position(2, 1),
                            listOf(
                                ASTNode(DataType.IDENTIFIER, "x", Position(2, 2), emptyList()),
                                ASTNode(DataType.NUMBER_TYPE, "number", Position(2, 3), emptyList())
                            )
                        ),
                        ASTNode(DataType.NUMBER_LITERAL, "42.0", Position(2, 4), emptyList())
                    )
                )
            )
        )
        interpreter.interpret(blockNode)
        assertEquals(42.0, interpreter.resolveVariable("x"))
    }

    @Test
    fun `test print statement`() {
        val outContent = ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(PrintStream(outContent))

        val interpreter = Interpreter("1.1") { println(it) }
        val printNode = ASTNode(
            DataType.PRINTLN,
            "println",
            Position(1, 0),
            listOf(ASTNode(DataType.STRING_LITERAL, "hello world", Position(1, 1), emptyList()))
        )
        interpreter.interpret(printNode)
        assertEquals("hello world\n", outContent.toString().replace("\r\n", "\n"))

        System.setOut(originalOut)
    }

    @Test
    fun `test literal expression`() {
        val interpreter = Interpreter("1.1")
        val literalNode = ASTNode(DataType.NUMBER_LITERAL, "123.0", Position(1, 0), emptyList())
        val result = interpreter.interpret(literalNode)
        assertEquals(123.0, result)
    }
}
