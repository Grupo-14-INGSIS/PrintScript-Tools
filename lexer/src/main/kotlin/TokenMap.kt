package lexer.src.main.kotlin

import tokendata.src.main.kotlin.DataType

object TokenMap {
    private val tokenMapv10 = mapOf(
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

    private val tokenMapv11 = tokenMapv10 + mapOf(
        "const" to DataType.CONST_KEYWORD,
        "if" to DataType.IF_KEYWORD,
        "else" to DataType.ELSE_KEYWORD,
        "boolean" to DataType.BOOBLEAN_TYPE,
        "readInput" to DataType.READ_INPUT,
        "readEnv" to DataType.READ_ENV,
        "{" to DataType.OPEN_BRACE,
        "}" to DataType.CLOSE_BRACE,
        "true" to DataType.BOOLEAN_LITERAL,
        "false" to DataType.BOOLEAN_LITERAL
    )

    fun classifyTokenMap(piece: String, version: String = "1.0"): DataType? { // 1.0 como default
        if (version == "1.0") {
            if (tokenMapv10.containsKey(piece)) {
                return tokenMapv10[piece]
            }
        } else if (version == "1.1") {
            if (tokenMapv11.containsKey(piece)) {
                return tokenMapv11[piece]
            }
        }
        // No version specified or invalid token
        return tokenMapv10[piece]
    } // si agrego una nueva version solo cambio aca
}
