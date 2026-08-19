package ast.src.main.kotlin

/**
 * Representa la clasificacion semantica de un nodo en el Arbol de Sintaxis Abstracta (AST).
 * Desacopla el modelo sintactico (AST) del vocabulario lexico (Token / DataType).
 */
enum class ASTNodeType {
    // Declaraciones y Asignaciones
    LET_KEYWORD,
    CONST_KEYWORD,
    IF_KEYWORD,
    ELSE_KEYWORD,
    DECLARATION,
    VAR_DECLARATION_WITHOUT_ASSIGNATION,
    ASSIGNATION,

    // Tipos de datos
    STRING_TYPE,
    NUMBER_TYPE,
    BOOLEAN_TYPE,

    // Variables y Literales
    IDENTIFIER,
    STRING_LITERAL,
    NUMBER_LITERAL,
    BOOLEAN_LITERAL,

    // Operaciones
    ADDITION,
    SUBTRACTION,
    MULTIPLICATION,
    DIVISION,

    // Funciones e I/O
    PRINTLN,
    FUNCTION_CALL,
    READ_INPUT,
    READ_ENV,

    // Control de flujo y estructura
    IF_STATEMENT,
    BLOCK,
    SCRIPT,

    // Nodo invalido / Error
    INVALID
}
