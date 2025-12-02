package lexer.src.test.kotlin

import tokendata.src.main.kotlin.DataType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import lexer.src.main.kotlin.Lexer

class IntegrationTest {

    @Test
    fun `test complete variable declaration with let using string`() {
        val input = "let x: number;"
        val lexer = Lexer.from(input) // Directamente con string

        val container = lexer.lexIntoStatements().toList().first()

        assertEquals(7, container.container.size)

        with(container.container) {
            assertEquals(DataType.LET_KEYWORD, get(0).type)
            assertEquals("let", get(0).content)
            assertEquals(DataType.SPACE, get(1).type)
            assertEquals(" ", get(1).content)
            assertEquals(DataType.IDENTIFIER, get(2).type)
            assertEquals("x", get(2).content)
            assertEquals(DataType.COLON, get(3).type)
            assertEquals(":", get(3).content)
            assertEquals(DataType.SPACE, get(4).type)
            assertEquals(" ", get(4).content)
            assertEquals(DataType.NUMBER_TYPE, get(5).type)
            assertEquals("number", get(5).content)
            assertEquals(DataType.SEMICOLON, get(6).type)
            assertEquals(";", get(6).content)
        }
    }

    @Test
    fun `test same input using file`() {
        val input = "let x: number;"
        val tempFile = createTempFile("test", ".txt")
        tempFile.writeText(input)

        val lexer = Lexer.from(tempFile) // Directamente con file
        val container = lexer.lexIntoStatements().toList().first()

        assertEquals(7, container.container.size)

        // Debe dar exactamente el mismo resultado que con string
        with(container.container) {
            assertEquals(DataType.LET_KEYWORD, get(0).type)
            assertEquals("let", get(0).content)
        }

        tempFile.delete()
    }

    @Test
    fun `test variable declaration and assignment with number`() {
        val input = "let x: number = 42;"
        val lexer = Lexer.from(input)

        val container = lexer.lexIntoStatements().toList().first()

        assertEquals(11, container.container.size)

        with(container.container) {
            assertEquals(DataType.LET_KEYWORD, get(0).type)
            assertEquals(DataType.IDENTIFIER, get(2).type)
            assertEquals(DataType.COLON, get(3).type)
            assertEquals(DataType.NUMBER_TYPE, get(5).type)
            assertEquals(DataType.ASSIGNATION, get(7).type)
            assertEquals(DataType.NUMBER_LITERAL, get(9).type)
            assertEquals("42", get(9).content)
            assertEquals(DataType.SEMICOLON, get(10).type)
        }
    }

    @Test
    fun `test arithmetic expression`() {
        val input = "x + y * 2;"
        val lexer = Lexer.from(input)

        val container = lexer.lexIntoStatements().toList().first()

        assertEquals(10, container.container.size)

        with(container.container) {
            assertEquals(DataType.IDENTIFIER, get(0).type)
            assertEquals("x", get(0).content)
            assertEquals(DataType.ADDITION, get(2).type)
            assertEquals(DataType.IDENTIFIER, get(4).type)
            assertEquals("y", get(4).content)
            assertEquals(DataType.MULTIPLICATION, get(6).type)
            assertEquals(DataType.NUMBER_LITERAL, get(8).type)
            assertEquals("2", get(8).content)
        }
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

        // Check first statement: "let x = 1;"
        val firstStatement = statements[0]
        assertEquals(DataType.LET_KEYWORD, firstStatement.container[0].type)
        assertEquals("x", firstStatement.container[2].content)
        assertEquals("1", firstStatement.container[6].content)
        assertEquals(DataType.SEMICOLON, firstStatement.container.last().type)

        // Check second statement: " let y = 2;" (note the leading space)
        val secondStatement = statements[1]
        assertEquals(DataType.SPACE, secondStatement.container[0].type)
        assertEquals(DataType.LET_KEYWORD, secondStatement.container[1].type)
        assertEquals("y", secondStatement.container[3].content)
        assertEquals("2", secondStatement.container[7].content)
        assertEquals(DataType.SEMICOLON, secondStatement.container.last().type)
    }
}
