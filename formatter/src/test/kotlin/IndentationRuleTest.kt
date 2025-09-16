package formatter.src.test.kotlin
import formatter.src.main.kotlin.formatrule.optional.IndentationRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tokendata.src.main.kotlin.DataType

class IndentationRuleTest {
    @Test
    fun `ignores line break at the end of file`() {
        val source = containerOf(
            token(
                DataType.IDENTIFIER,
                "x"
            ),
            token(
                DataType.LINE_BREAK,
                "\n"
            )
        )

        val rule = IndentationRule(indentSize = 2)
        val result = rule.format(source)

        // No debería agregar espacios inútiles después de un salto final
        assertEquals("\n", result.last()!!.content)
    }

    @Test
    fun `does not break tokens without line breaks`() {
        val source = containerOf(
            token(
                DataType.IDENTIFIER,
                "x"
            ),
            token(
                DataType.SPACE,
                " "
            ),
            token(
                DataType.IDENTIFIER,
                "y"
            )
        )

        val rule = IndentationRule(indentSize = 2)
        val result = rule.format(source)

        // No debería tocar nada si no hay saltos de línea
        assertEquals("x", result.get(0)!!.content)
        assertEquals(" ", result.get(1)!!.content)
        assertEquals("y", result.get(2)!!.content)
    }
}
