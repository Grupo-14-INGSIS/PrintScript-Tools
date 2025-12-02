package lexer.src.main.kotlin

class WhiteSpaceHandler : CharacterHandler {
    override fun handle(char: Char, state: LexerState): Pair<LexerState, List<String>> {
        val completedPieces = mutableListOf<String>()
        var newState = state.copy(pieceReady = false) // Reset pieceReady

        if (state.isInLiteral) {
            // If inside a literal, just append the char and return
            newState = state.copy(currentPiece = state.currentPiece + char)
        } else {
            // If not inside a literal
            if (state.currentPiece.isNotEmpty()) {
                // The currentPiece is a completed token
                completedPieces.add(state.currentPiece)
            }
            // Whitespace itself is also a piece (e.g. " ", "\n")
            completedPieces.add(char.toString())
            newState = state.copy(currentPiece = "") // Reset currentPiece
        }
        return Pair(newState, completedPieces)
    }
}
