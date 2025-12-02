package lexer.src.main.kotlin

data class LexerState(
    val isInLiteral: Boolean = false,
    val currentPiece: String = "",
    val pieceReady: Boolean = false
)

// es el estado inmutable, el contenedor de caraveres mientras el lexer recorre el archivo
