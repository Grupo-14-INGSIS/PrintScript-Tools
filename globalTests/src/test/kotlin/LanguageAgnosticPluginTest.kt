package globaltests.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container
import interpreter.src.main.kotlin.ActionType
import interpreter.src.main.kotlin.Actions
import interpreter.src.main.kotlin.ExecutionContext
import interpreter.src.main.kotlin.Interpreter
import lexer.src.main.kotlin.ExactMatchTokenPlugin
import lexer.src.main.kotlin.Lexer
import lexer.src.main.kotlin.RegexTokenPlugin
import lexer.src.main.kotlin.TokenPlugin
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.src.main.kotlin.ExpressionParser
import parser.src.main.kotlin.Parser
import parser.src.main.kotlin.StatementParser
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

/**
 * Test que demuestra como extender y probar el compilador/interprete con un lenguaje
 * personalizado utilizando unicamente el sistema de plugins (sin modificar el codigo core).
 */
class LanguageAgnosticPluginTest {

    @Test
    fun testCustomMiniLanguageWithPlugins() {
        // 1. Definimos plugins lexicos personalizados para un lenguaje matematico simple:
        // Sintaxis: echo 5 + 10;
        val customPlugins: List<TokenPlugin> = listOf(
            ExactMatchTokenPlugin(
                mapOf(
                    "echo" to DataType.PRINTLN,
                    "+" to DataType.ADDITION,
                    ";" to DataType.SEMICOLON,
                    " " to DataType.SPACE
                )
            ),
            RegexTokenPlugin(Regex("^[0-9]+$"), DataType.NUMBER_LITERAL)
        )

        val code = "echo 5 + 10;"

        // 2. Lexer agnostico usando los plugins inyectados
        val lexer = Lexer.from(code, customPlugins)
        val statements = lexer.lexIntoStatements().toList()
        assertEquals(1, statements.size)

        // 3. Parser personalizado que entiende la sentencia 'echo <expr>'
        val customEchoStatementParser = object : StatementParser {
            override fun canParse(tokens: Container): Boolean {
                return tokens.size() >= 2 && tokens.first()?.type == DataType.PRINTLN
            }

            override fun parse(tokens: Container, parser: ExpressionParser): ASTNode {
                val exprTokens = tokens.slice(1, tokens.size())
                val exprAst = parser.expParse(exprTokens)
                return ASTNode(
                    DataType.PRINTLN,
                    "echo",
                    tokens.first()?.position ?: Position(1, 1),
                    listOf(exprAst)
                )
            }
        }

        val parser = Parser(statements.first(), "1.0", listOf(customEchoStatementParser))
        val ast = parser.parse()
        assertEquals(DataType.PRINTLN, ast.type)

        // 4. Interprete con un handler de salida personalizado
        val outputs = mutableListOf<String>()
        val interpreter = Interpreter("1.0", printer = { outputs.add(it.toString()) })

        // Registrar handler para la accion PRINT
        val customEchoAction = object : ActionType {
            override fun interpret(node: ASTNode, interpreter: ExecutionContext): Any {
                val value = node.children.firstOrNull()?.let { interpreter.interpret(it) }
                interpreter.printer("CUSTOM ECHO: $value")
                return value ?: ""
            }
        }
        interpreter.registerHandler(Actions.PRINT, customEchoAction)

        interpreter.interpret(ast)

        assertEquals(listOf("CUSTOM ECHO: 15"), outputs)
    }
}
