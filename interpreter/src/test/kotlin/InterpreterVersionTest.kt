package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
import ast.src.main.kotlin.Position
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

    @Test
    fun `test registerVersion and executeAST in Interpreter`() {
        interpreter.src.main.kotlin.Interpreter.registerVersion(
            "test_v2",
            setOf(interpreter.src.main.kotlin.Actions.PRINT, interpreter.src.main.kotlin.Actions.LITERAL)
        ) {
            interpreter.src.main.kotlin.Interpreter.defaultV10Handlers
        }

        val interp = Interpreter("test_v2")
        val ast = ASTNode(
            ASTNodeType.FUNCTION_CALL,
            "println",
            Position(1, 0),
            listOf(ASTNode(ASTNodeType.STRING_LITERAL, "hello", Position(1, 1), emptyList()))
        )
        interp.interpret(ast)

        val root = ASTNode(
            ASTNodeType.BLOCK,
            "",
            Position(0, 0),
            listOf(ast)
        )
        val outputs = interp.executeAST(root)
        org.junit.jupiter.api.Assertions.assertNotNull(outputs)
    }

    @Test
    fun `test custom action handlers constructor`() {
        val customHandlers = interpreter.src.main.kotlin.Interpreter.defaultV10Handlers
        val interp = Interpreter(customHandlers)
        org.junit.jupiter.api.Assertions.assertNotNull(interp)
    }

    @Test
    fun `test interpreter version registry methods`() {
        val registry = interpreter.src.main.kotlin.InterpreterVersionRegistry
        org.junit.jupiter.api.Assertions.assertTrue(registry.isSupported("1.0"))
        org.junit.jupiter.api.Assertions.assertTrue(registry.isSupported("1.1"))
        org.junit.jupiter.api.Assertions.assertFalse(registry.isSupported("9.9"))

        val config10 = registry.getConfig("1.0")
        org.junit.jupiter.api.Assertions.assertNotNull(config10)
        org.junit.jupiter.api.Assertions.assertTrue(
            registry.isActionSupported("1.0", interpreter.src.main.kotlin.Actions.PRINT)
        )
        org.junit.jupiter.api.Assertions.assertFalse(
            registry.isActionSupported("1.0", interpreter.src.main.kotlin.Actions.READ_INPUT)
        )
        org.junit.jupiter.api.Assertions.assertTrue(
            registry.isActionSupported("1.1", interpreter.src.main.kotlin.Actions.READ_INPUT)
        )

        val supportedVersions = registry.supportedVersions()
        org.junit.jupiter.api.Assertions.assertTrue(supportedVersions.contains("1.0"))
        org.junit.jupiter.api.Assertions.assertTrue(supportedVersions.contains("1.1"))

        assertThrows<IllegalArgumentException> {
            registry.getConfig("non_existent")
        }

        val testConfig = interpreter.src.main.kotlin.InterpreterVersionConfig(
            supportedActions = setOf(interpreter.src.main.kotlin.Actions.PRINT),
            handlerBuilder = { registry.defaultV10Handlers }
        )
        registry.register("test_v3", testConfig)
        org.junit.jupiter.api.Assertions.assertTrue(registry.isSupported("test_v3"))
        org.junit.jupiter.api.Assertions.assertEquals(testConfig, registry.getConfig("test_v3"))
    }
}


