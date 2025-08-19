package src.main.model.tools.interpreter.lexer

class WhiteSpaceHandler : CharacterHandler {
    override fun handle(char: Char, state: LexerState) : LexerState{
        return if (state.isInLiteral) {
            return state.copy(currentPiece = state.currentPiece + char)
        } else {
            val flushedPieces = buildList {
                if (state.currentPiece.isNotEmpty()) {
                    add(state.currentPiece)
                }
                add(char.toString())
            }

            state.copy(
                currentPiece = "",
                pieces = state.pieces + flushedPieces
            )
        }
    }
}
