package src.main.model.tools.interpreter.lexer

class WhiteSpaceHandler : CharacterHandler {
    override fun handle(char: Char, state: LexerState) {
        if (state.isInLiteral) {
            state.currentPiece.append(char)
        } else {
            if (state.currentPiece.isNotEmpty()) {
                state.pieces.add(state.currentPiece.toString())
                state.currentPiece.clear()
            }
            state.pieces.add(char.toString()) //el espacio solo tambien lo tokenizo. MUY IMPORTANTE!!
        }
    }
}