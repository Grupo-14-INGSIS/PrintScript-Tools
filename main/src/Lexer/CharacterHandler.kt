package Lexer

interface CharacterHandler {
    fun handle(char: Char, state: LexerState)
}