package formatter.tests

import org.junit.jupiter.api.Assertions.*
import formatter.src.main.kotlin.ConfigLoader
import formatter.src.main.kotlin.formatrule.FormatRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import formatter.src.main.kotlin.ConfigLoader


class ConfigLoaderTest {

    @Test
    fun `readConfig loads switches and values from yaml`(@TempDir tempDir: Path) {
        val yamlContent = """
            rules:
              switch:
                NoSpaceBeforeColon: true
                NoSpaceAfterColon: false
              setValue:
                LineLength: 80
        """.trimIndent()

        val configFile = tempDir.resolve("config.yml").toFile()
        configFile.writeText(yamlContent)

        val loader = ConfigLoader(configFile.absolutePath)
        val result = loader.readConfig()

        // Solo NoSpaceBeforeColon debería estar
        assertTrue(result.containsKey("NoSpaceBeforeColon"))
        assertFalse(result.containsKey("NoSpaceAfterColon"))
        // setValue debe incluirse siempre
        assertEquals(80, result["LineLength"])
    }

    @Test
    fun `createConfigurableRules creates rules for known keys`() {
        val loader = ConfigLoader("dummy.yml")
        val rules = loader.createConfigurableRules(
            mapOf(
                "NoSpaceBeforeColon" to true,
                "NoSpaceAfterEquals" to true,
                "UnknownRule" to true
            )
        )

        val names = rules.map { it::class.simpleName }.toSet()
        assertTrue(names.contains("NoSpaceBeforeColonRule"))
        assertTrue(names.contains("NoSpaceAfterEqualsRule"))
        // UnknownRule no debería haberse agregado
        assertEquals(2, rules.size)
    }
}
