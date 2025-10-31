package linter.src.test.kotlin
import linter.src.main.kotlin.config.ConfigFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class ConfigFactoryTest {

    private lateinit var factory: ConfigFactory

    @BeforeEach
    fun setup() {
        factory = ConfigFactory()
    }

    @Test
    fun `createConfig should handle complete configuration`() {
        val yamlMap = mapOf(
            "rules" to mapOf(
                "identifier_format" to mapOf("style" to "snake_case"),
                "mandatory_variable_or_literal_in_println" to mapOf("enabled" to false)
            )
        )

        val config = factory.createConfig(yamlMap)

        assertNotNull(config.rules.identifier_format)
        assertEquals("snake_case", config.rules.identifier_format?.style)
        assertNotNull(config.rules.mandatory_variable_or_literal_in_println)
        assertEquals(false, config.rules.mandatory_variable_or_literal_in_println?.enabled)
    }

    @Test
    fun `createConfig should use defaults when values missing`() {
        val yamlMap = mapOf(
            "rules" to mapOf(
                "identifier_format" to emptyMap<String, Any>(),
                "mandatory_variable_or_literal_in_println" to emptyMap<String, Any>()
            )
        )

        val config = factory.createConfig(yamlMap)

        assertNotNull(config.rules.identifier_format)
        assertEquals("camelCase", config.rules.identifier_format?.style)
        assertNotNull(config.rules.mandatory_variable_or_literal_in_println)
        assertEquals(true, config.rules.mandatory_variable_or_literal_in_println?.enabled)
    }

    @Test
    fun `createConfig should handle missing rules section`() {
        val yamlMap = emptyMap<String, Any>()

        val config = factory.createConfig(yamlMap)

        assertNull(config.rules.identifier_format)
        assertNull(config.rules.mandatory_variable_or_literal_in_println)
    }

    @Test
    fun `createConfig should handle missing individual rules`() {
        val yamlMap = mapOf("rules" to emptyMap<String, Any>())

        val config = factory.createConfig(yamlMap)

        assertNull(config.rules.identifier_format)
        assertNull(config.rules.mandatory_variable_or_literal_in_println)
    }

    @Test
    fun `createConfig should handle partial configuration`() {
        val yamlMap = mapOf(
            "rules" to mapOf(
                "identifier_format" to mapOf("style" to "camelCase")
            )
        )

        val config = factory.createConfig(yamlMap)

        assertNotNull(config.rules.identifier_format)
        assertEquals("camelCase", config.rules.identifier_format?.style)
        assertNull(config.rules.mandatory_variable_or_literal_in_println)
    }
}
