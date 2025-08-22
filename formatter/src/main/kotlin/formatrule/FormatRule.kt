package formatter.src.main.kotlin.formatrule

import common.src.main.kotlin.Token

interface FormatRule {
    fun format(code: List<Token>): List<Token>
}