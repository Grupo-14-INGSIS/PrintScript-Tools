package linter.src.test.kotlin

import linter.src.main.kotlin.config.IdentifierNamingConfig
import linter.src.main.kotlin.config.PrintLnConfig
import linter.src.main.kotlin.config.RulesConfig
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class ConfigDataClassesTest {

    @Test
    fun `IdentifierNamingConfig should have default camelCase style`() {
        val config = IdentifierNamingConfig()
        assertEquals("camelCase", config.style)
    }

    @Test
    fun `IdentifierNamingConfig should accept custom style`() {
        val config = IdentifierNamingConfig("snake_case")
        assertEquals("snake_case", config.style)
    }

    @Test
    fun `PrintLnConfig should have default enabled true`() {
        val config = PrintLnConfig()
        assertTrue(config.enabled)
    }

    @Test
    fun `PrintLnConfig should accept custom enabled value`() {
        val config = PrintLnConfig(false)
        assertFalse(config.enabled)
    }

    @Test
    fun `RulesConfig should have null defaults`() {
        val config = RulesConfig()
        assertNull(config.identifier_format)
        assertNull(config.mandatory_variable_or_literal_in_println)
    }

    @Test
    fun `RulesConfig should accept custom values`() {
        val identifierConfig = IdentifierNamingConfig("snake_case")
        val printlnConfig = PrintLnConfig(false)
        val config = RulesConfig(identifierConfig, printlnConfig)

        assertEquals(identifierConfig, config.identifier_format)
        assertEquals(printlnConfig, config.mandatory_variable_or_literal_in_println)
    }
}
