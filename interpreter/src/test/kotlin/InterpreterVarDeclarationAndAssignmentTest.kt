package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InterpreterVarDeclarationAndAssignmentTest {

    @Test
    fun `test var declaration and assignment with correct type`() {
        val interpreter = Interpreter("1.1")
        val varDeclarationNode = ASTNode(
            DataType.DECLARATION,
            "=",
            Position(1, 0),
            listOf(
                ASTNode(
                    DataType.LET_KEYWORD,
                    "x",
                    Position(1, 1),
                    listOf(
                        ASTNode(DataType.IDENTIFIER, "x", Position(1, 2), emptyList()),
                        ASTNode(DataType.NUMBER_TYPE, "number", Position(1, 3), emptyList())
                    )
                ),
                ASTNode(DataType.NUMBER_LITERAL, "42.0", Position(1, 4), emptyList())
            )
        )
        interpreter.interpret(varDeclarationNode)
        assertEquals(42.0, interpreter.resolveVariable("x"))
    }

    @Test
    fun `test var declaration and assignment with incorrect type`() {
        val interpreter = Interpreter("1.1")
        val varDeclarationNode = ASTNode(
            DataType.DECLARATION,
            "=",
            Position(1, 0),
            listOf(
                ASTNode(
                    DataType.LET_KEYWORD,
                    "x",
                    Position(1, 1),
                    listOf(
                        ASTNode(DataType.IDENTIFIER, "x", Position(1, 2), emptyList()),
                        ASTNode(DataType.NUMBER_TYPE, "number", Position(1, 3), emptyList())
                    )
                ),
                ASTNode(DataType.STRING_LITERAL, "hello", Position(1, 4), emptyList())
            )
        )
        assertThrows<IllegalArgumentException> {
            interpreter.interpret(varDeclarationNode)
        }
    }
}
