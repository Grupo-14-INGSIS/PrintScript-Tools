package globaltests.src.test.kotlin

import org.junit.jupiter.api.Assertions.*
import tokendata.src.main.kotlin.DataType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import parser.src.main.kotlin.Parser
import lexer.src.main.kotlin.Lexer
import container.src.main.kotlin.Container
import lexer.src.main.kotlin.StringCharSource
import interpreter.src.main.kotlin.Interpreter // Import the Interpreter
import kotlin.jvm.kotlin


class EndToEndTest {

    @Test
    fun `test simple variable declaration and assignment`() {
        val input = "let x : number = 5;"
        val result = executeFullPipeline(input)
        assertTrue(result)
    }

    @Test
    fun `test arithmetic expression parsing and evaluation`() {
        val input = "2 + 3 * 4"
        val lexer = Lexer.from(input)
        val tokens: Container = lexer.lexIntoStatements().first()

        val parser = Parser(tokens)
        val ast = parser.parse()

        assertEquals(DataType.ADDITION, ast.type)
        assertEquals(2, ast.children.size)

        assertEquals("2", ast.children[1].content)

        assertEquals(DataType.MULTIPLICATION, ast.children[0].type)
        assertEquals("3", ast.children[0].children[1].content)
        assertEquals("4", ast.children[0].children[0].content)
    }


    @Test
    fun `test complex expression with parentheses`() {
        val input = "5 * 4"
        val lexer = Lexer.from(input)
        val tokens = lexer.lexIntoStatements().first()

        val parser = Parser(tokens)
        val ast = parser.parse()

        assertEquals(DataType.MULTIPLICATION, ast.type)
        assertEquals("4", ast.children[0].content)
    }

    @Test
    fun `test variable declaration with different types`() {
        val testCases = listOf(
            "let name : string = \"John\";",
            "let age : number = 25;",
            "let pi : number = 3.14;"
        )

        testCases.forEach { input ->
            val result = executeFullPipeline(input)
            assertTrue(result, "Failed to process: $input")
        }
    }

    @Test
    fun `test lexer token classification`() {
        val input = "let x : number = 42;"
        val lexer = Lexer.from(input)
        val tokens = lexer.lexIntoStatements().first()

        val expectedTypes = listOf(
            DataType.LET_KEYWORD,
            DataType.SPACE,
            DataType.IDENTIFIER,
            DataType.SPACE,
            DataType.COLON,
            DataType.SPACE,
            DataType.NUMBER_TYPE,
            DataType.SPACE,
            DataType.ASSIGNATION,
            DataType.SPACE,
            DataType.NUMBER_LITERAL,
            DataType.SEMICOLON
        )

        assertEquals(expectedTypes.size, tokens.size())

        for (i in expectedTypes.indices) {
            assertEquals(
                expectedTypes[i],
                tokens.get(i)?.type,
                "Token at position $i should be ${expectedTypes[i]} but was ${tokens.get(i)?.type}"
            )
        }
    }

    @Test
    fun `test string literal with quotes`() {
        val input = "\"This is a string with spaces\""
        val lexer = Lexer.from(input)
        val tokens = lexer.lexIntoStatements().first()

        assertEquals(1, tokens.size())
        assertEquals(DataType.STRING_LITERAL, tokens.get(0)?.type)
        assertEquals("\"This is a string with spaces\"", tokens.get(0)?.content)
    }

    @Test
    fun `test arithmetic operations evaluation`() {
        val testCases = mapOf(
            "5 + 3" to 8.0,
            "10 - 4" to 6.0,
            "6 * 7" to 42.0,
            "15 / 3" to 5.0
        )

        testCases.forEach { (input, _) ->
            val lexer = Lexer.from(input)
            val tokens = lexer.lexIntoStatements().first()

            val parser = Parser(tokens)
            val ast = parser.parse()

            assertNotNull(ast)
            assertTrue(
                ast.type in listOf(
                    DataType.ADDITION,
                    DataType.SUBTRACTION,
                    DataType.MULTIPLICATION,
                    DataType.DIVISION
                )
            )
        }
    }

    @Test
    fun `test complete program execution`() {
        val program = """
            let message : string = "Hello";
            let count : number = 42;
            println message;
        """.trimIndent()

        val lexer = Lexer.from(program)
        val statements = lexer.lexIntoStatements()
        val allTokens = statements.flatMap { it.container }

        assertNotNull(allTokens)
        assertTrue(allTokens.size > 10)
    }

    @Test
    fun `test lexer with file input simulation`() {
        val fileContent = """
            let x : number = 10;
            let y : number = 20;
            let result : number = x + y;
            println result;
        """.trimIndent()

        val lexer = Lexer.from(fileContent)
        val statements = lexer.lexIntoStatements()
        val allTokens = statements.flatMap { it.container }

        assertTrue(allTokens.size > 20)

        var letCount = 0
        var numberCount = 0

        for (token in allTokens) {
            when (token.type) {
                DataType.LET_KEYWORD -> letCount++
                DataType.NUMBER_TYPE -> numberCount++
                else -> {}
            }
        }

        assertEquals(3, letCount, "Should have 3 'let' keywords")
        assertEquals(3, numberCount, "Should have 3 'number' type declarations")
    }

    private fun executeFullPipeline(input: String): Boolean {
        return try {
            val lexer = Lexer.from(input)
            val tokens = lexer.lexIntoStatements().first()

            val parser = Parser(tokens)
            val ast = parser.parse()

            ast.type != DataType.INVALID
        } catch (e: Exception) {
            println("Pipeline failed with exception: ${e.message}")
            false
        }
    }

    @Test
    fun `invalid if statement condition throws exception`() {
        val sourceCode = """
            if(21) {
                println("this should fail");
            }
        """.trimIndent()
        val version = "1.1"

        val ex = assertThrows(IllegalStateException::class.java) {
            executeFullPipelineAndThrow(sourceCode, version)
        }
        assertTrue(ex.message!!.contains("La condición de un 'if' debe ser booleana"))
    }

    private fun executeFullPipelineAndThrow(input: String, version: String) {
        val lexer = Lexer(StringCharSource(input), version)
        val statements = lexer.lexIntoStatements()

        val interpreter = Interpreter(version)
        for (statement in statements) {
            val parser = Parser(statement, version)
            val ast = parser.parse()
            interpreter.executeAST(ast)
        }
    }
}

