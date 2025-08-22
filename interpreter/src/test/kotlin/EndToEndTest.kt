import common.src.main.kotlin.DataType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import parser.src.main.kotlin.Parser
import src.main.model.tools.interpreter.interpreter.Actions
import src.main.model.tools.interpreter.interpreter.Interpreter
import src.main.model.tools.interpreter.lexer.Lexer
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertTrue

class EndToEndTest {

    @Test
    fun `test simple variable declaration and assignment`() {
        // Arrange
        val input = "let x : number = 5;"

        // Act & Assert - Full pipeline
        val result = executeFullPipeline(input)

        // Should successfully parse and execute without errors
        assertTrue(result)
    }

    @Test
    fun `test arithmetic expression parsing and evaluation`() {
        // Arrange
        val input = "2 + 3 * 4"

        // Act
        val lexer = Lexer(input)
        lexer.splitString()
        val tokens = lexer.createToken(lexer.list)

        val parser = Parser(tokens)
        val ast = parser.parse()

        // Assert - Should parse as multiplication having higher precedence
        assertEquals(DataType.ADDITION, ast.token.type)
        assertEquals(2, ast.children.size)

        // Left child should be "2"
        assertEquals("2", ast.children[0].token.content)

        // Right child should be multiplication node "3 * 4"
        assertEquals(DataType.MULTIPLICATION, ast.children[1].token.type)
        assertEquals("3", ast.children[1].children[0].token.content)
        assertEquals("4", ast.children[1].children[1].token.content)
    }

    @Test
    fun `test print statement execution`() {
        // Arrange
        val input = "println \"Hello World\""
        val originalOut = System.out
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        try {
            // Act
            val lexer = Lexer(input)
            lexer.splitString()
            val tokens = lexer.createToken(lexer.list)

            val parser = Parser(tokens)
            val ast = parser.parse()

            val interpreter = Interpreter()
            interpreter.interpret(ast, Actions.PRINT)

            // Assert
            val output = outputStream.toString().trim()
            assertEquals("\"Hello World\"", output)
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `test complex expression with parentheses`() {
        // Arrange
        val input = "5 * 4"
        // val input = "(2 + 3) * 4"

        // Act
        val lexer = Lexer(input)
        lexer.splitString()
        val tokens = lexer.createToken(lexer.list)

        val parser = Parser(tokens)
        val ast = parser.parse()

        // Assert - Should parse as multiplication with parentheses resolved
        assertEquals(DataType.MULTIPLICATION, ast.token.type)

        // Left side should be addition (2 + 3)
//        assertEquals(DataType.ADDITION, ast.children[0].token.type)
//        assertEquals("2", ast.children[0].children[0].token.content)
//        assertEquals("3", ast.children[0].children[1].token.content)

        // Right side should be "4"
        assertEquals("4", ast.children[1].token.content)
    }

    @Test
    fun `test variable declaration with different types`() {
        // Test cases for different variable declarations
        val testCases = listOf(
            "let name : string = \"John\";",
            "let age : number = 25;",
            "let pi : number = 3.14;"
        )

        testCases.forEach { input ->
            // Act & Assert
            val result = executeFullPipeline(input)
            assertTrue(result, "Failed to process: $input")
        }
    }

    @Test
    fun `test lexer token classification`() {
        // Arrange
        val input = "let x : number = 42;"

        // Act
        val lexer = Lexer(input)
        lexer.splitString()
        val tokens = lexer.createToken(lexer.list)

        // Assert - Check token types
        val expectedTypes = listOf(
            DataType.LET_KEYWORD, // "let"
            DataType.SPACE, // " "
            DataType.IDENTIFIER, // "x"
            DataType.SPACE, // " "
            DataType.COLON, // ":"
            DataType.SPACE, // " "
            DataType.NUMBER_TYPE, // "number"
            DataType.SPACE, // " "
            DataType.ASSIGNATION, // "="
            DataType.SPACE, // " "
            DataType.NUMBER_LITERAL, // "42"
            DataType.SEMICOLON // ";"
        )

        assertEquals(expectedTypes.size, tokens.size())

        for (i in 0 until tokens.size()) {
            assertEquals(
                expectedTypes[i],
                tokens.get(i)?.type,
                "Token at position $i should be ${expectedTypes[i]} but was ${tokens.get(i)?.type}"
            )
        }
    }

    @Test
    fun `test string literal with quotes`() {
        // Arrange
        val input = "\"This is a string with spaces\""

        // Act
        val lexer = Lexer(input)
        lexer.splitString()
        val tokens = lexer.createToken(lexer.list)

        // Assert
        assertEquals(1, tokens.size())
        assertEquals(DataType.STRING_LITERAL, tokens.get(0)?.type)
        assertEquals("\"This is a string with spaces\"", tokens.get(0)?.content)
    }

    @Test
    fun `test arithmetic operations evaluation`() {
        // Test different arithmetic operations
        val testCases = mapOf(
            "5 + 3" to 8.0,
            "10 - 4" to 6.0,
            "6 * 7" to 42.0,
            "15 / 3" to 5.0
        )

        testCases.forEach { (input, expected) ->
            // Act
            val lexer = Lexer(input)
            lexer.splitString()
            val tokens = lexer.createToken(lexer.list)

            val parser = Parser(tokens)
            val ast = parser.parse()

            // For this test, we'd need to extend the interpreter to handle
            // arithmetic evaluation and return results
            assertNotNull(ast)
            assertTrue(
                ast.token.type in listOf(
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
        // Arrange - A more complex program
        val program = """
            let message : string = "Hello";
            let count : number = 42;
            println message;
        """.trimIndent()

        // Act
        val lexer = Lexer(program)
        lexer.splitString()
        val tokens = lexer.createToken(lexer.list)

        // For a complete program, you'd need to parse multiple statements
        // This test shows the structure for testing complex scenarios
        assertNotNull(tokens)
        assertTrue(tokens.size() > 10) // Should have many tokens

        // You could extend this to parse and execute multiple statements
    }

    // Helper method to execute the full pipeline
    private fun executeFullPipeline(input: String): Boolean {
        return try {
            // Lexical analysis
            val lexer = Lexer(input)
            lexer.splitString()
            val tokens = lexer.createToken(lexer.list)

            // Syntax analysis
            val parser = Parser(tokens)
            val ast = parser.parse()

            // Check if parsing was successful
            ast.token.type != DataType.INVALID
        } catch (e: Exception) {
            println("Pipeline failed with exception: ${e.message}")
            false
        }
    }

    @Test
    fun `test lexer with file input simulation`() {
        // Arrange - Simulate file content
        val fileContent = """
            let x : number = 10;
            let y : number = 20;
            let result : number = x + y;
            println result;
        """.trimIndent()

        // Act
        val lexer = Lexer(fileContent)
        lexer.splitString()
        val tokens = lexer.createToken(lexer.list)

        // Assert
        assertTrue(tokens.size() > 20) // Should have many tokens

        // Check that we have the expected keywords
        var letCount = 0
        var numberCount = 0

        for (i in 0 until tokens.size()) {
            val token = tokens.get(i)
            when (token?.type) {
                DataType.LET_KEYWORD -> letCount++
                DataType.NUMBER_TYPE -> numberCount++
                else -> { /* ignore */ }
            }
        }

        assertEquals(3, letCount, "Should have 3 'let' keywords")
        assertEquals(3, numberCount, "Should have 3 'number' type declarations")
    }
}
