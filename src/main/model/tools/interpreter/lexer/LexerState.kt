package src.main.model.tools.interpreter.lexer

data class LexerState(
    var isInLiteral: Boolean = false,
    var currentPiece: StringBuilder = StringBuilder(),
    val pieces: MutableList<String> = mutableListOf()
)
