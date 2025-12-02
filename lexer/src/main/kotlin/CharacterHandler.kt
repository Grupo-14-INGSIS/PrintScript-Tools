package lexer.src.main.kotlin

interface CharacterHandler {
    fun handle(char: Char, state: LexerState): Pair<LexerState, List<String>>
}
