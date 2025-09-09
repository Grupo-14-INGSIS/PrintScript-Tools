package lexer.src.main.kotlin

data class LexerState(
    val isInLiteral: Boolean = false,
    val currentPiece: String = "", // contribuye a la implementacion de inmutabilidad
    val pieces: List<String> = emptyList()
)
