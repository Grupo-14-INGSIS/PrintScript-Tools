package Common

enum class DataType {
    PUBLIC_KEY_WORD,
    FINAL_KEY_WORD,

    LET_KEY_WORD,

    IDENTIFIER,

    // Types
    STRING_TYPE,
    NUMBER_TYPE,

    // Literals
    STRING,
    NUMBER,

    // Operations
    ASSIGNATION,
    ADDITION,
    SUBTRACTION,
    MULTIPLICATION,
    DIVISION,
    PRINTLN,

    // Punctuation -> May be useful for Formatter
    SPACE,
    COLON,
    SEMICOLON,
    LINE_BREAK
}