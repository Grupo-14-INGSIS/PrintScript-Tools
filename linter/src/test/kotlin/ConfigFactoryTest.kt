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
                "printlnArg" to mapOf("enabled" to false)
            )
        )

        val config = factory.createConfig(yamlMap)

        assertNotNull(config.rules.identifier_format)
        assertEquals("snake_case", config.rules.identifier_format?.style)
        assertNotNull(config.rules.printlnArg)
        assertEquals(false, config.rules.printlnArg?.enabled)
    }

    @Test
    fun `createConfig should use defaults when values missing`() {
        val yamlMap = mapOf(
            "rules" to mapOf(
                "identifier_format" to emptyMap<String, Any>(),
                "printlnArg" to emptyMap<String, Any>()
            )
        )

        val config = factory.createConfig(yamlMap)

        assertNotNull(config.rules.identifier_format)
        assertEquals("camelCase", config.rules.identifier_format?.style)
        assertNotNull(config.rules.printlnArg)
        assertEquals(true, config.rules.printlnArg?.enabled)
    }

    @Test
    fun `createConfig should handle missing rules section`() {
        val yamlMap = emptyMap<String, Any>()

        val config = factory.createConfig(yamlMap)

        assertNull(config.rules.identifier_format)
        assertNull(config.rules.printlnArg)
    }

    @Test
    fun `createConfig should handle missing individual rules`() {
        val yamlMap = mapOf("rules" to emptyMap<String, Any>())

        val config = factory.createConfig(yamlMap)

        assertNull(config.rules.identifier_format)
        assertNull(config.rules.printlnArg)
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
        assertNull(config.rules.printlnArg)
    }
}
