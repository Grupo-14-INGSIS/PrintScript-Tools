package formatter.src.main.kotlin

import formatter.src.main.kotlin.formatrule.FormatRule

fun interface FormatRuleFactory {
    fun create(value: Any): FormatRule?
}
