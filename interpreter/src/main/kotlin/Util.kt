package interpreter.src.main.kotlin

fun Any?.formatNumber(): String {
    return when (this) {
        is Double -> if (this % 1.0 == 0.0) this.toInt().toString() else this.toString()
        is String -> if (this.startsWith("\"") && this.endsWith("\"")) {
            this.substring(1, this.length - 1)
        } else {
            this.toString()
        }
        else -> this.toString()
    }
}
