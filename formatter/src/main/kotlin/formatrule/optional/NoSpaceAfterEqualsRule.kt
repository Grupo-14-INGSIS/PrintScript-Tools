package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class NoSpaceAfterEqualsRule : FormatRule {

    private val equals = DataType.ASSIGNATION
    private val space = DataType.SPACE

    override fun format(source: Container): Container {
        var token: Token?
        var next: Token?
        var tokens: Container = source
        var i = 0
        while (i < tokens.size()) {
            token = tokens.get(i)
            if (token == null) break

            /*
             * Delete every space after an equals token
             */
            if (token.type == equals) {
                // Eliminar TODOS los espacios consecutivos después del =
                while (i + 1 < tokens.size()) {
                    next = tokens.get(i + 1)
                    if (next == null) {
                        break // End of tokens
                    } else if (next.type == space) {
                        val response = tokens.remove(i + 1)
                        tokens = response.container
                        if (response.token == null) break
                        // No incrementamos i porque el siguiente token ahora está en i+1
                    } else {
                        // No es un espacio, salimos del bucle interno
                        break
                    }
                }
                i++ // Avanzamos después de procesar el equals
            } else {
                i++
            }
        }
        return tokens
    }
}
