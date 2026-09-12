package linter.src.main.kotlin

fun interface LintRuleFactory {
    fun create(config: Map<String, Any>): LintRule?
}
