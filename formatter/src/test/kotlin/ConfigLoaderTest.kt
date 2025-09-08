package formatter.src.test.kotlin

import formatter.src.main.kotlin.ConfigLoader
import formatter.src.main.kotlin.formatrule.FormatRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ConfigLoaderTest {
    // C:\Users\santi\faculty\INGSIS\PrintScript-Tools\formatter\src\test\resources\format_rules.yaml
    val configFile = "C:\\Users\\santi\\faculty\\INGSIS\\PrintScript-Tools\\formatter\\src\\test\\resources\\format_rules.yaml"

    val mandatoryRules = 2 // total 3
    val configurableRules = 4 // total 5
    val totalRules = mandatoryRules + configurableRules

    @Test
    fun createMandatoryRules() {
        val loader = ConfigLoader(configFile)
        val rules: List<FormatRule> = loader.createMandatoryRules()

        assertEquals(mandatoryRules, rules.size)
    }

    @Test
    fun testReadConfig() {
        val loader = ConfigLoader(configFile)
        val rules: Map<String, Any> = loader.readConfig()

        assertEquals(5, rules.size)

        assertEquals(true, rules.containsKey("NoSpaceBeforeColon"))
        assertEquals(true, rules["NoSpaceBeforeColon"])

        assertEquals(true, rules.containsKey("NoSpaceAfterColon"))
        assertEquals(true, rules["NoSpaceAfterColon"])

        assertEquals(true, rules.containsKey("NoSpaceBeforeEquals"))
        assertEquals(true, rules["NoSpaceBeforeEquals"])

        assertEquals(true, rules.containsKey("NoSpaceAfterEquals"))
        assertEquals(true, rules["NoSpaceAfterEquals"])

        assertEquals(true, rules.containsKey("lineBreakBeforePrint"))
        assertEquals(2, rules["lineBreakBeforePrint"])
    }

    @Test
    fun createConfigurableRules() {
        val loader = ConfigLoader(configFile)
        val yaml = loader.readConfig()
        val rules: List<FormatRule> = loader.createConfigurableRules(yaml)

        assertEquals(configurableRules, rules.size)
    }

    @Test
    fun testLoadConfig() {
        val loader = ConfigLoader(configFile)
        val rules: List<FormatRule> = loader.loadConfig()
        assertEquals(totalRules, rules.size)
    }

}
