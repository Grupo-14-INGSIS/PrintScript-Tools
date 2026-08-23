package globaltests.src.test.kotlin

import interpreter.src.main.kotlin.Interpreter
import lexer.src.main.kotlin.Lexer
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.src.main.kotlin.Parser

class EndToEndNonHappyPathTest {

    private fun runPipeline(source: String, version: String = "1.0") {
        val lexer = Lexer.from(source, version)
        val statements = lexer.lexIntoStatements().toList()
        val interpreter = Interpreter(version)

        for (statement in statements) {
            val parser = Parser(statement, version)
            val ast = parser.parse()
            interpreter.interpret(ast)
        }
    }

    @Test
    fun `test division by zero throws runtime exception`() {
        val code = """
            let a: number = 10;
            let b: number = 0;
            let c: number = a / b;
        """.trimIndent()

        val exception = assertThrows(IllegalArgumentException::class.java) {
            runPipeline(code, "1.0")
        }
        assertTrue(exception.message?.contains("divide by zero", ignoreCase = true) == true)
    }

    @Test
    fun `test reassigning constant variable throws illegal state exception in 1_1`() {
        val code = """
            const pi: number = 3.14;
            pi = 3.1416;
        """.trimIndent()

        val exception = assertThrows(IllegalStateException::class.java) {
            runPipeline(code, "1.1")
        }
        assertTrue(exception.message?.contains("Cannot reassign", ignoreCase = true) == true)
    }

    @Test
    fun `test accessing undeclared variable throws exception`() {
        val code = """
            let a: number = 10;
            let b: number = a + undeclaredVar;
        """.trimIndent()

        val exception = assertThrows(IllegalStateException::class.java) {
            runPipeline(code, "1.0")
        }
        assertTrue(exception.message?.contains("not declared", ignoreCase = true) == true)
    }

    @Test
    fun `test assigning incompatible type to existing variable throws exception`() {
        val code = """
            let a: number = 5;
            a = "not a number";
        """.trimIndent()

        val exception = assertThrows(IllegalArgumentException::class.java) {
            runPipeline(code, "1.0")
        }
        assertTrue(
            exception.message?.contains("no se puede convertir", ignoreCase = true) == true ||
                exception.message?.contains("not compatible", ignoreCase = true) == true
        )
    }
}
