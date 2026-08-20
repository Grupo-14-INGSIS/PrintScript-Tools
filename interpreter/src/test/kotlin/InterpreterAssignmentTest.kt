package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InterpreterAssignmentTest {
    @Test
    fun `assign numeric value to variable`() {
        val interpreter = Interpreter("1.1")
        val declarationNode = ASTNode(
            ASTNodeType.DECLARATION,
            "=",
            Position(1, 0),
            listOf(
                ASTNode(
                    ASTNodeType.LET_KEYWORD,
                    "x",
                    Position(1, 1),
                    listOf(
                        ASTNode(ASTNodeType.IDENTIFIER, "x", Position(1, 2), emptyList()),
                        ASTNode(ASTNodeType.NUMBER_TYPE, "number", Position(1, 3), emptyList())
                    )
                ),
                ASTNode(ASTNodeType.NUMBER_LITERAL, "0.0", Position(1, 4), emptyList())
            )
        )
        interpreter.interpret(declarationNode)

        val assignmentNode = ASTNode(
            ASTNodeType.ASSIGNATION,
            "=",
            Position(2, 0),
            listOf(
                ASTNode(ASTNodeType.IDENTIFIER, "x", Position(2, 1), emptyList()),
                ASTNode(ASTNodeType.NUMBER_LITERAL, "42.0", Position(2, 2), emptyList())
            )
        )
        val result = interpreter.interpret(assignmentNode)
        assertEquals(42.0, result)
        assertEquals(42.0, interpreter.resolveVariable("x"))
    }

    @Test
    fun `assign string value to variable`() {
        val interpreter = Interpreter("1.1")
        val declarationNode = ASTNode(
            ASTNodeType.DECLARATION,
            "=",
            Position(1, 0),
            listOf(
                ASTNode(
                    ASTNodeType.LET_KEYWORD,
                    "greeting",
                    Position(1, 1),
                    listOf(
                        ASTNode(ASTNodeType.IDENTIFIER, "greeting", Position(1, 2), emptyList()),
                        ASTNode(ASTNodeType.STRING_TYPE, "string", Position(1, 3), emptyList())
                    )
                ),
                ASTNode(ASTNodeType.STRING_LITERAL, "", Position(1, 4), emptyList())
            )
        )
        interpreter.interpret(declarationNode)

        val assignmentNode = ASTNode(
            ASTNodeType.ASSIGNATION,
            "=",
            Position(2, 0),
            listOf(
                ASTNode(ASTNodeType.IDENTIFIER, "greeting", Position(2, 1), emptyList()),
                ASTNode(ASTNodeType.STRING_LITERAL, "hello", Position(2, 2), emptyList())
            )
        )
        val result = interpreter.interpret(assignmentNode)
        assertEquals("hello", result)
        assertEquals("hello", interpreter.resolveVariable("greeting"))
    }

    @Test
    fun `reassign existing variable`() {
        val interpreter = Interpreter("1.1")
        val declarationNode = ASTNode(
            ASTNodeType.DECLARATION,
            "=",
            Position(1, 0),
            listOf(
                ASTNode(
                    ASTNodeType.LET_KEYWORD,
                    "y",
                    Position(1, 1),
                    listOf(
                        ASTNode(ASTNodeType.IDENTIFIER, "y", Position(1, 2), emptyList()),
                        ASTNode(ASTNodeType.NUMBER_TYPE, "number", Position(1, 3), emptyList())
                    )
                ),
                ASTNode(ASTNodeType.NUMBER_LITERAL, "0.0", Position(1, 4), emptyList())
            )
        )
        interpreter.interpret(declarationNode)

        val firstAssignment = ASTNode(
            ASTNodeType.ASSIGNATION,
            "=",
            Position(2, 0),
            listOf(
                ASTNode(ASTNodeType.IDENTIFIER, "y", Position(2, 1), emptyList()),
                ASTNode(ASTNodeType.NUMBER_LITERAL, "10.0", Position(2, 2), emptyList())
            )
        )
        interpreter.interpret(firstAssignment)

        val secondAssignment = ASTNode(
            ASTNodeType.ASSIGNATION,
            "=",
            Position(3, 0),
            listOf(
                ASTNode(ASTNodeType.IDENTIFIER, "y", Position(3, 1), emptyList()),
                ASTNode(ASTNodeType.NUMBER_LITERAL, "20.0", Position(3, 2), emptyList())
            )
        )
        val result = interpreter.interpret(secondAssignment)
        assertEquals(20.0, result)
        assertEquals(20.0, interpreter.resolveVariable("y"))
    }

    @Test
    fun `assign to undeclared variable throws exception`() {
        val interpreter = Interpreter("1.1")
        val assignmentNode = ASTNode(
            ASTNodeType.ASSIGNATION,
            "=",
            Position(1, 0),
            listOf(
                ASTNode(ASTNodeType.IDENTIFIER, "x", Position(1, 1), emptyList()),
                ASTNode(ASTNodeType.NUMBER_LITERAL, "42", Position(1, 2), emptyList())
            )
        )
        assertThrows<IllegalStateException> {
            interpreter.interpret(assignmentNode)
        }
    }

    @Test
    fun `assign type mismatch throws exception`() {
        val interpreter = Interpreter("1.1")
        val declarationNode = ASTNode(
            ASTNodeType.DECLARATION,
            "=",
            Position(1, 0),
            listOf(
                ASTNode(
                    ASTNodeType.LET_KEYWORD,
                    "numVar",
                    Position(1, 1),
                    listOf(
                        ASTNode(ASTNodeType.IDENTIFIER, "numVar", Position(1, 2), emptyList()),
                        ASTNode(ASTNodeType.NUMBER_TYPE, "number", Position(1, 3), emptyList())
                    )
                ),
                ASTNode(ASTNodeType.NUMBER_LITERAL, "0.0", Position(1, 4), emptyList())
            )
        )
        interpreter.interpret(declarationNode)

        val assignmentNode = ASTNode(
            ASTNodeType.ASSIGNATION,
            "=",
            Position(2, 0),
            listOf(
                ASTNode(ASTNodeType.IDENTIFIER, "numVar", Position(2, 1), emptyList()),
                ASTNode(ASTNodeType.STRING_LITERAL, "hello", Position(2, 2), emptyList())
            )
        )
        assertThrows<IllegalArgumentException> {
            interpreter.interpret(assignmentNode)
        }
    }
}


