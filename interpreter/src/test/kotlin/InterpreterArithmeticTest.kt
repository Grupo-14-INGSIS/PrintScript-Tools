package interpreter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
import ast.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertEquals
import interpreter.src.main.kotlin.Interpreter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class InterpreterArithmeticTest {

    @ParameterizedTest(name = "test {0} operation: 10.0 {1} 5.0 = {2}")
    @MethodSource("provideArithmeticNodes")
    fun `test arithmetic operations`(type: ASTNodeType, op: String, expectedResult: Double) {
        val interpreter = Interpreter("1.1")
        val node = createNode(type, op, "10.0", "5.0")

        val result = interpreter.interpret(node)
        assertEquals(expectedResult, result)
    }

    @Test
    fun `test division by zero throws exception`() {
        val interpreter = Interpreter("1.1")
        val divideNode = createNode(ASTNodeType.DIVISION, "/", "10.0", "0.0")

        assertThrows<IllegalArgumentException> {
            interpreter.interpret(divideNode)
        }
    }

    companion object {
        @JvmStatic
        fun provideArithmeticNodes(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(ASTNodeType.ADDITION, "+", 15.0),
                Arguments.of(ASTNodeType.SUBTRACTION, "-", 5.0),
                Arguments.of(ASTNodeType.MULTIPLICATION, "*", 50.0),
                Arguments.of(ASTNodeType.DIVISION, "/", 2.0)
            )
        }

        private fun createNode(type: ASTNodeType, op: String, left: String, right: String): ASTNode {
            return ASTNode(
                type,
                op,
                Position(1, 0),
                listOf(
                    ASTNode(ASTNodeType.NUMBER_LITERAL, left, Position(1, 1), emptyList()),
                    ASTNode(ASTNodeType.NUMBER_LITERAL, right, Position(1, 2), emptyList())
                )
            )
        }
    }
}
