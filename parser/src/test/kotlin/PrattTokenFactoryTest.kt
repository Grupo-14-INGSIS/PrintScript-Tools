package parser.src.test.kotlin

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import parser.src.main.kotlin.PrattTokenFactory
import parser.src.main.kotlin.Association
import parser.src.main.kotlin.VersionFeatures
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

class PrattTokenFactoryTest {

    private val features = VersionFeatures(
        keywords = setOf("let", "const", "if", "else"),
        types = setOf("String", "Number", "Boolean"),
        functions = setOf("println", "readInput"),
        operators = mapOf("+" to 10, "*" to 20, "println" to 5),
        associations = mapOf("+" to Association.LEFT, "*" to Association.RIGHT, "println" to Association.ANY),
        supportsConst = true,
        supportsIfElse = true,
        supportsBlocks = true,
        supportsBooleans = true
    )

    private val factory = PrattTokenFactory(features)

    @Test
    fun `operator token resolves precedence and associativity`() {
        val token = Token(DataType.ADDITION, "+", Position(1, 1))
        val pratt = factory.createPrattToken(token)

        assertEquals(10, pratt.precedence())
        assertEquals(Association.LEFT, pratt.associativity())
    }

    @Test
    fun `function token resolves if mapped as operator`() {
        val token = Token(DataType.PRINTLN, "println", Position(2, 1))
        val pratt = factory.createPrattToken(token)

        assertEquals(5, pratt.precedence())
        assertEquals(Association.ANY, pratt.associativity())
    }

    @Test
    fun `keyword token not in operators yields fallback`() {
        val token = Token(DataType.LET_KEYWORD, "let", Position(3, 1))
        val pratt = factory.createPrattToken(token)

        assertEquals(0, pratt.precedence())
        assertEquals(Association.ANY, pratt.associativity())
    }

    @Test
    fun `type token not in operators yields fallback`() {
        val token = Token(DataType.STRING_TYPE, "String", Position(4, 1))
        val pratt = factory.createPrattToken(token)

        assertEquals(0, pratt.precedence())
        assertEquals(Association.ANY, pratt.associativity())
    }

    @Test
    fun `identifier token yields fallback`() {
        val token = Token(DataType.IDENTIFIER, "myVar", Position(5, 1))
        val pratt = factory.createPrattToken(token)

        assertEquals(0, pratt.precedence())
        assertEquals(Association.ANY, pratt.associativity())
    }

    @Test
    fun `unknown content yields fallback`() {
        val token = Token(DataType.INVALID, "???", Position(6, 1))
        val pratt = factory.createPrattToken(token)

        assertEquals(0, pratt.precedence())
        assertEquals(Association.ANY, pratt.associativity())
    }
}
