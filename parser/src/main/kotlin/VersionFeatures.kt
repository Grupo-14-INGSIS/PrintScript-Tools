package parser.src.main.kotlin

data class VersionFeatures(
    val keywords: Set<String>,
    val types: Set<String>,
    val functions: Set<String>,
    val operators: Map<String, Int>, // operator -> precedence
    val associations: Map<String, Association>,
    val supportsConst: Boolean = false,
    val supportsIfElse: Boolean = false,
    val supportsBlocks: Boolean = false,
    val supportsBooleans: Boolean = false
)

