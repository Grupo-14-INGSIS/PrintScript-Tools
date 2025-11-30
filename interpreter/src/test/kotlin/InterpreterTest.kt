package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import interpreter.src.main.kotlin.Interpreter
import interpreter.src.main.kotlin.Actions

import tokendata.src.main.kotlin.DataType

class InterpreterTest {

    private fun node(content: String, type: DataType, children: List<ASTNode> = emptyList()) =
        ASTNode(type = type, content = content, position = Position(1, 1), children)

    @Test
    fun `determineAction arithmetic`() {
        val interpreter = Interpreter("1.0")

        assertEquals(Actions.ADD, interpreter.determineAction(node("+", DataType.ADDITION)))
        assertEquals(Actions.SUBTRACT, interpreter.determineAction(node("-", DataType.SUBTRACTION)))
        assertEquals(Actions.MULTIPLY, interpreter.determineAction(node("*", DataType.MULTIPLICATION)))
        assertEquals(Actions.DIVIDE, interpreter.determineAction(node("/", DataType.DIVISION)))
    }

    @Test
    fun `determineAction keywords`() {
        val interpreter = Interpreter("1.1")

        assertEquals(Actions.PRINT, interpreter.determineAction(node("println", DataType.PRINTLN)))

        val letNode = node("let", DataType.LET_KEYWORD)
        assertEquals(Actions.VAR_DECLARATION_AND_ASSIGNMENT, interpreter.determineAction(node("=", DataType.DECLARATION, listOf(letNode))))

        val constNode = node("const", DataType.CONST_KEYWORD)
        assertEquals(
            Actions.CONST_DECLARATION_AND_ASSIGNMENT,
            interpreter.determineAction(node("=", DataType.DECLARATION, listOf(constNode)))
        )

        assertEquals(Actions.ASSIGNMENT_TO_EXISTING_VAR, interpreter.determineAction(node("=", DataType.ASSIGNATION)))
        assertEquals(Actions.IF_STATEMENT, interpreter.determineAction(node("if", DataType.IF_STATEMENT)))
        assertEquals(Actions.READ_INPUT, interpreter.determineAction(node("readInput", DataType.FUNCTION_CALL)))
        assertEquals(Actions.READ_ENV, interpreter.determineAction(node("readEnv", DataType.FUNCTION_CALL)))
    }

    @Test
    fun `determineAction unknown token throws`() {
        val interpreter = Interpreter("1.0")
        val ex = assertThrows(IllegalArgumentException::class.java) {
            interpreter.determineAction(node("???", DataType.INVALID))
        }
        assertTrue(ex.message!!.contains("Unknown action"))
    }

    @Test
    fun `unsupported action in 1_0 should throw`() {
        val interpreter = Interpreter("1.0")

        val ex = assertThrows(IllegalArgumentException::class.java) {
            interpreter.interpret(node("if", DataType.IF_STATEMENT))
        }
        assertTrue(ex.message!!.contains("not supported"))
    }

    @Test
    fun `if statement works in 1_1`() {
        val interpreter = Interpreter("1.1")
        val ifNode = node("if", DataType.IF_STATEMENT, listOf(node("true", DataType.BOOLEAN_LITERAL), node("42", DataType.NUMBER_LITERAL)))
        val result = interpreter.interpret(ifNode)
        assertEquals(42, result)
    }
}
