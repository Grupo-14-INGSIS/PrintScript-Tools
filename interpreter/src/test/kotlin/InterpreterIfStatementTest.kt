package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test

class InterpreterIfStatementTest {
    @Test
    fun `if statement with true condition executes then block`() {
        val interpreter = Interpreter("1.1")
        val ifNode = ASTNode(
            DataType.IF_STATEMENT,
            "if",
            Position(1, 0),
            listOf(
                ASTNode(DataType.BOOLEAN_LITERAL, "true", Position(1, 1), emptyList()),
                ASTNode(DataType.STRING_LITERAL, "then block", Position(1, 2), emptyList())
            )
        )
        val result = interpreter.interpret(ifNode)
        assertEquals("then block", result)
    }

    @Test
    fun `if statement with false condition and no else block does nothing`() {
        val interpreter = Interpreter("1.1")
        val ifNode = ASTNode(
            DataType.IF_STATEMENT,
            "if",
            Position(1, 0),
            listOf(
                ASTNode(DataType.BOOLEAN_LITERAL, "false", Position(1, 1), emptyList()),
                ASTNode(DataType.STRING_LITERAL, "then block", Position(1, 2), emptyList())
            )
        )
        val result = interpreter.interpret(ifNode)
        assertEquals(Unit, result)
    }

    @Test
    fun `if statement with false condition executes else block`() {
        val interpreter = Interpreter("1.1")
        val ifNode = ASTNode(
            DataType.IF_STATEMENT,
            "if",
            Position(1, 0),
            listOf(
                ASTNode(DataType.BOOLEAN_LITERAL, "false", Position(1, 1), emptyList()),
                ASTNode(DataType.STRING_LITERAL, "then block", Position(1, 2), emptyList()),
                ASTNode(DataType.STRING_LITERAL, "else block", Position(1, 3), emptyList())
            )
        )
        val result = interpreter.interpret(ifNode)
        assertEquals("else block", result)
    }
}
