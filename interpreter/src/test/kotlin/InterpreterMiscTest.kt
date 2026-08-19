package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
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
            ASTNodeType.BLOCK,
            "",
            Position(1, 0),
            listOf(
                ASTNode(
                    ASTNodeType.DECLARATION,
                    "=",
                    Position(2, 0),
                    listOf(
                        ASTNode(
                            ASTNodeType.LET_KEYWORD,
                            "x",
                            Position(2, 1),
                            listOf(
                                ASTNode(ASTNodeType.IDENTIFIER, "x", Position(2, 2), emptyList()),
                                ASTNode(ASTNodeType.NUMBER_TYPE, "number", Position(2, 3), emptyList())
                            )
                        ),
                        ASTNode(ASTNodeType.NUMBER_LITERAL, "42.0", Position(2, 4), emptyList())
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
            ASTNodeType.PRINTLN,
            "println",
            Position(1, 0),
            listOf(ASTNode(ASTNodeType.STRING_LITERAL, "hello world", Position(1, 1), emptyList()))
        )
        interpreter.interpret(printNode)
        assertEquals("hello world\n", outContent.toString().replace("\r\n", "\n"))

        System.setOut(originalOut)
    }

    @Test
    fun `test literal expression`() {
        val interpreter = Interpreter("1.1")
        val literalNode = ASTNode(ASTNodeType.NUMBER_LITERAL, "123.0", Position(1, 0), emptyList())
        val result = interpreter.interpret(literalNode)
        assertEquals(123.0, result)
    }
}


