package Common

data class Token (
    val type: DataType,
    val content: String,
    val children: List<Token>,
    val position: Position
)