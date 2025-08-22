package src.main.model.tools.interpreter.lexer

class QuoteHandler : CharacterHandler {
    override fun handle(char: Char, state: LexerState): LexerState {
        val newPiece = state.currentPiece + char

        return if (!state.isInLiteral) {
            state.copy(
                isInLiteral = true,
                currentPiece = newPiece
            )
        } else {
            state.copy(
                isInLiteral = false,
                currentPiece = "", // vacio porque ya termine de acumular
                pieces = state.pieces + newPiece

            )
        }
    }
}
