package linter.src.test.kotlin

import linter.src.main.kotlin.config.IdentifierNamingConfig
import linter.src.main.kotlin.config.LinterConfig
import linter.src.main.kotlin.config.PrintLnConfig
import linter.src.main.kotlin.config.RulesConfig
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
        assertEquals(identifierConfig, linterConfig.rules.identifier_format)
        assertEquals(printlnConfig, linterConfig.rules.mandatory_variable_or_literal_in_println)
    }
}
