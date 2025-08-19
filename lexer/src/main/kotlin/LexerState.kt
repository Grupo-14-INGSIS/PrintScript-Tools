package src.main.model.tools.interpreter.lexer

data class LexerState(
    val isInLiteral: Boolean = false,
    val currentPiece: String = "", //contribuye a la implementacion de inmutabilidad
    val pieces: List<String> = emptyList()
)
