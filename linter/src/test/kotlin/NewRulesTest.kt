import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import linter.rules.IfWithoutElseRule
import linter.rules.ImmutableValRule
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

class NewRulesTest {
    @Test
    fun `should report var never reassigned`() {
        val declarationNode = ASTNode(DataType.DECLARATION, "var", Position(1, 0), listOf(
            ASTNode(DataType.IDENTIFIER, "counter", Position(1, 4), listOf()
        )))

        val root = ASTNode(DataType.INVALID, "", Position(0, 0), listOf(declarationNode))
        val rule = ImmutableValRule()
        val errors = rule.apply(root)

        assertTrue(errors.any { it.message.contains("never reassigned") })
    }

    @Test
    fun `should report if without else and no control flow`() {
        val thenBlock = ASTNode(DataType.OPEN_BRACE, "{", Position(1, 3), listOf(
            ASTNode(DataType.IDENTIFIER, "doSomething", Position(1, 4), emptyList()),
            ASTNode(DataType.CLOSE_BRACE, "}", Position(1, 15), emptyList())))
        val ifNode = ASTNode(DataType.IF_KEYWORD, "if", Position(1, 0), listOf(
            thenBlock))

        val root = ASTNode(DataType.INVALID, "", Position(0, 0), listOf(ifNode))
        val rule = IfWithoutElseRule()
        val errors = rule.apply(root)

        assertTrue(errors.any { it.message.contains("lacks 'else'") })
    }
}
