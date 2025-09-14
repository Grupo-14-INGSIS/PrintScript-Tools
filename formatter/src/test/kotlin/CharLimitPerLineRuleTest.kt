package formatter.src.test.kotlin

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.optional.CharLimitPerLineRule
import formatter.src.main.kotlin.formatrule.optional.ClassNameCamelCaseRule
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CharLimitPerLineRuleTest {
    @Test
    fun `should annotate lines exceeding character limit`() {
        val longLine = Token(type = DataType.STRING_LITERAL, content = "x".repeat(150), position = Position(1, 0))
        val lineBreak = Token(type = DataType.LINE_BREAK, content = "\n", position = Position(1, 150))
        var container = Container()
        container = container.addAll(listOf(longLine, lineBreak))

        val rule = CharLimitPerLineRule(maxLength = 140)
        val result = rule.format(container)

        assertTrue(result.container.any { it.content.contains("exceeds 140 chars") })
    }

    @Test
    fun `should annotate class name not in CamelCase`() {
        val declaration = Token(type = DataType.DECLARATION, content = "class", position = Position(1, 0))
        val identifier = Token(type = DataType.IDENTIFIER, content = "my_class", position = Position(1, 6))
        var container = Container()
        container = container.addAll(listOf(declaration, identifier))

        val rule = ClassNameCamelCaseRule()
        val result = rule.format(container)

        assertTrue(result.container.any { it.content.contains("should follow CamelCase") })
    }
}
