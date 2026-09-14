package lexer.src.test.kotlin

import tokendata.src.main.kotlin.DataType
import token.src.main.kotlin.Token
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import lexer.src.main.kotlin.Lexer

class IntegrationTest {

    @Test
    fun `test complete variable declaration with let using string`() {
        val input = "let x: number;"
        val lexer = Lexer.from(input)
        val container = lexer.lexIntoStatements().toList().first()

        assertTokensMatch(
            container.container,
            DataType.LET_KEYWORD to "let",
            DataType.SPACE to " ",
            DataType.IDENTIFIER to "x",
            DataType.COLON to ":",
            DataType.SPACE to " ",
            DataType.NUMBER_TYPE to "number",
            DataType.SEMICOLON to ";"
        )
    }

    @Test
    fun `test same input using file`() {
        val input = "let x: number;"
        val tempFile = createTempFile("test", ".txt")
        tempFile.writeText(input)

        val lexer = Lexer.from(tempFile)
        val container = lexer.lexIntoStatements().toList().first()

        assertTokensMatch(
            container.container,
            DataType.LET_KEYWORD to "let",
            DataType.SPACE to " ",
            DataType.IDENTIFIER to "x",
            DataType.COLON to ":",
            DataType.SPACE to " ",
            DataType.NUMBER_TYPE to "number",
            DataType.SEMICOLON to ";"
        )

        tempFile.delete()
    }

    @Test
    fun `test variable declaration and assignment with number`() {
        val input = "let x: number = 42;"
        val lexer = Lexer.from(input)
        val container = lexer.lexIntoStatements().toList().first()

        assertTokensMatch(
            container.container,
            DataType.LET_KEYWORD to "let",
            DataType.SPACE to " ",
            DataType.IDENTIFIER to "x",
            DataType.COLON to ":",
            DataType.SPACE to " ",
            DataType.NUMBER_TYPE to "number",
            DataType.SPACE to " ",
            DataType.ASSIGNATION to "=",
            DataType.SPACE to " ",
            DataType.NUMBER_LITERAL to "42",
            DataType.SEMICOLON to ";"
        )
    }

    @Test
    fun `test arithmetic expression`() {
        val input = "x + y * 2;"
        val lexer = Lexer.from(input)
        val container = lexer.lexIntoStatements().toList().first()

        assertTokensMatch(
            container.container,
            DataType.IDENTIFIER to "x",
            DataType.SPACE to " ",
            DataType.ADDITION to "+",
            DataType.SPACE to " ",
            DataType.IDENTIFIER to "y",
            DataType.SPACE to " ",
            DataType.MULTIPLICATION to "*",
            DataType.SPACE to " ",
            DataType.NUMBER_LITERAL to "2",
            DataType.SEMICOLON to ";"
        )
    }

    @Test
    fun `test file processing with custom buffer size`() {
        val content = "let x: number = 42;\n".repeat(100)
        val tempFile = createTempFile("large_test", ".txt")
        tempFile.writeText(content.trim())

        val lexer = Lexer.from(tempFile)
        val statements = lexer.lexIntoStatements().toList()

        assertEquals(100, statements.size)

        val letTokens = statements.flatMap { it.container }.filter { it.type == DataType.LET_KEYWORD }
        assertEquals(100, letTokens.size)

        tempFile.delete()
    }

    @Test
    fun `test lexer splits multiple statements correctly`() {
        val input = "let x = 1; let y = 2;"
        val lexer = Lexer.from(input)
        val statements = lexer.lexIntoStatements().toList()

        assertEquals(2, statements.size)

        assertTokensMatch(
            statements[0].container,
            DataType.LET_KEYWORD to "let",
            DataType.SPACE to " ",
            DataType.IDENTIFIER to "x",
            DataType.SPACE to " ",
            DataType.ASSIGNATION to "=",
            DataType.SPACE to " ",
            DataType.NUMBER_LITERAL to "1",
            DataType.SEMICOLON to ";"
        )

        assertTokensMatch(
            statements[1].container,
            DataType.SPACE to " ",
            DataType.LET_KEYWORD to "let",
            DataType.SPACE to " ",
            DataType.IDENTIFIER to "y",
            DataType.SPACE to " ",
            DataType.ASSIGNATION to "=",
            DataType.SPACE to " ",
            DataType.NUMBER_LITERAL to "2",
            DataType.SEMICOLON to ";"
        )
    }

    private fun assertTokensMatch(actualTokens: List<Token>, vararg expected: Pair<DataType, String>) {
        assertEquals(expected.size, actualTokens.size, "El tamaño de los tokens no coincide")
        expected.forEachIndexed { index, (expectedType, expectedContent) ->
            assertEquals(expectedType, actualTokens[index].type, "Falla el tipo en índice \$index")
            assertEquals(expectedContent, actualTokens[index].content, "Falla el contenido en índice \$index")
        }
    }
}
