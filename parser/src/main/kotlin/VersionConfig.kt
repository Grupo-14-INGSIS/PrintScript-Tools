package parser.src.main.kotlin

class VersionConfig {
    companion object {
        private val VERSION_1_0 = VersionFeatures(
            keywords = setOf("let"),
            types = setOf("string", "number"),
            functions = setOf("println"),
            operators = mapOf(
                "*" to 2,
                "/" to 2,
                "+" to 1,
                "-" to 1,
                "=" to 0
            ),
            associations = mapOf(
                "*" to Association.LEFT,
                "/" to Association.LEFT,
                "+" to Association.LEFT,
                "-" to Association.LEFT,
                "=" to Association.RIGHT
            )
        )

        private val VERSION_1_1 = VersionFeatures(
            keywords = setOf("let", "const", "if", "else"),
            types = setOf("string", "number", "boolean"),
            functions = setOf("println", "readInput", "readEnv"),
            operators = mapOf(
                "*" to 2,
                "/" to 2,
                "+" to 1,
                "-" to 1,
                "=" to 0
            ),
            associations = mapOf(
                "*" to Association.LEFT,
                "/" to Association.LEFT,
                "+" to Association.LEFT,
                "-" to Association.LEFT,
                "=" to Association.RIGHT
            ),
            supportsConst = true,
            supportsIfElse = true,
            supportsBlocks = true,
            supportsBooleans = true
        )

        fun getFeatures(version: String): VersionFeatures {
            return when (version) {
                "1.0" -> VERSION_1_0
                "1.1" -> VERSION_1_1
                else -> throw IllegalArgumentException("Unsupported version: $version")
            }
        }
    }
}

