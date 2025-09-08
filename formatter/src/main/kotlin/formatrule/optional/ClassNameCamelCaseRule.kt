package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class ClassNameCamelCaseRule : FormatRule {
    override fun format(source: Container): Container {
        var output = Container()

        for (token in source.container) {
            if (token.type == DataType.IDENTIFIER) {
                val name = token.content
                if (!isCamelCase(name)) {
                    output = output.addContainer(
                        Token(
                            type = null,
                            content = "// Class name '$name' should follow CamelCase\n",
                            position = token.position
                        )
                    )
                }
            }
            output = output.addContainer(token)
        }

        return output
    }

    private fun isCamelCase(name: String): Boolean {
        return name.matches(Regex("^[A-Z][a-zA-Z0-9]*$"))
    }
}
