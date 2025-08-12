package src.main.tools.interpreter.lexer

class QuoteHandler : CharacterHandler {
    override fun handle(char: Char, state: LexerState) {
        state.isInLiteral = !state.isInLiteral
        state.currentPiece.append(char)

        if(!state.isInLiteral){
            state.pieces.add(state.currentPiece.toString())
            state.currentPiece.clear()
        }
    }

}