package model.structure

data class Token (
    val type: DataType,
    val content: String,
    val position: Position
)