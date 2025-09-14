package formatter.src.main.kotlin

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.formatrule.FormatRule

data class FormatterSetup(
    private val tokens: Container,
    private val rules: List<FormatRule>
) {
    fun getTokens(): Container = tokens
    fun getRules(): List<FormatRule> = rules.toList()
}
