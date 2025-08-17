package src.main.model.tools.interpreter.lexer

class RegularHandler : CharacterHandler {
    override fun handle(char: Char, state: LexerState) : LexerState {
        val updatePiece = state.currentPiece + char
        return state.copy(currentPiece = updatePiece)
    }
}