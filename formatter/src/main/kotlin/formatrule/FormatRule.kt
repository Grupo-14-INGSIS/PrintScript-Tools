package formatter.src.main.kotlin.formatrule

import common.src.main.kotlin.Container
import common.src.main.kotlin.Token

interface FormatRule {
    /** Takes a `Container` argument and modifies it, returning true.
      * If the operation cannot be performed, it returns false */
    fun format(tokens: Container): Boolean
}
