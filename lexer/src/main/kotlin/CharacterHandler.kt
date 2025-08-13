package src.main.model.tools.interpreter.lexer

interface CharacterHandler {
    fun handle(char: Char, state: LexerState)
}