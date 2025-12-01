package formatter.src.test.kotlin
import container.src.main.kotlin.Container
import formatter.src.main.kotlin.formatrule.optional.IndentationRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

class IndentationRuleTest {
    @Test
    fun `ignores line break at the end of file`() {
        val source = containerOf(
            token(DataType.IDENTIFIER, "x"),
            token(DataType.LINE_BREAK, "\n")
        )

        val rule = IndentationRule(indentSize = 2)
        val result = rule.format(listOf(source)).first()

        // No debería agregar espacios inútiles después de un salto final
        assertEquals("\n", result.container.last().content)
    }

    @Test
    fun `does not break tokens without line breaks`() {
        val source = containerOf(
            token(DataType.IDENTIFIER, "x"),
            token(DataType.SPACE, " "),
            token(DataType.IDENTIFIER, "y")
        )

        val rule = IndentationRule(indentSize = 2)
        val result = rule.format(listOf(source)).first()

        // No debería tocar nada si no hay saltos de línea
        assertEquals("x", result.container[0].content)
        assertEquals(" ", result.container[1].content)
        assertEquals("y", result.container[2].content)
    }

    @Test
    fun `tck failing test for indentation`() {
        val source = containerOf(
            token(DataType.LET_KEYWORD, "let"), token(DataType.SPACE, " "),
            token(DataType.IDENTIFIER, "something"), token(DataType.COLON, ":"), token(DataType.SPACE, " "),
            token(DataType.BOOLEAN_TYPE, "boolean"), token(DataType.SPACE, " "),
            token(DataType.ASSIGNATION, "="), token(DataType.SPACE, " "),
            token(DataType.BOOLEAN_LITERAL, "true"), token(DataType.SEMICOLON, ";"), token(DataType.LINE_BREAK, "\n"),

            token(DataType.IF_KEYWORD, "if"), token(DataType.SPACE, " "),
            token(DataType.OPEN_PARENTHESIS, "("), token(DataType.IDENTIFIER, "something"), token(DataType.CLOSE_PARENTHESIS, ")"),
            token(DataType.SPACE, " "),
            token(DataType.OPEN_BRACE, "{"), token(DataType.LINE_BREAK, "\n"),

            token(DataType.SPACE, "  "), // Existing indentation in input
            token(DataType.IF_KEYWORD, "if"), token(DataType.SPACE, " "),
            token(DataType.OPEN_PARENTHESIS, "("), token(DataType.IDENTIFIER, "something"), token(DataType.CLOSE_PARENTHESIS, ")"),
            token(DataType.SPACE, " "),
            token(DataType.OPEN_BRACE, "{"), token(DataType.LINE_BREAK, "\n"),

            token(DataType.SPACE, "    "), // Existing indentation in input
            token(DataType.PRINTLN, "println"), token(DataType.OPEN_PARENTHESIS, "("),
            token(DataType.STRING_LITERAL, "\"Entered two ifs\""), token(DataType.CLOSE_PARENTHESIS, ")"), token(DataType.SEMICOLON, ";"),
            token(DataType.LINE_BREAK, "\n"),

            token(DataType.SPACE, "  "), // Existing indentation in input
            token(DataType.CLOSE_BRACE, "}"), token(DataType.LINE_BREAK, "\n"),

            token(DataType.CLOSE_BRACE, "}")
        )

        val expected = """
            let something: boolean = true;
            if (something) {
                if (something) {
                    println("Entered two ifs");
                }
            }
        """.trimIndent()

        val rule = IndentationRule(indentSize = 4)
        val formattedStatements = rule.format(listOf(source))
        val result = statementsToString(formattedStatements)

        assertEquals(expected, result)
    }

    private fun containerOf(vararg tokens: Token): Container {
        return Container(tokens.toList())
    }

    private fun token(type: DataType, content: String): Token {
        return Token(type, content, Position(0, 0))
    }

    private fun statementsToString(statements: List<Container>): String {
        return statements.joinToString(separator = "") { statement ->
            statement.container.joinToString(separator = "") { it.content }
        }
    }
}
