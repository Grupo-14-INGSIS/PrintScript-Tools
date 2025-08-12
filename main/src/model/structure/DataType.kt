package model.structure

enum class DataType {
    PUBLIC_KEYWORD,
    FINAL_KEYWORD,

    LET_KEYWORD,

    IDENTIFIER,

    // Types
    STRING_TYPE,
    NUMBER_TYPE,

    // Literals
    STRING_LITERAL,
    NUMBER_LITERAL,

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
    LINE_BREAK,

    INVALID
}