package src.main.model.structure

object TokenMap {
    private val tokenMap = mapOf(
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

    fun classifyTokenMap(piece: String): DataType? {
        return tokenMap[piece]
    }

}