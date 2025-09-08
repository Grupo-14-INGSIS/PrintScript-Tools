package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class CharLimitPerLine(private val maxLength: Int = 140) : FormatRule {

    override fun format(source: Container): Container {
        val output = Container()
        val currentLineTokens = mutableListOf<Token>()
        var currentLineLength = 0
        var lineNumber = 1

        for (token in source.container) {
            val segments = token.content.split("\n")

            for ((i, segment) in segments.withIndex()) {
                val segmentLength = segment.length
                currentLineLength += segmentLength
                currentLineTokens.add(token.copy(content = segment))

                if (i < segments.lastIndex) {
                    // Fin de línea detectado
                    if (currentLineLength > maxLength) {
                        output.addContainer(
                            Token(
                                type = null,
                                content = "// Line $lineNumber exceeds $maxLength chars\n",
                                position = token.position
                            )
                        )
                    }
                    output.addAll(currentLineTokens)
                    output.addContainer(Token(type = null, content = "\n", position = token.position))

                    currentLineTokens.clear()
                    currentLineLength = 0
                    lineNumber++
                }
            }
        }

        // Última línea sin salto
        if (currentLineTokens.isNotEmpty()) {
            if (currentLineLength > maxLength) {
                output.addContainer(
                    Token(
                        type = null,
                        content = "// Line $lineNumber exceeds $maxLength chars\n",
                        position = currentLineTokens.first().position
                    )
                )
            }
            output.addAll(currentLineTokens)
        }

        return output
    }
}
