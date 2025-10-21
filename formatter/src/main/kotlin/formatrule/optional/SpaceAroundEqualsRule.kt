package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

// NOTA: Necesitas agregar esta línea en ConfigLoader.kt en createConfigurableRules:
// "SpaceAroundEquals" -> SpaceAroundEqualsRule()
// Y en el YAML usar: SpaceAroundEquals: true

/**
 * Esta regla ASEGURA que haya exactamente UN espacio antes y después del =
 * Es lo opuesto a NoSpaceBeforeEquals/NoSpaceAfterEquals
 */
class SpaceAroundEqualsRule : FormatRule {

    private val equals = DataType.ASSIGNATION
    private val space = DataType.SPACE

    override fun format(source: Container): Container {
        var token: Token?
        var previous: Token?
        var next: Token?
        var tokens = source
        var i = 0

        while (i < tokens.size()) {
            token = tokens.get(i)
            if (token == null) break

            if (token.type == equals) {
                // Asegurar espacio ANTES del =
                if (i > 0) {
                    previous = tokens.get(i - 1)
                    if (previous != null && previous.type != space) {
                        // No hay espacio antes, agregar uno
                        val spaceToken = Token(DataType.SPACE, " ", token.position)
                        tokens = tokens.addAt(spaceToken, i + 1)
                        i++ // Ajustar índice porque agregamos un token
                    } else if (previous != null && previous.type == space) {
                        // Ya hay espacio, asegurar que sea solo UNO
                        var spacesBeforeCount = 0
                        var checkIdx = i - 1
                        while (checkIdx >= 0) {
                            val checkToken = tokens.get(checkIdx)
                            if (checkToken != null && checkToken.type == space) {
                                spacesBeforeCount++
                                checkIdx--
                            } else {
                                break
                            }
                        }

                        // Eliminar espacios extras (dejar solo 1)
                        while (spacesBeforeCount > 1) {
                            val response = tokens.remove(i - 1)
                            tokens = response.container
                            i--
                            spacesBeforeCount--
                        }
                    }
                }

                // Asegurar espacio DESPUÉS del =
                if (i + 1 < tokens.size()) {
                    next = tokens.get(i + 1)
                    if (next != null && next.type != space) {
                        // No hay espacio después, agregar uno
                        val spaceToken = Token(DataType.SPACE, " ", token.position)
                        tokens = tokens.addAt(spaceToken, i + 1)
                    } else if (next != null && next.type == space) {
                        // Ya hay espacio, asegurar que sea solo UNO
                        var spacesAfterCount = 0
                        var checkIdx = i + 1
                        while (checkIdx < tokens.size()) {
                            val checkToken = tokens.get(checkIdx)
                            if (checkToken != null && checkToken.type == space) {
                                spacesAfterCount++
                                checkIdx++
                            } else {
                                break
                            }
                        }

                        // Eliminar espacios extras (dejar solo 1)
                        while (spacesAfterCount > 1) {
                            val response = tokens.remove(i + 2)
                            tokens = response.container
                            spacesAfterCount--
                        }
                    }
                }
                i++
            } else {
                i++
            }
        }
        return tokens
    }
}
