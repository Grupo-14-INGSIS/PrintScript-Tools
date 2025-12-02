package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test

class InterpreterIfStatementTest {

    @Test
    fun `if statement with true condition executes then branch`() {
        val interpreter = Interpreter("1.1")
        val ifNode = ASTNode(
            DataType.IF_STATEMENT,
            "if",
            Position(1, 0),
            listOf(
                ASTNode(DataType.BOOLEAN_LITERAL, "true", Position(1, 1), emptyList()),
                ASTNode(
                    DataType.BLOCK,
                    "",
                    Position(1, 2),
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
            )
        )
        interpreter.interpret(ifNode)
        assertEquals(42.0, interpreter.resolveVariable("x"))
    }

    @Test
    fun `if statement with false condition does not execute then branch`() {
        val interpreter = Interpreter("1.1")
        val ifNode = ASTNode(
            DataType.IF_STATEMENT,
            "if",
            Position(1, 0),
            listOf(
                ASTNode(DataType.BOOLEAN_LITERAL, "false", Position(1, 1), emptyList()),
                ASTNode(
                    DataType.BLOCK,
                    "",
                    Position(1, 2),
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
            )
        )
        interpreter.interpret(ifNode)
        // x should not be declared
        try {
            interpreter.resolveVariable("x")
        } catch (e: IllegalStateException) {
            assertEquals("Variable 'x' not declared", e.message)
        }
    }

    @Test
    fun `if-else statement with true condition executes then branch`() {
        val interpreter = Interpreter("1.1")
        val ifNode = ASTNode(
            DataType.IF_STATEMENT,
            "if",
            Position(1, 0),
            listOf(
                ASTNode(DataType.BOOLEAN_LITERAL, "true", Position(1, 1), emptyList()),
                ASTNode(
                    DataType.BLOCK,
                    "",
                    Position(1, 2),
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
                ),
                ASTNode(
                    DataType.BLOCK,
                    "",
                    Position(3, 0),
                    listOf(
                        ASTNode(
                            DataType.DECLARATION,
                            "=",
                            Position(4, 0),
                            listOf(
                                ASTNode(
                                    DataType.LET_KEYWORD,
                                    "x",
                                    Position(4, 1),
                                    listOf(
                                        ASTNode(DataType.IDENTIFIER, "x", Position(4, 2), emptyList()),
                                        ASTNode(DataType.NUMBER_TYPE, "number", Position(4, 3), emptyList())
                                    )
                                ),
                                ASTNode(DataType.NUMBER_LITERAL, "100.0", Position(4, 4), emptyList())
                            )
                        )
                    )
                )
            )
        )
        interpreter.interpret(ifNode)
        assertEquals(42.0, interpreter.resolveVariable("x"))
    }

    @Test
    fun `if-else statement with false condition executes else branch`() {
        val interpreter = Interpreter("1.1")
        val ifNode = ASTNode(
            DataType.IF_STATEMENT,
            "if",
            Position(1, 0),
            listOf(
                ASTNode(DataType.BOOLEAN_LITERAL, "false", Position(1, 1), emptyList()),
                ASTNode(
                    DataType.BLOCK,
                    "",
                    Position(1, 2),
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
                ),
                ASTNode(
                    DataType.BLOCK,
                    "",
                    Position(3, 0),
                    listOf(
                        ASTNode(
                            DataType.DECLARATION,
                            "=",
                            Position(4, 0),
                            listOf(
                                ASTNode(
                                    DataType.LET_KEYWORD,
                                    "x",
                                    Position(4, 1),
                                    listOf(
                                        ASTNode(DataType.IDENTIFIER, "x", Position(4, 2), emptyList()),
                                        ASTNode(DataType.NUMBER_TYPE, "number", Position(4, 3), emptyList())
                                    )
                                ),
                                ASTNode(DataType.NUMBER_LITERAL, "100.0", Position(4, 4), emptyList())
                            )
                        )
                    )
                )
            )
        )
        interpreter.interpret(ifNode)
        assertEquals(100.0, interpreter.resolveVariable("x"))
    }
}
