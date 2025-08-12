package Lexer

class RegularHandler : CharacterHandler {
    override fun handle(char: Char, state: LexerState) {
        state.currentPiece.append(char)
    }
}