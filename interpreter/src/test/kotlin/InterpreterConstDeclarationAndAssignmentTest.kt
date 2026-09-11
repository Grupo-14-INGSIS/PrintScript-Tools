package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
import ast.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.ConstDeclarationAndAssignment
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InterpreterConstDeclarationAndAssignmentTest {

    @Test
    fun `test const declaration and assignment with string type`() {
        val interpreter = Interpreter("1.1")
        val constDeclarationNode = ASTNode(
            ASTNodeType.DECLARATION,
            "=",
            Position(1, 0),
            listOf(
                ASTNode(
                    ASTNodeType.CONST_KEYWORD,
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
        interpreter.interpret(constDeclarationNode)
        assertEquals("World", interpreter.resolveVariable("name"))
    }

    @Test
    fun `test const declaration and assignment with correct type`() {
        val interpreter = Interpreter("1.1")
        val constDeclarationNode = ASTNode(
            ASTNodeType.DECLARATION,
            "=",
            Position(1, 0),
            listOf(
                ASTNode(
                    ASTNodeType.CONST_KEYWORD,
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
        interpreter.interpret(constDeclarationNode)
        assertEquals(42.0, interpreter.resolveVariable("x"))
    }

    @Test
    fun `test const declaration and assignment with incorrect type`() {
        val interpreter = Interpreter("1.1")
        val constDeclarationNode = ASTNode(
            ASTNodeType.DECLARATION,
            "=",
            Position(1, 0),
            listOf(
                ASTNode(
                    ASTNodeType.CONST_KEYWORD,
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
            interpreter.interpret(constDeclarationNode)
        }
    }

    @Test
    fun `test ConstDeclarationAndAssignment object directly`() {
        val interpreter = Interpreter("1.1")
        val node = ASTNode(
            ASTNodeType.DECLARATION,
            "=",
            Position(1, 0),
            listOf(
                ASTNode(ASTNodeType.IDENTIFIER, "myConst", Position(1, 1), emptyList()),
                ASTNode(ASTNodeType.STRING_TYPE, "string", Position(1, 2), emptyList()),
                ASTNode(ASTNodeType.STRING_LITERAL, "val", Position(1, 3), emptyList())
            )
        )
        ConstDeclarationAndAssignment.interpret(node, interpreter)
        assertEquals("val", interpreter.resolveVariable("myConst"))

        // test number and boolean types
        val numNode = ASTNode(
            ASTNodeType.DECLARATION,
            "=",
            Position(1, 0),
            listOf(
                ASTNode(ASTNodeType.IDENTIFIER, "myNum", Position(1, 1), emptyList()),
                ASTNode(ASTNodeType.NUMBER_TYPE, "number", Position(1, 2), emptyList()),
                ASTNode(ASTNodeType.NUMBER_LITERAL, "42", Position(1, 3), emptyList())
            )
        )
        ConstDeclarationAndAssignment.interpret(numNode, interpreter)

        val boolNode = ASTNode(
            ASTNodeType.DECLARATION,
            "=",
            Position(1, 0),
            listOf(
                ASTNode(ASTNodeType.IDENTIFIER, "myBool", Position(1, 1), emptyList()),
                ASTNode(ASTNodeType.BOOLEAN_TYPE, "boolean", Position(1, 2), emptyList()),
                ASTNode(ASTNodeType.BOOLEAN_LITERAL, "true", Position(1, 3), emptyList())
            )
        )
        ConstDeclarationAndAssignment.interpret(boolNode, interpreter)

        // type mismatch throws
        val mismatchNode = ASTNode(
            ASTNodeType.DECLARATION,
            "=",
            Position(1, 0),
            listOf(
                ASTNode(ASTNodeType.IDENTIFIER, "myBad", Position(1, 1), emptyList()),
                ASTNode(ASTNodeType.NUMBER_TYPE, "number", Position(1, 2), emptyList()),
                ASTNode(ASTNodeType.STRING_LITERAL, "not_a_num", Position(1, 3), emptyList())
            )
        )
        assertThrows<IllegalArgumentException> {
            ConstDeclarationAndAssignment.interpret(mismatchNode, interpreter)
        }
    }
}


