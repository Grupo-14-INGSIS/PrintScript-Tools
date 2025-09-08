import common.src.main.kotlin.ASTNode
import common.src.main.kotlin.DataType
import common.src.main.kotlin.Position
import common.src.main.kotlin.Token
import linter.rules.IfWithoutElseRule
import linter.rules.ImmutableValRule
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

class NewRulesTest {
    @Test
    fun `should report var never reassigned`() {
        val varToken = Token(DataType.DECLARATION, "var", Position(1, 0))
        val idToken = Token(DataType.IDENTIFIER, "counter", Position(1, 4))
        val declarationNode = ASTNode(varToken, listOf(ASTNode(idToken, emptyList())))

        val root = ASTNode(Token(DataType.INVALID, "", Position(0, 0)), listOf(declarationNode))
        val rule = ImmutableValRule()
        val errors = rule.apply(root)

        assertTrue(errors.any { it.message.contains("never reassigned") })
    }

    @Test
    fun `should report if without else and no control flow`() {
        val ifToken = Token(DataType.IF_KEYWORD, "if", Position(1, 0))
        val openBrace = Token(DataType.OPEN_BRACE, "{", Position(1, 3))
        val printlnToken = Token(DataType.IDENTIFIER, "doSomething", Position(1, 4)) // no control flow
        val closeBrace = Token(DataType.CLOSE_BRACE, "}", Position(1, 15))

        val thenBlock = ASTNode(openBrace, listOf(ASTNode(printlnToken, emptyList()), ASTNode(closeBrace, emptyList())))
        val ifNode = ASTNode(ifToken, listOf(thenBlock))

        val root = ASTNode(Token(DataType.INVALID, "", Position(0, 0)), listOf(ifNode))
        val rule = IfWithoutElseRule()
        val errors = rule.apply(root)

        assertTrue(errors.any { it.message.contains("lacks 'else'") })
    }
}
