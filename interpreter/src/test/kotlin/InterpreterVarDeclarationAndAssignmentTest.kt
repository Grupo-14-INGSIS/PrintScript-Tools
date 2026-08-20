package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InterpreterVarDeclarationAndAssignmentTest {

    @Test
    fun `test var declaration and assignment with string type`() {
        val interpreter = Interpreter("1.1")
        val varDeclarationNode = ASTNode(
            ASTNodeType.DECLARATION,
            "=",
            Position(1, 0),
            listOf(
                ASTNode(
                    ASTNodeType.LET_KEYWORD,
                    "name",
                    Position(1, 1),
                    listOf(
                        ASTNode(ASTNodeType.IDENTIFIER, "name", Position(1, 2), emptyList()),
                        ASTNode(ASTNodeType.STRING_TYPE, "string", Position(1, 3), emptyList())
                    )
                ),
                ASTNode(ASTNodeType.STRING_LITERAL, "World", Position(1, 4), emptyList())
            )
        )
        interpreter.interpret(varDeclarationNode)
        assertEquals("World", interpreter.resolveVariable("name"))
    }

    @Test
    fun `test var declaration and assignment with correct type`() {
        val interpreter = Interpreter("1.1")
        val varDeclarationNode = ASTNode(
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
                ASTNode(ASTNodeType.NUMBER_LITERAL, "42.0", Position(1, 4), emptyList())
            )
        )
        interpreter.interpret(varDeclarationNode)
        assertEquals(42.0, interpreter.resolveVariable("x"))
    }

    @Test
    fun `test var reassignment works correctly`() {
        val interpreter = Interpreter("1.1")
        val varDeclarationNode = ASTNode(
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
                ASTNode(ASTNodeType.NUMBER_LITERAL, "10.0", Position(1, 4), emptyList())
            )
        )
        interpreter.interpret(varDeclarationNode)
        assertEquals(10.0, interpreter.resolveVariable("x"))

        val varReassignmentNode = ASTNode(
            ASTNodeType.ASSIGNATION,
            "=",
            Position(2, 0),
            listOf(
                ASTNode(ASTNodeType.IDENTIFIER, "x", Position(2, 1), emptyList()),
                ASTNode(ASTNodeType.NUMBER_LITERAL, "20.0", Position(2, 2), emptyList())
            )
        )
        interpreter.interpret(varReassignmentNode)
        assertEquals(20.0, interpreter.resolveVariable("x"))
    }

    @Test
    fun `test var declaration and assignment with incorrect type`() {
        val interpreter = Interpreter("1.1")
        val varDeclarationNode = ASTNode(
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
                ASTNode(ASTNodeType.STRING_LITERAL, "hello", Position(1, 4), emptyList())
            )
        )
        assertThrows<IllegalArgumentException> {
            interpreter.interpret(varDeclarationNode)
        }
    }
}


