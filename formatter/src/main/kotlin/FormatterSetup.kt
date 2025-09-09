package formatter.src.main.kotlin

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.formatrule.FormatRule

data class FormatterSetup(
    val tokens: Container,
    val rules: List<FormatRule>
)
