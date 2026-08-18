package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import interpreter.src.main.kotlin.ActionType
import interpreter.src.main.kotlin.Actions
import interpreter.src.main.kotlin.Environment
import interpreter.src.main.kotlin.ExecutionContext
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

class EnvironmentTest {

    @Test
    fun `Environment handles nested scopes properly`() {
        val env = Environment()
        env.declareVariable("a", 10.0, "number")
        assertEquals(10.0, env.resolveVariable("a"))

        env.enterScope()
        env.declareVariable("b", "local", "string")
        assertEquals(10.0, env.resolveVariable("a"))
        assertEquals("local", env.resolveVariable("b"))

        env.exitScope()
        assertEquals(10.0, env.resolveVariable("a"))
        assertThrows(IllegalStateException::class.java) {
            env.resolveVariable("b")
        }
    }

    @Test
    fun `Environment prevents reassigning constants`() {
        val env = Environment()
        env.declareConstant("PI", 3.14159, "number")
        assertEquals(3.14159, env.resolveVariable("PI"))

        assertThrows(IllegalStateException::class.java) {
            env.assignVariable("PI", 3.0)
        }
    }

    @Test
    fun `Interpreter supports custom action registration fulfilling OCP`() {
        val interpreter = Interpreter("1.0")

        val customActionHandler = object : ActionType {
            override fun interpret(node: ASTNode, interpreter: ExecutionContext): Any {
                return "CUSTOM_ACTION_RESULT_${node.content}"
            }
        }

        interpreter.registerHandler(Actions.NULL, customActionHandler)
        interpreter.registerNodeAction(DataType.INVALID, Actions.NULL)

        val testNode = ASTNode(DataType.INVALID, "HELLO", Position(1, 1), emptyList())
        val result = interpreter.interpret(testNode)

        assertEquals("CUSTOM_ACTION_RESULT_HELLO", result)
    }
}
