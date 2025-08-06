package Common

data class Token (
    val type: DataType,
    val content: String,
    val children: List<Token> = listOf(),
    val position: Position
)