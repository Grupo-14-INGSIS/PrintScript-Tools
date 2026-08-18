package lexer.src.main.kotlin

object CharacterClassifier {
    private val defaultSeparators = setOf(';', ':', '\n', '+', '-', '*', '/', '=', '(', ')', '{', '}', ',')
    private val defaultQuotes = setOf('"', '\'', '`')
    private val defaultWhitespaces = setOf(' ', '\t', '\r')

    private val customSeparators = mutableSetOf<Char>()
    private val customQuotes = mutableSetOf<Char>()
    private val customWhitespaces = mutableSetOf<Char>()

    fun registerSeparator(char: Char) {
        customSeparators.add(char)
    }

    fun registerQuote(char: Char) {
        customQuotes.add(char)
    }

    fun registerWhitespace(char: Char) {
        customWhitespaces.add(char)
    }

    fun classify(char: Char): CharacterType {
        return when {
            char in defaultQuotes || char in customQuotes -> CharacterType.QUOTE
            char in defaultSeparators || char in customSeparators -> CharacterType.SEPARATOR
            char in defaultWhitespaces || char in customWhitespaces -> CharacterType.WHITESPACE
            else -> CharacterType.REGULAR
        }
    }
}
// factory para clasificar caracteres
// agrego ` par multiline
// {} son de v1.1
