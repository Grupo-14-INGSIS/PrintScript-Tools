package model.structure

object TokenPattern {
    private val tokenPatterns = listOf(
        Regex("^[\"'].*[\"']\$") to DataType.STRING_LITERAL,
        Regex("^[0-9]+(\\.[0-9]+)?$") to DataType.NUMBER_LITERAL,
        Regex("^[a-zA-Z_][a-zA-Z0-9_]*$") to DataType.IDENTIFIER
    )

    fun classifyTokenPattern(piece: String): DataType?{
        for ((regex, type) in tokenPatterns) {
            if (piece.matches(regex)) return type
        }
        return null;
    }
}