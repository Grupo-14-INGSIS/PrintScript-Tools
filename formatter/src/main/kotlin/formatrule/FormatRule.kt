package formatter.src.main.kotlin.formatrule

import container.src.main.kotlin.Container

interface FormatRule {
    fun format(statements: List<Container>): List<Container>
}
