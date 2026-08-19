package linter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
import tokendata.src.main.kotlin.Position
import linter.src.main.kotlin.rules.PrintLnRule
import linter.src.main.kotlin.LintError
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PrintLnRuleTest {

    private fun node(type: ASTNodeType, content: String = "", line: Int = 1, column: Int = 1, children: List<ASTNode> = emptyList()) =
        ASTNode(type, content, Position(line, column), children)

    @Test
    fun `valid println with identifier`() {
        val arg = node(ASTNodeType.IDENTIFIER, "value")
        val printlnNode = node(ASTNodeType.PRINTLN, children = listOf(arg))
        val rule = PrintLnRule()
        val errors = rule.apply(printlnNode)
        assertEquals(emptyList<LintError>(), errors)
    }

    @Test
    fun `valid println with string literal`() {
        val arg = node(ASTNodeType.STRING_LITERAL, "\"hello\"")
        val printlnNode = node(ASTNodeType.PRINTLN, children = listOf(arg))
        val rule = PrintLnRule()
        val errors = rule.apply(printlnNode)
        assertEquals(emptyList<LintError>(), errors)
    }
}
