package lexer.src.main.kotlin

class QuoteHandler : CharacterHandler {
    override fun handle(char: Char, state: LexerState): Pair<LexerState, List<String>> {
        val newPiece = state.currentPiece + char

        return if (!state.isInLiteral) { // Starting a literal
            Pair(
                state.copy(
                    isInLiteral = true,
                    currentPiece = newPiece,
                    pieceReady = false
                ),
                emptyList() // No completed pieces yet
            )
        } else { // Ending a literal
            Pair(
                state.copy(
                    isInLiteral = false,
                    currentPiece = "", // The current piece has been yielded
                    pieceReady = false // Reset pieceReady after yielding
                ),
                listOf(newPiece) // The completed literal
            )
        }
    }
}
