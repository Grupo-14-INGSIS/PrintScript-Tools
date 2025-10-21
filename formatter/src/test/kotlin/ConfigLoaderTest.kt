package formatter.src.test.kotlin

import org.junit.jupiter.api.Assertions.*
import formatter.src.main.kotlin.ConfigLoader
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

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
        val result = loader.readConfig() // ✅ Ahora es internal

        // Solo NoSpaceBeforeColon debería estar
        assertTrue(result.containsKey("NoSpaceBeforeColon"))
        assertFalse(result.containsKey("NoSpaceAfterColon"))
        // setValue debe incluirse siempre
        assertEquals(80, result["LineLength"])
    }

    @Test
    fun `createConfigurableRules creates rules for known keys`() {
        val loader = ConfigLoader("dummy.yml") // El archivo no se lee en este test
        val rules = loader.createConfigurableRules( // ✅ Ahora es internal
            mapOf(
                "NoSpaceBeforeColon" to true,
                "NoSpaceAfterEquals" to true,
                "UnknownRule" to true
            )
        )

        val names = rules.map { it::class.simpleName }.toSet()
        assertTrue(names.contains("NoSpaceBeforeColonRule"))
        // UnknownRule no debería haberse agregado
        assertEquals(1, rules.size)
    }

    @Test
    fun `loadConfig integrates all rules correctly`(@TempDir tempDir: Path) {
        val yamlContent = """
            rules:
              switch:
                NoSpaceBeforeColon: true
                NoSpaceAfterColon: true
              setValue:
                lineBreakBeforePrint: 2
                indentSize: 4
        """.trimIndent()

        val configFile = tempDir.resolve("config.yml").toFile()
        configFile.writeText(yamlContent)

        val loader = ConfigLoader(configFile.absolutePath)
        val allRules = loader.loadConfig()

        // Debería incluir reglas obligatorias + configurables
        assertTrue(allRules.size >= 6) // 4 mandatory + al menos 2 configurable

        val ruleNames = allRules.map { it::class.simpleName }.toSet()
        // Verificar reglas obligatorias
        assertTrue(ruleNames.contains("SpaceAroundOperatorRule"))
        assertTrue(ruleNames.contains("SpaceBetweenTokensRule"))
        // Verificar reglas configurables
        assertTrue(ruleNames.contains("NoSpaceBeforeColonRule"))
        assertTrue(ruleNames.contains("NoSpaceAfterColonRule"))
    }
}
