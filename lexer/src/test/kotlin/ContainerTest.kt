package model.structure

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ContainerTest {

    @Test
    fun `test add token to container`() {
        val container = Container()
        val token = Token(DataType.IDENTIFIER, "test", Position(0, 0))

        container.addContainer(token)

        assertEquals(1, container.container.size)
        assertEquals(token, container.container[0])
    }

    @Test
    fun `test add multiple tokens to container`() {
        val container = Container()
        val token1 = Token(DataType.LET_KEYWORD, "let", Position(0, 0))
        val token2 = Token(DataType.IDENTIFIER, "x", Position(1, 1))

        container.addContainer(token1)
        container.addContainer(token2)

        assertEquals(2, container.container.size)
        assertEquals(token1, container.container[0])
        assertEquals(token2, container.container[1])
    }
}
