package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class AssignSpacingRule(
    private val spaceBefore: Boolean = true,
    private val spaceAfter: Boolean = true
) : FormatRule {

    private val equals = DataType.ASSIGNATION
    private val space = DataType.SPACE

    override fun format(source: Container): Container {
        var tokens = source
        var i = 0

        while (i < tokens.size()) {
            val token = tokens.get(i)
            if (token == null) break

            if (token.type == equals) {
                // Handle space AFTER equals first
                if (spaceAfter) {
                    if (i + 1 < tokens.size()) {
                        val next = tokens.get(i + 1)
                        if (next?.type != space) {
                            tokens = tokens.addAt(
                                Token(space, " ", Position(0, 0)),
                                i + 1
                            )
                        }
                    }
                } else {
                    // Remove all spaces after
                    while (i + 1 < tokens.size()) {
                        val next = tokens.get(i + 1)
                        if (next?.type == space) {
                            val response = tokens.remove(i + 1)
                            tokens = response.container
                            if (response.token == null) break
                        } else {
                            break
                        }
                    }
                }

                // Handle space BEFORE equals
                if (spaceBefore) {
                    if (i > 0) {
                        val previous = tokens.get(i - 1)
                        if (previous?.type != space) {
                            tokens = tokens.addAt(
                                Token(space, " ", Position(0, 0)),
                                i
                            )
                            i++ // Adjust index because we added before
                        }
                    }
                } else {
                    // Remove all spaces before
                    while (i > 0) {
                        val previous = tokens.get(i - 1)
                        if (previous?.type == space) {
                            val response = tokens.remove(i - 1)
                            tokens = response.container
                            if (response.token == null) break
                            i-- // Adjust index
                        } else {
                            break
                        }
                    }
                }
            }
            i++
        }

        return tokens
    }
}
