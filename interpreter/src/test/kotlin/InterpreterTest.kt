package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import interpreter.src.main.kotlin.Interpreter
import interpreter.src.main.kotlin.Actions


class InterpreterTest {

    private fun node(content: String, type: ASTNodeType, children: List<ASTNode> = emptyList()) =
        ASTNode(type = type, content = content, position = Position(1, 1), children)

    @Test
    fun `determineAction arithmetic`() {
        val interpreter = Interpreter("1.0")

        assertEquals(Actions.ADD, interpreter.determineAction(node("+", ASTNodeType.ADDITION)))
        assertEquals(Actions.SUBTRACT, interpreter.determineAction(node("-", ASTNodeType.SUBTRACTION)))
        assertEquals(Actions.MULTIPLY, interpreter.determineAction(node("*", ASTNodeType.MULTIPLICATION)))
        assertEquals(Actions.DIVIDE, interpreter.determineAction(node("/", ASTNodeType.DIVISION)))
    }

    @Test
    fun `determineAction keywords`() {
        val interpreter = Interpreter("1.1")

        assertEquals(Actions.PRINT, interpreter.determineAction(node("println", ASTNodeType.PRINTLN)))

        val letNode = node("let", ASTNodeType.LET_KEYWORD)
        assertEquals(
            Actions.VAR_DECLARATION_AND_ASSIGNMENT,
            interpreter.determineAction(node("=", ASTNodeType.DECLARATION, listOf(letNode)))
        )

        val constNode = node("const", ASTNodeType.CONST_KEYWORD)
        assertEquals(
            Actions.CONST_DECLARATION_AND_ASSIGNMENT,
            interpreter.determineAction(node("=", ASTNodeType.DECLARATION, listOf(constNode)))
        )

        assertEquals(Actions.ASSIGNMENT_TO_EXISTING_VAR, interpreter.determineAction(node("=", ASTNodeType.ASSIGNATION)))
        assertEquals(Actions.IF_STATEMENT, interpreter.determineAction(node("if", ASTNodeType.IF_STATEMENT)))
        assertEquals(Actions.READ_INPUT, interpreter.determineAction(node("readInput", ASTNodeType.FUNCTION_CALL)))
        assertEquals(Actions.READ_ENV, interpreter.determineAction(node("readEnv", ASTNodeType.FUNCTION_CALL)))
    }

    @Test
    fun `determineAction unknown token throws`() {
        val interpreter = Interpreter("1.0")
        val ex = assertThrows(IllegalArgumentException::class.java) {
            interpreter.determineAction(node("???", ASTNodeType.INVALID))
        }
        assertTrue(ex.message!!.contains("Unknown action"))
    }

    @Test
    fun `unsupported action in 1_0 should throw`() {
        val interpreter = Interpreter("1.0")

        val ex = assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(node("if", ASTNodeType.IF_STATEMENT))
        }
        assertTrue(ex.message!!.contains("not supported"))
    }

    @Test
    fun `if statement works in 1_1`() {
        val interpreter = Interpreter("1.1")
        val ifNode = node(
            "if",
            ASTNodeType.IF_STATEMENT,
            listOf(node("true", ASTNodeType.BOOLEAN_LITERAL), node("42", ASTNodeType.NUMBER_LITERAL))
        )
        val result = interpreter.interpret(ifNode)
        assertEquals(42, result)
    }
}


