package lexer.src.main.kotlin

import tokendata.src.main.kotlin.DataType

/**
 * Factoría que ensambla la lista de plugins de tokenización según la versión del lenguaje
 * o la configuración deseada.
 */
object TokenPluginFactory {

    private val v10Keywords: Map<String, DataType> = mapOf(
        "let" to DataType.LET_KEYWORD,
        "string" to DataType.STRING_TYPE,
        "number" to DataType.NUMBER_TYPE,
        "=" to DataType.ASSIGNATION,
        "+" to DataType.ADDITION,
        "-" to DataType.SUBTRACTION,
        "*" to DataType.MULTIPLICATION,
        "/" to DataType.DIVISION,
        "println" to DataType.PRINTLN,
        " " to DataType.SPACE,
        ":" to DataType.COLON,
        ";" to DataType.SEMICOLON,
        "\n" to DataType.LINE_BREAK,
        "(" to DataType.OPEN_PARENTHESIS,
        ")" to DataType.CLOSE_PARENTHESIS
    )

    private val v11Keywords: Map<String, DataType> = v10Keywords + mapOf(
        "const" to DataType.CONST_KEYWORD,
        "if" to DataType.IF_KEYWORD,
        "else" to DataType.ELSE_KEYWORD,
        "boolean" to DataType.BOOLEAN_TYPE,
        "readInput" to DataType.READ_INPUT,
        "readEnv" to DataType.READ_ENV,
        "{" to DataType.OPEN_BRACE,
        "}" to DataType.CLOSE_BRACE,
        "true" to DataType.BOOLEAN_LITERAL,
        "false" to DataType.BOOLEAN_LITERAL
    )

    fun createPlugins(version: String = "1.0"): List<TokenPlugin> {
        val keywords = when (version) {
            "1.1" -> v11Keywords
            else -> v10Keywords
        }

        return listOf(
            ExactMatchTokenPlugin(keywords),
            RegexTokenPlugin(Regex("^[\"'].*[\"']\$"), DataType.STRING_LITERAL),
            RegexTokenPlugin(Regex("^[0-9]+(\\.[0-9]+)?\$"), DataType.NUMBER_LITERAL),
            RegexTokenPlugin(Regex("^[a-zA-Z_][a-zA-Z0-9_]*\$"), DataType.IDENTIFIER)
        )
    }
}
