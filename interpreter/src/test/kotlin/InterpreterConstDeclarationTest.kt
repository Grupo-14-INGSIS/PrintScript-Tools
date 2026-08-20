package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InterpreterConstDeclarationTest {

    @Test
    fun `declare and assign number constant`() {
        val interpreter = Interpreter("1.1")
        val declarationNode = ASTNode(
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
        interpreter.interpret(declarationNode)
        assertEquals(42.0, interpreter.resolveVariable("x"))
    }

    @Test
    fun `declare and assign string constant`() {
        val interpreter = Interpreter("1.1")
        val declarationNode = ASTNode(
            ASTNodeType.DECLARATION,
            "=",
            Position(1, 0),
            listOf(
                ASTNode(
                    ASTNodeType.CONST_KEYWORD,
                    "msg",
                    Position(1, 1),
                    listOf(
                        ASTNode(ASTNodeType.IDENTIFIER, "msg", Position(1, 2), emptyList()),
                        ASTNode(ASTNodeType.STRING_TYPE, "string", Position(1, 3), emptyList())
                    )
                ),
                ASTNode(ASTNodeType.STRING_LITERAL, "hello", Position(1, 4), emptyList())
            )
        )
        interpreter.interpret(declarationNode)
        assertEquals("hello", interpreter.resolveVariable("msg"))
    }

    @Test
    fun `declare and assign boolean constant`() {
        val interpreter = Interpreter("1.1")
        val declarationNode = ASTNode(
            ASTNodeType.DECLARATION,
            "=",
            Position(1, 0),
            listOf(
                ASTNode(
                    ASTNodeType.CONST_KEYWORD,
                    "flag",
                    Position(1, 1),
                    listOf(
                        ASTNode(ASTNodeType.IDENTIFIER, "flag", Position(1, 2), emptyList()),
                        ASTNode(ASTNodeType.BOOLEAN_TYPE, "boolean", Position(1, 3), emptyList())
                    )
                ),
                ASTNode(ASTNodeType.BOOLEAN_LITERAL, "true", Position(1, 4), emptyList())
            )
        )
        interpreter.interpret(declarationNode)
        assertEquals(true, interpreter.resolveVariable("flag"))
    }

    @Test
    fun `re-assigning a constant throws exception`() {
        val interpreter = Interpreter("1.1")
        val declarationNode = ASTNode(
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
        interpreter.interpret(declarationNode)

        val assignmentNode = ASTNode(
            ASTNodeType.ASSIGNATION,
            "=",
            Position(2, 0),
            listOf(
                ASTNode(ASTNodeType.IDENTIFIER, "x", Position(2, 1), emptyList()),
                ASTNode(ASTNodeType.NUMBER_LITERAL, "100.0", Position(2, 2), emptyList())
            )
        )

        assertThrows<IllegalStateException> {
            interpreter.interpret(assignmentNode)
        }
    }
}


