package src.main.tools.interpreter.lexer

interface CharacterHandler {
    fun handle(char: Char, state: LexerState)
}