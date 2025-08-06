package Common

data class Token (
    val position: Position,
    val type: DataType,
    val content: String
)