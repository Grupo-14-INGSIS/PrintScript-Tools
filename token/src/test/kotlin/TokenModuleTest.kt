package token.src.test.kotlin

import ErrorReporter
import container.src.main.kotlin.Container
import container.src.main.kotlin.RemoveResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class TokenModuleTest {

    @Test
    fun `test Position data class in token module`() {
        val p1 = Position(1, 10)
        val p2 = Position(1, 10)
        val p3 = Position(2, 20)

        assertEquals(1, p1.line)
        assertEquals(10, p1.column)
        assertEquals(p1, p2)
        assertEquals(p1.hashCode(), p2.hashCode())
        assertTrue(p1 != p3)
        assertEquals(1, p1.component1())
        assertEquals(10, p1.component2())

        val pCopy = p1.copy(line = 5)
        assertEquals(5, pCopy.line)
        assertEquals(10, pCopy.column)
    }

    @Test
    fun `test Token data class`() {
        val pos = Position(1, 1)
        val token1 = Token(DataType.LET_KEYWORD, "let", pos)
        val token2 = Token(DataType.LET_KEYWORD, "let", pos)
        val token3 = Token(DataType.IDENTIFIER, "x", pos)

        assertEquals(DataType.LET_KEYWORD, token1.type)
        assertEquals("let", token1.content)
        assertEquals(pos, token1.position)
        assertEquals(token1, token2)
        assertEquals(token1.hashCode(), token2.hashCode())
        assertTrue(token1 != token3)

        val tokenCopy = token1.copy(content = "const")
        assertEquals("const", tokenCopy.content)
        assertEquals(DataType.LET_KEYWORD, tokenCopy.type)
    }

    @Test
    fun `test DataType enum values`() {
        for (type in DataType.values()) {
            assertNotNull(type.name)
            assertEquals(type, DataType.valueOf(type.name))
        }
    }

    @Test
    fun `test Container operations`() {
        var container = Container()
        assertTrue(container.isEmpty())
        assertEquals(0, container.size())
        assertNull(container.first())
        assertNull(container.last())
        assertNull(container.get(0))
        assertNull(container.get(-1))

        val t1 = Token(DataType.LET_KEYWORD, "let", Position(1, 1))
        val t2 = Token(DataType.IDENTIFIER, "x", Position(1, 5))
        val t3 = Token(DataType.SEMICOLON, ";", Position(1, 7))

        container = container.addContainer(t1)
        assertFalse(container.isEmpty())
        assertEquals(1, container.size())
        assertEquals(t1, container.first())
        assertEquals(t1, container.last())
        assertEquals(t1, container.get(0))

        container = container.addAll(listOf(t2, t3))
        assertEquals(3, container.size())
        assertEquals(t1, container.first())
        assertEquals(t3, container.last())
        assertEquals(t2, container.get(1))
        assertEquals(t3, container.get(2))
        assertNull(container.get(5))

        // addAt
        val tInserted = Token(DataType.COLON, ":", Position(1, 6))
        val withInserted = container.addAt(tInserted, 2)
        assertEquals(4, withInserted.size())
        assertEquals(tInserted, withInserted.get(2))

        // addAt boundaries
        val atNeg = container.addAt(tInserted, -5)
        assertEquals(tInserted, atNeg.first())

        val atBeyond = container.addAt(tInserted, 100)
        assertEquals(tInserted, atBeyond.last())

        // remove
        val removeValid = container.remove(1)
        assertEquals(t2, removeValid.token)
        assertEquals(2, removeValid.container.size())
        assertEquals(t1, removeValid.container.get(0))
        assertEquals(t3, removeValid.container.get(1))

        val removeInvalid = container.remove(10)
        assertNull(removeInvalid.token)
        assertEquals(container.size(), removeInvalid.container.size())

        val removeNeg = container.remove(-1)
        assertNull(removeNeg.token)

        // take
        val taken = container.take(1)
        assertEquals(1, taken.size())
        assertEquals(t2, taken.get(0))

        val takeInvalid = container.take(-1)
        assertTrue(takeInvalid.isEmpty())
        val takeBeyond = container.take(10)
        assertTrue(takeBeyond.isEmpty())

        // slice
        val sliced = container.slice(1, 3)
        assertEquals(2, sliced.size())
        assertEquals(t2, sliced.get(0))
        assertEquals(t3, sliced.get(1))

        val sliceFromGreater = container.slice(2, 1)
        assertTrue(sliceFromGreater.isEmpty())

        val sliceNeg = container.slice(-1, 2)
        assertEquals(2, sliceNeg.size())

        val sliceBeyond = container.slice(1, 100)
        assertEquals(2, sliceBeyond.size())
    }

    @Test
    fun `test RemoveResponse data class`() {
        val t = Token(DataType.IDENTIFIER, "a", Position(1, 1))
        val c = Container(listOf(t))
        val resp = RemoveResponse(t, c)

        assertEquals(t, resp.token)
        assertEquals(c, resp.container)
        assertEquals(resp, resp.copy())
        assertTrue(resp.toString().contains("RemoveResponse"))
    }

    @Test
    fun `test ErrorReporter`() {
        val originalOut = System.out
        val outStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outStream))

        try {
            val reporter = ErrorReporter()
            val exc = RuntimeException("Syntax problem")

            // Report without container
            reporter.reportError("lexing", exc, null)
            var output = outStream.toString()
            assertTrue(output.contains("ERROR during lexing: Syntax problem"))

            // Report with empty container
            outStream.reset()
            reporter.reportError("parsing", exc, Container())
            output = outStream.toString()
            assertTrue(output.contains("ERROR during parsing: Syntax problem"))

            // Report with tokens in container
            outStream.reset()
            val t = Token(DataType.IDENTIFIER, "foo", Position(3, 12))
            reporter.reportError("execution", exc, Container(listOf(t)))
            output = outStream.toString()
            assertTrue(output.contains("Location: Line 3, Column 12"))
            assertTrue(output.contains("Near token: 'foo' (IDENTIFIER)"))

            // Static method
            outStream.reset()
            ErrorReporter.report("static_test", exc, Container(listOf(t)))
            output = outStream.toString()
            assertTrue(output.contains("ERROR during static_test"))
        } finally {
            System.setOut(originalOut)
        }
    }
}
