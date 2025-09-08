package test.container

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import container.src.main.kotlin.Container

class SpaceAroundOperatorRuleTest {

    private fun token(type: DataType, content: String, line: Int, column: Int): Token {
        return Token(type, content, Position(line, column))
    }

    @Test
    fun `container copy preserves tokens`() {
        val original = Container()
        original.addContainer(token(DataType.IDENTIFIER, "a", 1, 0))
        val copy = original.copy()
        assertEquals(1, copy.size())
        assertEquals("a", copy.get(0)?.content)
        assertEquals(1, copy.get(0)?.position?.line)
        assertEquals(0, copy.get(0)?.position?.column)
    }

    @Test
    fun `sliceOne returns correct token`() {
        val container = Container()
        container.addContainer(token(DataType.IDENTIFIER, "x", 2, 5))
        val slice = container.sliceOne(0)
        assertEquals(1, slice.size())
        assertEquals("x", slice.get(0)?.content)
        assertEquals(2, slice.get(0)?.position?.line)
        assertEquals(5, slice.get(0)?.position?.column)
    }

    @Test
    fun `addAt inserts token at index`() {
        val container = Container()
        container.addContainer(token(DataType.IDENTIFIER, "a", 3, 1))
        container.addAt(token(DataType.ADDITION, "+", 3, 2), 1)
        assertEquals("+", container.get(1)?.content)
        assertEquals(3, container.get(1)?.position?.line)
        assertEquals(2, container.get(1)?.position?.column)
    }
}
