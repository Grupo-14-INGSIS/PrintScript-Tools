package model.structure

object TokenMap {
    private val tokenMap = mapOf(
        "public" to DataType.PUBLIC_KEYWORD,
        "final" to DataType.FINAL_KEYWORD,
        "let" to DataType.LET_KEYWORD,
        "String" to DataType.STRING_TYPE,
        "Number" to DataType.NUMBER_TYPE,
        "=" to DataType.ASSIGNATION,
        "+" to DataType.ADDITION,
        "-" to DataType.SUBTRACTION,
        "*" to DataType.MULTIPLICATION,
        "/" to DataType.DIVISION,
        "println" to DataType.PRINTLN,
        " " to DataType.SPACE,
        ":" to DataType.COLON,
        ";" to DataType.SEMICOLON,
        "\n" to DataType.LINE_BREAK
    )

    fun classifyTokenMap(piece: String): DataType? {
        return tokenMap[piece]
    }

}