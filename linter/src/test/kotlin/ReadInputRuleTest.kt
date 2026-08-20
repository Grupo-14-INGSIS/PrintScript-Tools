package linter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
import tokendata.src.main.kotlin.Position
import linter.src.main.kotlin.rules.ReadInputRule
import linter.src.main.kotlin.LintError
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ReadInputRuleTest {

    private fun node(type: ASTNodeType, content: String = "", line: Int = 1, column: Int = 1, children: List<ASTNode> = emptyList()) =
        ASTNode(type, content, Position(line, column), children)

    @Test
    fun `valid readInput with identifier`() {
        val arg = node(ASTNodeType.IDENTIFIER, "userInput")
        val readInputNode = node(ASTNodeType.READ_INPUT, children = listOf(arg))
        val rule = ReadInputRule()
        val errors = rule.apply(readInputNode)
        assertEquals(emptyList<LintError>(), errors)
    }

    @Test
    fun `valid readInput with string literal`() {
        val arg = node(ASTNodeType.STRING_LITERAL, "\"name\"")
        val readInputNode = node(ASTNodeType.READ_INPUT, children = listOf(arg))
        val rule = ReadInputRule()
        val errors = rule.apply(readInputNode)
        assertEquals(emptyList<LintError>(), errors)
    }
}
