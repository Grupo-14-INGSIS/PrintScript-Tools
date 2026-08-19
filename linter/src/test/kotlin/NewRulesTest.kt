package linter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
import linter.src.main.kotlin.rules.ImmutableValRule
import linter.src.main.kotlin.rules.IfWithoutElseRule
import tokendata.src.main.kotlin.Position
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class NewRulesTest {
    @Test
    fun `should report var never reassigned`() {
        val declarationNode = ASTNode(
            ASTNodeType.DECLARATION,
            "var",
            Position(1, 0),
            listOf(
                ASTNode(
                    ASTNodeType.IDENTIFIER,
                    "counter",
                    Position(1, 4),
                    listOf()
                )
            )
        )

        val root = ASTNode(ASTNodeType.INVALID, "", Position(0, 0), listOf(declarationNode))
        val rule = ImmutableValRule()
        val errors = rule.apply(root)

        assertTrue(errors.any { it.message.contains("never reassigned") })
    }

    @Test
    fun `should report if without else and no control flow`() {
        val thenBlock = ASTNode(
            ASTNodeType.BLOCK,
            "block",
            Position(1, 3),
            listOf(
                ASTNode(ASTNodeType.IDENTIFIER, "doSomething", Position(1, 4), emptyList())
            )
        )
        val ifNode = ASTNode(
            ASTNodeType.IF_KEYWORD,
            "if",
            Position(1, 0),
            listOf(
                ASTNode(ASTNodeType.BOOLEAN_LITERAL, "true", Position(1, 1), emptyList()),
                thenBlock
            )
        )

        val root = ASTNode(ASTNodeType.INVALID, "", Position(0, 0), listOf(ifNode))
        val rule = IfWithoutElseRule()
        val errors = rule.apply(root)

        assertTrue(errors.any { it.message.contains("lacks 'else'") })
    }
}
