package src.main.tools.interpreter.lexer

object CharacterClassifier {
    private val separators = setOf(';', ':', '\n', '+', '-', '*', '/', '=', '(', ')')
    private val quotes = setOf('"', '\'')
    private val whitespaces = setOf(' ', '\t', '\r')

    fun classify(char: Char): CharacterType {
        return when {
            char in quotes -> CharacterType.QUOTE
            char in separators -> CharacterType.SEPARATOR
            char in whitespaces -> CharacterType.WHITESPACE
            else -> CharacterType.REGULAR
        }
    }
}
// factory para clasificar caracteres