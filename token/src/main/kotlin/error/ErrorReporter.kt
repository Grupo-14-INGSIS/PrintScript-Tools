import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

class ErrorReporter {

    fun reportError(
        operation: String,
        exception: Exception,
        tokens: Container? = null
    ) {
        println("\nERROR during $operation: ${exception.message}")

        // Mostrar ubicación si hay tokens disponibles
        tokens?.let {
            if (it.size() > 0) {
                var targetToken = it.get(0)
                for (i in 0 until it.size()) {
                    val current = it.get(i)
                    if (current != null && current.type != DataType.SPACE && current.type != DataType.LINE_BREAK) {
                        targetToken = current
                        break
                    }
                }
                targetToken?.let { token ->
                    val pos: Position = token.position
                    println("Location: Line ${pos.line}, Column ${pos.column}")
                    println("Near token: '${token.content}' (${token.type})")
                }
            }
        }
    }

    companion object {
        fun report(
            operation: String,
            exception: Exception,
            tokens: Container? = null
        ) {
            ErrorReporter().reportError(operation, exception, tokens)
        }
    }
}
