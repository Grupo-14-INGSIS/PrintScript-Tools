package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
import tokendata.src.main.kotlin.Position
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InterpreterVersionTest {

    @Test
    fun `test unsupported action in version 1_0`() {
        val interpreter = Interpreter("1.0")
        val ifNode = ASTNode(
            ASTNodeType.IF_STATEMENT,
            "if",
            Position(1, 0),
            listOf(
                ASTNode(ASTNodeType.BOOLEAN_LITERAL, "true", Position(1, 1), emptyList()),
                ASTNode(
                    ASTNodeType.BLOCK,
                    "",
                    Position(1, 2),
                    emptyList()
                )
            )
        )
        assertThrows<IllegalArgumentException> {
            interpreter.interpret(ifNode)
        }
    }

    @Test
    fun `test unknown action`() {
        val interpreter = Interpreter("1.1")
        val unknownNode = ASTNode(
            ASTNodeType.INVALID,
            "unknown",
            Position(1, 0),
            emptyList()
        )
        assertThrows<IllegalArgumentException> {
            interpreter.interpret(unknownNode)
        }
    }
}


