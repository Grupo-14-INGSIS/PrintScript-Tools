package globaltests.src.test.kotlin

import interpreter.src.main.kotlin.Interpreter
import lexer.src.main.kotlin.Lexer
import lexer.src.main.kotlin.StringCharSource
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import parser.src.main.kotlin.Parser
import inputprovider.src.main.kotlin.ConsoleInputProvider

class ValidationTest {

    private fun execute(input: String, version: String) {
        val lexer = Lexer(StringCharSource(input), version)
        val statements = lexer.lexIntoStatements()
        val interpreter = Interpreter(version, ConsoleInputProvider())
        for (statement in statements) {
            val parser = Parser(statement, version)
            val ast = parser.parse()
            interpreter.interpret(ast)
        }
    }

    @Test
    fun `v1_0 invalid const declaration`() {
        val sourceCode = """const a: string = "constant declaration should not be allowed in version 1.0";"""
        assertThrows<Exception> {
            execute(sourceCode, "1.0")
        }
    }

    @Test
    fun `v1_0 invalid expression for type`() {
        val sourceCode = """let pi: number = "hola";"""
        assertThrows<Exception> {
            execute(sourceCode, "1.0")
        }
    }

    @Test
    fun `v1_0 invalid if statement`() {
        val sourceCode = """let a: number = 21;
if(a) {
    println("if should not be supported in version 1.0");
}"""
        assertThrows<Exception> {
            execute(sourceCode, "1.0")
        }
    }

    @Test
    fun `v1_0 invalid missing semi colon`() {
        val sourceCode = """println(5)"""
        assertThrows<Exception> {
            execute(sourceCode, "1.0")
        }
    }

    @Test
    fun `v1_0 invalid string arithmetic op`() {
        val sourceCode = """let result: string = "string" * 5;
println(result);"""
        assertThrows<Exception> {
            execute(sourceCode, "1.0")
        }
    }

    @Test
    fun `v1_0 valid arithmetic ops`() {
        val sourceCode = """let cuenta: number = 5*5-8/4+2;"""
        assertDoesNotThrow {
            execute(sourceCode, "1.0")
        }
    }

    @Test
    fun `v1_1 invalid argument in if`() {
        val sourceCode = """let a: number = 21;
if(a) {
    println("this should fail, invalid argument in if statement");
}"""
        assertThrows<Exception> {
            execute(sourceCode, "1.1")
        }
    }

    @Test
    fun `v1_1 invalid const re assign`() {
        val sourceCode = """const b: number = 5;
b = 2;"""
        assertThrows<Exception> {
            execute(sourceCode, "1.1")
        }
    }

    @Test
    fun `v1_1 invalid expression for type`() {
        val sourceCode = """let pi: number = "hola";"""
        assertThrows<Exception> {
            execute(sourceCode, "1.1")
        }
    }

    @Test
    fun `v1_1 invalid missing semi colon`() {
        val sourceCode = """println(5)"""
        assertThrows<Exception> {
            execute(sourceCode, "1.1")
        }
    }

    @Test
    fun `v1_1 invalid string arithmetic op`() {
        val sourceCode = """let result: string = "string" * 5;
println(result);"""
        assertThrows<Exception> {
            execute(sourceCode, "1.1")
        }
    }

    @Test
    fun `v1_1 valid arithmetic ops`() {
        val sourceCode = """let cuenta: number = 5*5-8/4+2;"""
        assertDoesNotThrow {
            execute(sourceCode, "1.1")
        }
    }

    @Test
    fun `v1_1 valid const declaration`() {
        val sourceCode = """const b: string = "this should be valid in 1.1";"""
        assertDoesNotThrow {
            execute(sourceCode, "1.1")
        }
    }
}
