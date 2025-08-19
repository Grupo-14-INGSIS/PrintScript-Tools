package src.main.model.tools.interpreter.lexer

class SeparatorHandler : CharacterHandler {
    override fun handle(char: Char, state: LexerState) : LexerState {
        return if (state.isInLiteral) {
            val updatePiece = state.currentPiece + char
            return state.copy(currentPiece = updatePiece)
        } else {
            val flushPieces = buildList { //flush del buffer es lo proximo a ser vaciado
                if (state.currentPiece.isNotEmpty()) {
                    add(state.currentPiece)
                }
                add(char.toString())
            }
            state.copy(
                currentPiece = "",
                pieces = state.pieces + flushPieces
            )
        }
    }
}
