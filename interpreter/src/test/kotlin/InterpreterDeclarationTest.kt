package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test

class InterpreterDeclarationTest {
//    @Test
//    fun `declare number variable with default value`() {
//        val interpreter = Interpreter("1.1")
//        val declarationNode = ASTNode(
//            DataType.DECLARATION,
//            "var",
//            Position(1, 0),
//            listOf(
//                ASTNode(DataType.IDENTIFIER, "x", Position(1, 1), emptyList()),
//                ASTNode(DataType.NUMBER_TYPE, "number", Position(1, 2), emptyList())
//            )
//        )
//        interpreter.interpret(declarationNode)
//        assertEquals(0.0, interpreter.resolveVariable("x"))
//    }
//
//    @Test
//    fun `declare string variable with default value`() {
//        val interpreter = Interpreter("1.1")
//        val declarationNode = ASTNode(
//            DataType.DECLARATION,
//            "var",
//            Position(1, 0),
//            listOf(
//                ASTNode(DataType.IDENTIFIER, "msg", Position(1, 1), emptyList()),
//                ASTNode(DataType.STRING_TYPE, "string", Position(1, 2), emptyList())
//            )
//        )
//        interpreter.interpret(declarationNode)
//        assertEquals("", interpreter.resolveVariable("msg"))
//    }
//
//    @Test
//    fun `declare boolean variable with default value`() {
//        val interpreter = Interpreter("1.1")
//        val declarationNode = ASTNode(
//            DataType.DECLARATION,
//            "var",
//            Position(1, 0),
//            listOf(
//                ASTNode(DataType.IDENTIFIER, "flag", Position(1, 1), emptyList()),
//                ASTNode(DataType.BOOLEAN_TYPE, "boolean", Position(1, 2), emptyList())
//            )
//        )
//        interpreter.interpret(declarationNode)
//        assertEquals(false, interpreter.resolveVariable("flag"))
//    }

    @Test
    fun `declare and assign number variable`() {
        val interpreter = Interpreter("1.1")
        val declarationNode = ASTNode(
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
        interpreter.interpret(declarationNode)
        assertEquals(42.0, interpreter.resolveVariable("x"))
    }

    @Test
    fun `declare and assign string variable`() {
        val interpreter = Interpreter("1.1")
        val declarationNode = ASTNode(
            DataType.DECLARATION,
            "=",
            Position(1, 0),
            listOf(
                ASTNode(
                    DataType.LET_KEYWORD,
                    "msg",
                    Position(1, 1),
                    listOf(
                        ASTNode(DataType.IDENTIFIER, "msg", Position(1, 2), emptyList()),
                        ASTNode(DataType.STRING_TYPE, "string", Position(1, 3), emptyList())
                    )
                ),
                ASTNode(DataType.STRING_LITERAL, "hello", Position(1, 4), emptyList())
            )
        )
        interpreter.interpret(declarationNode)
        assertEquals("hello", interpreter.resolveVariable("msg"))
    }

    @Test
    fun `declare and assign boolean variable`() {
        val interpreter = Interpreter("1.1")
        val declarationNode = ASTNode(
            DataType.DECLARATION,
            "=",
            Position(1, 0),
            listOf(
                ASTNode(
                    DataType.LET_KEYWORD,
                    "flag",
                    Position(1, 1),
                    listOf(
                        ASTNode(DataType.IDENTIFIER, "flag", Position(1, 2), emptyList()),
                        ASTNode(DataType.BOOLEAN_TYPE, "boolean", Position(1, 3), emptyList())
                    )
                ),
                ASTNode(DataType.BOOLEAN_LITERAL, "true", Position(1, 4), emptyList())
            )
        )
        interpreter.interpret(declarationNode)
        assertEquals(true, interpreter.resolveVariable("flag"))
    }

//    @Test
//    fun `redeclaration of variable throws exception`() {
//        val interpreter = Interpreter("1.1")
//        val declarationNode = ASTNode(
//            DataType.DECLARATION,
//            "var",
//            Position(1, 0),
//            listOf(
//                ASTNode(DataType.IDENTIFIER, "x", Position(1, 1), emptyList()),
//                ASTNode(DataType.NUMBER_TYPE, "number", Position(1, 2), emptyList())
//            )
//        )
//        interpreter.interpret(declarationNode)
//
//        val redeclarationNode = ASTNode(
//            DataType.DECLARATION,
//            "var",
//            Position(2, 0),
//            listOf(
//                ASTNode(DataType.IDENTIFIER, "x", Position(2, 1), emptyList()),
//                ASTNode(DataType.STRING_TYPE, "string", Position(2, 2), emptyList())
//            )
//        )
//
//        assertThrows<IllegalStateException> {
//            interpreter.interpret(redeclarationNode)
//        }
//    }
}
