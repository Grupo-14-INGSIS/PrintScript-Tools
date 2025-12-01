package interpreter.src.main.kotlin

fun Any?.formatNumber(): String {
    return when (this) {
        is Double -> if (this % 1.0 == 0.0) this.toInt().toString() else this.toString()
        else -> this.toString()
    }
}
