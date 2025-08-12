package src.main.tools.interpreter.lexer

class RegularHandler : CharacterHandler {
    override fun handle(char: Char, state: LexerState) {
        state.currentPiece.append(char)
    }
}