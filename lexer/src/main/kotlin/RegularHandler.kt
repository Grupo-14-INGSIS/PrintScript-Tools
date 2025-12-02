package lexer.src.main.kotlin

class RegularHandler : CharacterHandler {
    override fun handle(char: Char, state: LexerState): Pair<LexerState, List<String>> {
        // Regular characters just accumulate into currentPiece
        val newState = state.copy(currentPiece = state.currentPiece + char, pieceReady = false)
        return Pair(newState, emptyList()) // Never completes a piece by itself
    }
}
