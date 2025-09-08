package formatter.src.main.kotlin.formatrule

import container.src.main.kotlin.Container

interface FormatRule {
    /** Takes a `Container` argument and modifies it, returning true.
     * If the operation cannot be performed, it returns false */
    fun format(source: Container): Container
}
