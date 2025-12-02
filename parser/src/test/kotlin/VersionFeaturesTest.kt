package parser.src.test.kotlin

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import parser.src.main.kotlin.VersionFeatures
import parser.src.main.kotlin.Association
import parser.src.main.kotlin.VersionConfig

class VersionFeaturesTest {

    @Test
    fun `features store keywords, types and functions correctly`() {
        val features = VersionFeatures(
            keywords = setOf("let", "const", "if"),
            types = setOf("String", "Number"),
            functions = setOf("println", "readInput"),
            operators = emptyMap(),
            associations = emptyMap()
        )

        assertTrue(features.keywords.contains("let"))
        assertTrue(features.types.contains("Number"))
        assertTrue(features.functions.contains("println"))
    }

    @Test
    fun `features store operators and associations correctly`() {
        val features = VersionFeatures(
            keywords = emptySet(),
            types = emptySet(),
            functions = emptySet(),
            operators = mapOf("+" to 10, "*" to 20),
            associations = mapOf("+" to Association.LEFT, "*" to Association.RIGHT)
        )

        assertEquals(10, features.operators["+"])
        assertEquals(Association.RIGHT, features.associations["*"])
    }

    @Test
    fun `feature flags default to false`() {
        val features = VersionFeatures(
            keywords = emptySet(),
            types = emptySet(),
            functions = emptySet(),
            operators = emptyMap(),
            associations = emptyMap()
        )

        assertFalse(features.supportsConst)
        assertFalse(features.supportsIfElse)
        assertFalse(features.supportsBlocks)
        assertFalse(features.supportsBooleans)
    }

    @Test
    fun `feature flags can be enabled`() {
        val features = VersionFeatures(
            keywords = emptySet(),
            types = emptySet(),
            functions = emptySet(),
            operators = emptyMap(),
            associations = emptyMap(),
            supportsConst = true,
            supportsIfElse = true,
            supportsBlocks = true,
            supportsBooleans = true
        )

        assertTrue(features.supportsConst)
        assertTrue(features.supportsIfElse)
        assertTrue(features.supportsBlocks)
        assertTrue(features.supportsBooleans)
    }

    @Test
    fun `equality is structural`() {
        val f1 = VersionFeatures(
            keywords = setOf("let"),
            types = setOf("String"),
            functions = setOf("println"),
            operators = mapOf("+" to 10),
            associations = mapOf("+" to Association.LEFT),
            supportsConst = true
        )

        val f2 = VersionFeatures(
            keywords = setOf("let"),
            types = setOf("String"),
            functions = setOf("println"),
            operators = mapOf("+" to 10),
            associations = mapOf("+" to Association.LEFT),
            supportsConst = true
        )

        assertEquals(f1, f2)
        assertEquals(f1.hashCode(), f2.hashCode())
    }

    @Test
    fun `inequality when flags differ`() {
        val f1 = VersionFeatures(
            keywords = setOf("let"),
            types = setOf("String"),
            functions = setOf("println"),
            operators = mapOf("+" to 10),
            associations = mapOf("+" to Association.LEFT),
            supportsConst = true
        )

        val f2 = f1.copy(supportsConst = false)

        assertNotEquals(f1, f2)
    }

    @Test
    fun `test V1_0 features`() {
        val features = VersionConfig.getFeatures("1.0")
        assertTrue(features.keywords.contains("let"))
        assertTrue(features.types.contains("string"))
        assertTrue(features.functions.contains("println"))
        assertFalse(features.supportsConst)
        assertFalse(features.supportsIfElse)
        assertFalse(features.supportsBlocks)
        assertFalse(features.supportsBooleans)
    }

    @Test
    fun `test V1_1 features`() {
        val features = VersionConfig.getFeatures("1.1")
        assertTrue(features.keywords.contains("const"))
        assertTrue(features.types.contains("boolean"))
        assertTrue(features.functions.contains("readInput"))
        assertTrue(features.supportsConst)
        assertTrue(features.supportsIfElse)
        assertTrue(features.supportsBlocks)
        assertTrue(features.supportsBooleans)
    }
}
