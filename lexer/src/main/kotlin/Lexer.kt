package src.main.model.tools.interpreter.lexer

import common.src.main.kotlin.Container

class Lexer(val input: String) {

    private var finalState: LexerState = LexerState()
        private set

    val pieces: List<String>
        get() = finalState.pieces

    fun splitString() {
        finalState = input.fold(LexerState()) { state, char ->
            val characterType = CharacterClassifier.classify(char)
            val handler = CharacterHandlerFactory.getHandler(characterType)
            handler.handle(char, state)
        }.let { state ->
            if (state.currentPiece.isNotEmpty()) {
                state.copy(pieces = state.pieces + state.currentPiece)
            } else {
                state
            }
        }
    }

    fun createToken(): Container {
        return TokenFactory.createTokens(finalState.pieces)
    }
}



//hace lo MISMO que la version anterior, pero ahora prgormao en el mismo dominio, oculto codigo y es extensible
//generar lista de palabras separando por espacios
//tomar cada elemento y clasificarlo generanodo un token y guardarlo en container