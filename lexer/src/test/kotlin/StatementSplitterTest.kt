package lexer.src.test.kotlin

import container.src.main.kotlin.Container
import lexer.src.main.kotlin.DefaultStatementSplitter
import lexer.src.main.kotlin.Lexer
import lexer.src.main.kotlin.StatementSplitter
import lexer.src.main.kotlin.StringCharSource
import lexer.src.main.kotlin.TokenPlugin
import lexer.src.main.kotlin.TokenPluginFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class StatementSplitterTest {

    @Test
    fun `DefaultStatementSplitter splits basic semicolon statements in v1_0`() {
        val splitter = DefaultStatementSplitter(supportsBlocks = false)
        val plugins = TokenPluginFactory.createPlugins("1.0")
        val pieces = sequenceOf("let", " ", "x", " ", ":", " ", "number", " ", "=", " ", "5", ";")

        val statements = splitter.splitIntoStatements(pieces, plugins).toList()
        assertEquals(1, statements.size)
        assertTrue(statements[0].size() > 0)
    }

    @Test
    fun `DefaultStatementSplitter handles if else block lookahead in v1_1`() {
        val splitter = DefaultStatementSplitter(supportsBlocks = true)
        val plugins = TokenPluginFactory.createPlugins("1.1")
        val pieces = sequenceOf(
            "if", " ", "(", "true", ")", " ", "{", "println", "(", "\"a\"", ")", ";", "}",
            " ", "else", " ", "{", "println", "(", "\"b\"", ")", ";", "}"
        )

        val statements = splitter.splitIntoStatements(pieces, plugins).toList()
        assertEquals(1, statements.size)
    }

    @Test
    fun `Custom StatementSplitter can be injected into Lexer`() {
        val customSplitter = object : StatementSplitter {
            override fun splitIntoStatements(
                pieces: Sequence<String>,
                tokenPlugins: List<TokenPlugin>
            ): Sequence<Container> = sequence {
                yield(Container())
            }
        }

        val lexer = Lexer(
            source = StringCharSource("let x: number = 1;"),
            tokenPlugins = TokenPluginFactory.createPlugins("1.0"),
            version = "1.0",
            statementSplitter = customSplitter
        )

        val statements = lexer.lexIntoStatements().toList()
        assertEquals(1, statements.size)
        assertEquals(0, statements[0].size())
    }

    @Test
    fun `tokens across multiple lines maintain 1-indexed column position`() {
        val lexer = Lexer.from("let x: number = 5;\nprintln();", "1.0")
        val statements = lexer.lexIntoStatements().toList()
        assertEquals(2, statements.size)

        val secondStatement = statements[1]
        var targetToken = secondStatement.get(0)
        for (i in 0 until secondStatement.size()) {
            val current = secondStatement.get(i)
            if (current != null && current.content == "println") {
                targetToken = current
                break
            }
        }
        assertEquals("println", targetToken?.content)
        assertEquals(2, targetToken?.position?.line)
        assertEquals(1, targetToken?.position?.column)
    }
}
