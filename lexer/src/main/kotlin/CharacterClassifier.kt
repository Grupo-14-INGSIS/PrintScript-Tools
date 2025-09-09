package lexer.src.main.kotlin

object CharacterClassifier {
    private val separators = setOf(';', ':', '\n', '+', '-', '*', '/', '=', '(', ')', '{', '}')
    private val quotes = setOf('"', '\'', '`')
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
// agrego ` par multiline
// {} son de v1.1
