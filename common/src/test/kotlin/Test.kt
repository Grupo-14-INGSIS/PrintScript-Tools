import common.src.main.kotlin.ASTNode
import common.src.main.kotlin.Container
import common.src.main.kotlin.DataType
import common.src.main.kotlin.Position
import common.src.main.kotlin.ReservedKeywords
import common.src.main.kotlin.Token
import common.src.main.kotlin.TokenMap
import common.src.main.kotlin.TokenPattern
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

class Test {

    @Test
    fun `token stores correct data`() {
        val pos = Position(1, 5)
        val token = Token(DataType.IDENTIFIER, "myVar", pos)
        assertEquals(DataType.IDENTIFIER, token.type)
        assertEquals("myVar", token.content)
        assertEquals(pos, token.position)
    }

    @Test
    fun `position stores line and column`() {
        val pos = Position(3, 12)
        assertEquals(3, pos.line)
        assertEquals(12, pos.column)
    }

    @Test
    fun `ast node holds token and children`() {
        val token = Token(DataType.IDENTIFIER, "x", Position(1, 1))
        val child = ASTNode(Token(DataType.NUMBER_LITERAL, "5", Position(1, 5)), emptyList())
        val node = ASTNode(token, listOf(child))

        assertEquals(token, node.token)
        assertEquals(1, node.children.size)
        assertEquals("5", node.children[0].token.content)
    }

    @Test
    fun `add and get token`() {
        val container = Container()
        val token = Token(DataType.IDENTIFIER, "x", Position(1, 1))
        container.addContainer(token)
        assertEquals(token, container.get(0))
    }

    @Test
    fun `sliceOne returns correct token`() {
        val container = Container()
        val token = Token(DataType.IDENTIFIER, "x", Position(1, 1))
        container.addContainer(token)
        val sliced = container.sliceOne(0)
        assertEquals("x", sliced.get(0)?.content)
    }

    @Test
    fun `slice returns correct range`() {
        val container = Container()
        val t1 = Token(DataType.IDENTIFIER, "a", Position(1, 1))
        val t2 = Token(DataType.IDENTIFIER, "b", Position(1, 2))
        val t3 = Token(DataType.IDENTIFIER, "c", Position(1, 3))
        container.addAll(listOf(t1, t2, t3))
        val sliced = container.slice(1, 3)
        assertEquals(2, sliced.size())
        assertEquals("b", sliced.get(0)?.content)
        assertEquals("c", sliced.get(1)?.content)
    }

    @Test
    fun `checkIs returns false for mismatched types`() {
        val container = Container()
        container.addAll(
            listOf(
                Token(DataType.IDENTIFIER, "x", Position(1, 1)),
                Token(DataType.STRING_LITERAL, "\"hello\"", Position(1, 2))
            )
        )
        val result = container.checkIs(listOf(DataType.IDENTIFIER, DataType.NUMBER_LITERAL))
        assertFalse(result)
    }

    @Test
    fun `valid variable name passes`() {
        assertTrue(ReservedKeywords.nameIsAllowed("validName"))
    }

    @Test
    fun `reserved word fails`() {
        assertFalse(ReservedKeywords.nameIsAllowed("let"))
    }

    @Test
    fun `forbidden character fails`() {
        assertFalse(ReservedKeywords.nameIsAllowed("invalid!name"))
    }

    @Test
    fun `forbidden beginning fails`() {
        assertFalse(ReservedKeywords.nameIsAllowed("1var"))
        assertFalse(ReservedKeywords.nameIsAllowed("_hidden"))
    }

    @Test
    fun `empty name fails`() {
        assertFalse(ReservedKeywords.nameIsAllowed(""))
    }

    @Test
    fun `classifyTokenMap returns correct type for 1_0`() {
        val type = TokenMap.classifyTokenMap("let", "1.0")
        assertEquals(DataType.LET_KEYWORD, type)
    }

    @Test
    fun `classifyTokenMap returns correct type for 1_1`() {
        val type = TokenMap.classifyTokenMap("const", "1.1")
        assertEquals(DataType.CONST_KEYWORD, type)
    }

    @Test
    fun `classifyTokenMap falls back to 1_0 for unknown version`() {
        val type = TokenMap.classifyTokenMap("let", "2.0")
        assertEquals(DataType.LET_KEYWORD, type)
    }

    @Test
    fun `classifyTokenMap returns null for unknown token`() {
        val type = TokenMap.classifyTokenMap("unknown", "1.0")
        assertNull(type)
    }

    @Test
    fun `string literal is classified correctly`() {
        val type = TokenPattern.classifyTokenPattern("\"hello\"")
        assertEquals(DataType.STRING_LITERAL, type)
    }

    @Test
    fun `number literal is classified correctly`() {
        val type = TokenPattern.classifyTokenPattern("42")
        assertEquals(DataType.NUMBER_LITERAL, type)
    }

    @Test
    fun `identifier is classified correctly`() {
        val type = TokenPattern.classifyTokenPattern("myVar123")
        assertEquals(DataType.IDENTIFIER, type)
    }

    @Test
    fun `invalid token returns null`() {
        val type = TokenPattern.classifyTokenPattern("$$$")
        assertNull(type)
    }
}
