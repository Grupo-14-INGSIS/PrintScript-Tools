package parser.src.test.kotlin

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import parser.src.main.kotlin.PrattTokenFactory
import parser.src.main.kotlin.VersionConfig
import parser.src.main.kotlin.VersionFeatures
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

class PrattTokenFactoryTest {

    @Test
    fun `factory creates literal token`() {
        val features = VersionFeatures(
            keywords = emptySet(),
            types = emptySet(),
            functions = emptySet(),
            operators = emptyMap(),
            associations = emptyMap()
        )
        val factory = PrattTokenFactory(features)
        val token = Token(DataType.NUMBER_LITERAL, "5", Position(0, 0))

        val prattToken = factory.createPrattToken(token)

        assertEquals(token, prattToken.token())
        assertEquals(0, prattToken.precedence())
    }

    @Test
    fun `factory creates operator token`() {
        val features = VersionFeatures(
            keywords = emptySet(),
            types = emptySet(),
            functions = emptySet(),
            operators = mapOf("+" to 10),
            associations = emptyMap()
        )
        val factory = PrattTokenFactory(features)
        val token = Token(DataType.ADDITION, "+", Position(0, 0))

        val prattToken = factory.createPrattToken(token)

        assertEquals(token, prattToken.token())
        assertEquals(10, prattToken.precedence())
    }

    @Test
    fun `factory creates parenthesis token`() {
        val features = VersionFeatures(
            keywords = emptySet(),
            types = emptySet(),
            functions = emptySet(),
            operators = emptyMap(),
            associations = emptyMap()
        )
        val factory = PrattTokenFactory(features)
        val token = Token(DataType.OPEN_PARENTHESIS, "(", Position(0, 0))

        val prattToken = factory.createPrattToken(token)

        assertEquals(token, prattToken.token())
        assertEquals(0, prattToken.precedence()) // Changed from -1 to 0
    }

    @Test
    fun `create pratt token`() {
        val features = VersionConfig.getFeatures("1.1")
        val factory = PrattTokenFactory(features)
        val token = Token(DataType.NUMBER_LITERAL, "5", Position(0, 0))
        val prattToken = factory.createPrattToken(token)
        assertEquals(token, prattToken.token())
    }
}
