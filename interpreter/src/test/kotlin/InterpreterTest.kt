package src.main.model.tools.interpreter.interpreter

import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class InterpreterTest {

    private fun node(content: String, children: List<ASTNode> = emptyList()) =
        ASTNode(type = null, content = content, position = Position(1, 1), children)

    @Test
    fun `determineAction arithmetic`() {
        val interpreter = Interpreter("1.0")

        assertEquals(Actions.ADD, interpreter.determineAction(node("+")))
        assertEquals(Actions.SUBTRACT, interpreter.determineAction(node("-")))
        assertEquals(Actions.MULTIPLY, interpreter.determineAction(node("*")))
        assertEquals(Actions.DIVIDE, interpreter.determineAction(node("/")))
    }

    @Test
    fun `determineAction keywords`() {
        val interpreter = Interpreter("1.1")

        assertEquals(Actions.PRINT, interpreter.determineAction(node("print")))
        assertEquals(Actions.VAR_DECLARATION, interpreter.determineAction(node("var")))
        assertEquals(Actions.VAR_DECLARATION_AND_ASSIGNMENT, interpreter.determineAction(node("=")))
        assertEquals(Actions.IF_STATEMENT, interpreter.determineAction(node("if")))
        assertEquals(Actions.READ_INPUT, interpreter.determineAction(node("readInput")))
        assertEquals(Actions.READ_ENV, interpreter.determineAction(node("readEnv")))
    }

    @Test
    fun `determineAction unknown token throws`() {
        val interpreter = Interpreter("1.0")
        val ex = assertThrows(IllegalArgumentException::class.java) {
            interpreter.determineAction(node("???"))
        }
        assertTrue(ex.message!!.contains("Unknown action"))
    }

    @Test
    fun `unsupported action in 1_0 should throw`() {
        val interpreter = Interpreter("1.0")

        val ex = assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(node("if"), Actions.IF_STATEMENT)
        }
        assertTrue(ex.message!!.contains("not supported"))
    }

    @Test
    fun `if statement works in 1_1`() {
        val interpreter = Interpreter("1.1")

        val result = interpreter.interpret(node("if"), Actions.IF_STATEMENT)
        assertNotNull(result)
    }
}
