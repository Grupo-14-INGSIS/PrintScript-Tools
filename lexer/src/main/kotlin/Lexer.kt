package lexer.src.main.kotlin

import container.src.main.kotlin.Container
import java.io.File

class Lexer(private val source: CharSource) {

    var list = listOf<String>()

    fun split(lotSize: Int = 8192) {
        var state = LexerState()

        val reader = source.openReader()
        val buffer = CharArray(lotSize)

        reader.use {
            var charsRead: Int
            while (reader.read(buffer).also { charsRead = it } != -1) {
                for (i in 0 until charsRead) {
                    state = classifier(buffer[i], state)
                }
            }
        }

        return finalizeParsing(state)
    }

    private fun classifier(char: Char, state: LexerState): LexerState {
        val type = CharacterClassifier.classify(char)
        val handler = CharacterHandlerFactory.getHandler(type)
        return handler.handle(char, state)
    }

    private fun finalizeParsing(state: LexerState) {
        val allPieces = if (state.currentPiece.isNotEmpty()) {
            state.pieces + state.currentPiece
        } else {
            state.pieces
        }
        list = allPieces
    }

    fun createToken(list: List<String>): Container {
        return TokenFactory.createTokens(list)
    }

// puede leer un String gracias a...
    companion object {
        fun from(input: Any): Lexer = when (input) {
            is String -> Lexer(StringCharSource(input))
            is File -> Lexer(FileCharSource(input))
            else -> throw IllegalArgumentException("Unsupported input type: ${input::class}")
        }
    }
}

// hace lo MISMO que la version anterior, pero ahora prgormao en el mismo dominio, oculto codigo y es extensible
// generar lista de palabras separando por espacios
// tomar cada elemento y clasificarlo generanodo un token y guardarlo en container
