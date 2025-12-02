// src/test/kotlin/parser/src/test/kotlin/PrattTokenTest.kt
package parser.src.test.kotlin

import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.*
import parser.src.main.kotlin.PrattToken
import parser.src.main.kotlin.Association
import token.src.main.kotlin.Token

class NewPrattTokenTest {

    private fun dummyToken(): Token = mockk(relaxed = true)

    @Test
    fun `token getter returns the same instance passed in constructor`() {
        val tok = dummyToken()
        val pt = PrattToken(token = tok, precedence = 7, associativity = Association.LEFT)
        assertSame(tok, pt.token())
    }

    @Test
    fun `precedence getter returns constructor value`() {
        val pt = PrattToken(token = dummyToken(), precedence = 42, associativity = Association.RIGHT)
        assertEquals(42, pt.precedence())
    }

    @Test
    fun `associativity getter returns constructor value`() {
        val pt = PrattToken(token = dummyToken(), precedence = 10, associativity = Association.LEFT)
        assertEquals(Association.LEFT, pt.associativity())
    }

    @Test
    fun `children index returns element when in bounds`() {
        val c1 = PrattToken(token = dummyToken(), precedence = 1, associativity = Association.LEFT)
        val c2 = PrattToken(token = dummyToken(), precedence = 2, associativity = Association.RIGHT)
        val pt = PrattToken(token = dummyToken(), precedence = 0, associativity = Association.ANY, children = listOf(c1, c2))

        assertSame(c1, pt.children(0))
        assertSame(c2, pt.children(1))
    }

    @Test
    fun `children index returns null when out of bounds`() {
        val pt = PrattToken(token = dummyToken(), precedence = 0, associativity = Association.ANY, children = emptyList())
        assertNull(pt.children(0))
        assertNull(pt.children(-1))
    }


    @Test
    fun `associate creates a new PrattToken with reset precedence and ANY associativity`() {
        val original = PrattToken(token = dummyToken(), precedence = 99, associativity = Association.RIGHT)
        val newChildren = listOf(
            PrattToken(token = dummyToken(), precedence = 1, associativity = Association.LEFT),
            PrattToken(token = dummyToken(), precedence = 2, associativity = Association.RIGHT)
        )

        val associated = original.associate(newChildren)

        assertNotSame(original, associated)
        assertSame(original.token(), associated.token())
        assertEquals(0, associated.precedence())
        assertEquals(Association.ANY, associated.associativity())
        assertEquals(newChildren, associated.allChildren())

        // Ensure original remains unchanged
        assertEquals(99, original.precedence())
        assertEquals(Association.RIGHT, original.associativity())
        assertTrue(original.allChildren().isEmpty())
    }

    @Test
    fun `associate defensively copies the children list`() {
        val original = PrattToken(token = dummyToken(), precedence = 5, associativity = Association.LEFT)
        val newChildren = mutableListOf(
            PrattToken(token = dummyToken(), precedence = 3, associativity = Association.RIGHT)
        )

        val associated = original.associate(newChildren)
        assertEquals(1, associated.allChildren().size)

        // Mutate the source list; associated should not change
        newChildren.add(PrattToken(token = dummyToken(), precedence = 4, associativity = Association.LEFT))
        assertEquals(1, associated.allChildren().size)
    }

    @Test
    fun `constructor does NOT defensively copy children list (documents current behavior)`() {
        val mutableChildren = mutableListOf(
            PrattToken(token = dummyToken(), precedence = 1, associativity = Association.LEFT)
        )
        val pt = PrattToken(
            token = dummyToken(),
            precedence = 0,
            associativity = Association.ANY,
            children = mutableChildren
        )

        // Mutating the original list affects the instance (current behavior)
        mutableChildren.add(PrattToken(token = dummyToken(), precedence = 2, associativity = Association.RIGHT))
        assertEquals(2, pt.allChildren().size)
    }

    @Test
    fun `children index negative returns null`() {
        val c = PrattToken(token = dummyToken(), precedence = 1, associativity = Association.LEFT)
        val pt = PrattToken(token = dummyToken(), precedence = 0, associativity = Association.ANY, children = listOf(c))
        assertNull(pt.children(-1))
    }

    @Test
    fun `empty children behaves consistently`() {
        val pt = PrattToken(token = dummyToken(), precedence = 7, associativity = Association.RIGHT, children = emptyList())
        assertTrue(pt.allChildren().isEmpty())
        assertNull(pt.children(0))
        assertNull(pt.children(Int.MAX_VALUE))
    }

    @Test
    fun `deep children structure preserves identity`() {
        val leaf1 = PrattToken(token = dummyToken(), precedence = 1, associativity = Association.LEFT)
        val leaf2 = PrattToken(token = dummyToken(), precedence = 2, associativity = Association.RIGHT)
        val mid = PrattToken(token = dummyToken(), precedence = 3, associativity = Association.LEFT, children = listOf(leaf1, leaf2))
        val root = PrattToken(token = dummyToken(), precedence = 4, associativity = Association.RIGHT, children = listOf(mid))

        assertSame(mid, root.children(0))
        assertSame(leaf1, root.children(0)?.children(0))
        assertSame(leaf2, root.children(0)?.children(1))
    }

    @Test
    fun `associate with empty children replaces children with empty list`() {
        val original = PrattToken(
            token = dummyToken(),
            precedence = 8,
            associativity = Association.LEFT,
            children = listOf(PrattToken(token = dummyToken(), precedence = 1, associativity = Association.RIGHT))
        )

        val associated = original.associate(emptyList())
        assertTrue(associated.allChildren().isEmpty())
        assertEquals(0, associated.precedence())
        assertEquals(Association.ANY, associated.associativity())
    }
}
