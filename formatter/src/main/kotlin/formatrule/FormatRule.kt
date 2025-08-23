package formatter.src.main.kotlin.formatrule

import common.src.main.kotlin.Container
import common.src.main.kotlin.Token

interface FormatRule {
    fun format(tokens: Container): Container
}
