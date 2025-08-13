package src.main.model.structure

data class Token (
    val type: DataType?,
    val content: String,
    val position: Int?,) //es un identificador, no la posicion del caracter por linea y columna
