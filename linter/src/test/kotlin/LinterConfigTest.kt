package linter

import linter.config.RulesConfig
import linter.config.IdentifierNamingConfig
import linter.config.LinterConfig
import linter.config.PrintLnConfig
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class LinterConfigTest {

    @Test
    fun `LinterConfig should contain rules configuration`() {
        val identifierConfig = IdentifierNamingConfig("snake_case")
        val printlnConfig = PrintLnConfig(false)
        val rulesConfig = RulesConfig(identifierConfig, printlnConfig)
        val linterConfig = LinterConfig(rulesConfig)

        assertEquals(rulesConfig, linterConfig.rules)
        assertEquals(identifierConfig, linterConfig.rules.identifierNaming)
        assertEquals(printlnConfig, linterConfig.rules.printlnArg)
    }
}
