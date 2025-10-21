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
                // Handle space before equals
                tokens = handleSpaceBefore(tokens, i)

                // Recalculate position after potential modifications
                val newEqualsIndex = findEqualsAt(tokens, i)
                if (newEqualsIndex == -1) break

                // Handle space after equals
                tokens = handleSpaceAfter(tokens, newEqualsIndex)

                i = newEqualsIndex + 1
            } else {
                i++
            }
        }

        return tokens
    }

    private fun handleSpaceBefore(tokens: Container, equalsIndex: Int): Container {
        var result = tokens
        var idx = equalsIndex

        if (spaceBefore) {
            // Add space before if not present
            if (idx > 0) {
                val previous = result.get(idx - 1)
                if (previous?.type != space) {
                    result = result.addAt(
                        Token(space, " ", Position(0, 0)),
                        idx
                    )
                }
            }
        } else {
            // Remove all spaces before
            while (idx > 0) {
                val previous = result.get(idx - 1)
                if (previous?.type == space) {
                    val response = result.remove(idx - 1)
                    result = response.container
                    if (response.token == null) break
                    idx--
                } else {
                    break
                }
            }
        }

        return result
    }

    private fun handleSpaceAfter(tokens: Container, equalsIndex: Int): Container {
        var result = tokens

        if (spaceAfter) {
            // Add space after if not present
            if (equalsIndex + 1 < result.size()) {
                val next = result.get(equalsIndex + 1)
                if (next?.type != space) {
                    result = result.addAt(
                        Token(space, " ", Position(0, 0)),
                        equalsIndex + 1
                    )
                }
            }
        } else {
            // Remove all spaces after
            var idx = equalsIndex + 1
            while (idx < result.size()) {
                val next = result.get(idx)
                if (next?.type == space) {
                    val response = result.remove(idx)
                    result = response.container
                    if (response.token == null) break
                } else {
                    break
                }
            }
        }

        return result
    }

    private fun findEqualsAt(tokens: Container, startIndex: Int): Int {
        for (i in startIndex until tokens.size()) {
            val token = tokens.get(i)
            if (token?.type == equals) {
                return i
            }
        }
        return -1
    }
}
