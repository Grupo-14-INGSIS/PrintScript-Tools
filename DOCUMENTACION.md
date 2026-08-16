# 📘 Documentación Técnica Completa de PrintScript-Tools

> **Descripción General:** **PrintScript-Tools** es una plataforma completa en **Kotlin** para las versiones 1.0 y 1.1 del lenguaje **PrintScript**. Este documento detalla cada módulo, archivo, clase y función, incluyendo sus **entradas (inputs)**, **salidas (outputs)** y **explicación detallada de su comportamiento y lógica interna**.

## 📌 Índice General de Módulos

- [Módulo `ANALYZER`](#-módulo-analyzer) - Orquesta el análisis estático completo (Lexer + Parser + Linter) notificando avances y errores.
- [Módulo `AST`](#-módulo-ast) - Define la jerarquía de nodos del Árbol de Sintaxis Abstracta (ASTNode) para representación intermedia.
- [Módulo `CLI`](#-módulo-cli) - Interfaz de línea de comandos del proyecto.
- [Módulo `CONTAINER`](#-módulo-container) - Contenedor de elementos y respuestas de remoción en el pipeline.
- [Módulo `ERROR`](#-módulo-error) - Gestión centralizada y reporte de errores sintácticos, de linting y de ejecución.
- [Módulo `EXECUTOR`](#-módulo-executor) - Orquesta la ejecución completa de un script (Lexer + Parser + Interpreter).
- [Módulo `FORMATTER`](#-módulo-formatter) - Formateador de código fuente que aplica reglas de estilo (espaciado, saltos de línea e indentación).
- [Módulo `FORMATTERACTION`](#-módulo-formatteraction) - Acción que orquesta la tokenización (Lexer) y el formateo (Formatter) de un archivo.
- [Módulo `INPUTPROVIDER`](#-módulo-inputprovider) - Abstracción e implementaciones para la lectura interactiva de entrada de usuario (`readInput`).
- [Módulo `INTERPRETER`](#-módulo-interpreter) - Motor de ejecución del AST. Evalúa expresiones, asignaciones, variables, bloques `if/else`, `println`, `readInput` y `readEnv`.
- [Módulo `LEXER`](#-módulo-lexer) - Transforma el código fuente en una secuencia de tokens mediante Chain of Responsibility y patrones de diseño.
- [Módulo `LINTER`](#-módulo-linter) - Analizador estático de código que evalúa el AST y tokens frente a reglas configurables (convención de nombres, `const`, etc.).
- [Módulo `LIST`](#-módulo-list) - Implementación personalizada de lista enlazada (`LinkedList`).
- [Módulo `MAINAPP`](#-módulo-mainApp) - Punto de entrada de la aplicación principal.
- [Módulo `PARSER`](#-módulo-parser) - Parsea la secuencia de tokens en un AST validando las reglas sintácticas mediante Pratt Parsing.
- [Módulo `PROGRESS`](#-módulo-progress) - Sistema de notificación de progreso e indicadores para tareas compuestas.
- [Módulo `RUNNER`](#-módulo-runner) - Fachada principal que expone métodos de alto nivel para ejecutar, analizar y formatear código.
- [Módulo `TOKEN`](#-módulo-token) - Define la estructura de datos fundamental del Token (tipo, valor, posición).
- [Módulo `TOKENDATA`](#-módulo-tokendata) - Contiene los tipos de datos enums y posiciones de código (DataType, Position).

---

## 📦 Módulo `token`

**Propósito del Módulo:** Define la estructura de datos fundamental del Token (tipo, valor, posición).

**Cantidad de Archivos:** `1` archivos de código fuente.

### 📄 Archivo: [Token.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/token/src/main/kotlin/Token.kt)

- **Ruta:** `token/src/main/kotlin/Token.kt`
- **Package:** `token.src.main.kotlin`
- **Líneas de Código:** `10`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Token`**
  - **Firma completa:** `data class Token(     val type: DataType?,     val content: String,     val position: Position )`



---

## 📦 Módulo `tokendata`

**Propósito del Módulo:** Contiene los tipos de datos enums y posiciones de código (DataType, Position).

**Cantidad de Archivos:** `2` archivos de código fuente.

### 📄 Archivo: [DataType.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/tokendata/src/main/kotlin/DataType.kt)

- **Ruta:** `tokendata/src/main/kotlin/DataType.kt`
- **Package:** `tokendata.src.main.kotlin`
- **Líneas de Código:** `67`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`DataType`**
  - **Firma completa:** `enum class DataType`


### 📄 Archivo: [Position.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/tokendata/src/main/kotlin/Position.kt)

- **Ruta:** `tokendata/src/main/kotlin/Position.kt`
- **Package:** `tokendata.src.main.kotlin`
- **Líneas de Código:** `8`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Position`**
  - **Firma completa:** `data class Position(     val line: Int,     val column: Int )`



---

## 📦 Módulo `ast`

**Propósito del Módulo:** Define la jerarquía de nodos del Árbol de Sintaxis Abstracta (ASTNode) para representación intermedia.

**Cantidad de Archivos:** `1` archivos de código fuente.

### 📄 Archivo: [ASTNode.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/ast/src/main/kotlin/ASTNode.kt)

- **Ruta:** `ast/src/main/kotlin/ASTNode.kt`
- **Package:** `ast.src.main.kotlin`
- **Líneas de Código:** `31`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`ASTNode`**
  - **Firma completa:** `class ASTNode(     val type: DataType?,     val content: String,     val position: Position,     val children: List<ASTNode> )`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`toString`**
  - **Firma:** `override fun toString(): String`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `String`
  - **💡 ¿Qué hace esta función?:** Devuelve la representación en formato de texto (String) de esta estructura para depuración e impresión.
- **`toString`**
  - **Firma:** `private fun toString(indent: Int): String`
  - **📥 Entradas (Inputs):** `indent: Int`
  - **📤 Salida (Output / Retorno):** `String`
  - **💡 ¿Qué hace esta función?:** Genera y retorna una cadena de texto formateada en forma de árbol sintáctico con la sangría especificada.



---

## 📦 Módulo `lexer`

**Propósito del Módulo:** Transforma el código fuente en una secuencia de tokens mediante Chain of Responsibility y patrones de diseño.

**Cantidad de Archivos:** `18` archivos de código fuente.

### 📄 Archivo: [CharacterClassifier.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/lexer/src/main/kotlin/CharacterClassifier.kt)

- **Ruta:** `lexer/src/main/kotlin/CharacterClassifier.kt`
- **Package:** `lexer.src.main.kotlin`
- **Líneas de Código:** `19`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`CharacterClassifier`**
  - **Firma completa:** `object CharacterClassifier`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`classify`**
  - **Firma:** `fun classify(char: Char): CharacterType`
  - **📥 Entradas (Inputs):** `char: Char`
  - **📤 Salida (Output / Retorno):** `CharacterType`
  - **💡 ¿Qué hace esta función?:** Realiza el procesamiento léxico específico de `classify`, analizando caracteres para formar el token adecuadamente.


### 📄 Archivo: [CharacterHandler.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/lexer/src/main/kotlin/CharacterHandler.kt)

- **Ruta:** `lexer/src/main/kotlin/CharacterHandler.kt`
- **Package:** `lexer.src.main.kotlin`
- **Líneas de Código:** `5`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`CharacterHandler`**
  - **Firma completa:** `interface CharacterHandler`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`handle`**
  - **Firma:** `fun handle(char: Char, state: LexerState): Pair<LexerState, List<String>>`
  - **📥 Entradas (Inputs):** `char: Char, state: LexerState`
  - **📤 Salida (Output / Retorno):** `Pair<LexerState, List<String>>`
  - **💡 ¿Qué hace esta función?:** Evalúa el carácter o token actual. Si coincide con la responsabilidad del manejador, genera el token correspondiente; de lo contrario, delega al siguiente manejador de la cadena.


### 📄 Archivo: [CharacterHandlerFactory.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/lexer/src/main/kotlin/CharacterHandlerFactory.kt)

- **Ruta:** `lexer/src/main/kotlin/CharacterHandlerFactory.kt`
- **Package:** `lexer.src.main.kotlin`
- **Líneas de Código:** `18`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`CharacterHandlerFactory`**
  - **Firma completa:** `object CharacterHandlerFactory`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`getHandler`**
  - **Firma:** `fun getHandler(type: CharacterType): CharacterHandler`
  - **📥 Entradas (Inputs):** `type: CharacterType`
  - **📤 Salida (Output / Retorno):** `CharacterHandler`
  - **💡 ¿Qué hace esta función?:** Realiza el procesamiento léxico específico de `getHandler`, analizando caracteres para formar el token adecuadamente.


### 📄 Archivo: [CharacterType.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/lexer/src/main/kotlin/CharacterType.kt)

- **Ruta:** `lexer/src/main/kotlin/CharacterType.kt`
- **Package:** `lexer.src.main.kotlin`
- **Líneas de Código:** `5`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`CharacterType`**
  - **Firma completa:** `enum class CharacterType`


### 📄 Archivo: [CharSource.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/lexer/src/main/kotlin/CharSource.kt)

- **Ruta:** `lexer/src/main/kotlin/CharSource.kt`
- **Package:** `lexer.src.main.kotlin`
- **Líneas de Código:** `7`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`CharSource`**
  - **Firma completa:** `interface CharSource`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`openReader`**
  - **Firma:** `fun openReader(): Reader`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Reader`
  - **💡 ¿Qué hace esta función?:** Realiza el procesamiento léxico específico de `openReader`, analizando caracteres para formar el token adecuadamente.


### 📄 Archivo: [FileCharSource.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/lexer/src/main/kotlin/FileCharSource.kt)

- **Ruta:** `lexer/src/main/kotlin/FileCharSource.kt`
- **Package:** `lexer.src.main.kotlin`
- **Líneas de Código:** `12`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`FileCharSource`**
  - **Firma completa:** `class FileCharSource(private val file: File) : CharSource`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`openReader`**
  - **Firma:** `override fun openReader(): Reader`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Reader`
  - **💡 ¿Qué hace esta función?:** Realiza el procesamiento léxico específico de `openReader`, analizando caracteres para formar el token adecuadamente.


### 📄 Archivo: [InputStreamCharSource.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/lexer/src/main/kotlin/InputStreamCharSource.kt)

- **Ruta:** `lexer/src/main/kotlin/InputStreamCharSource.kt`
- **Package:** `lexer.src.main.kotlin`
- **Líneas de Código:** `12`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`InputStreamCharSource`**
  - **Firma completa:** `class InputStreamCharSource(private val inputStream: InputStream) : CharSource`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`openReader`**
  - **Firma:** `override fun openReader(): Reader`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Reader`
  - **💡 ¿Qué hace esta función?:** Realiza el procesamiento léxico específico de `openReader`, analizando caracteres para formar el token adecuadamente.


### 📄 Archivo: [Lexer.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/lexer/src/main/kotlin/Lexer.kt)

- **Ruta:** `lexer/src/main/kotlin/Lexer.kt`
- **Package:** `lexer.src.main.kotlin`
- **Líneas de Código:** `113`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Lexer`**
  - **Firma completa:** `class Lexer`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`split`**
  - **Firma:** `fun split(): Sequence<String>`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Sequence<String>`
  - **💡 ¿Qué hace esta función?:** Realiza el procesamiento léxico específico de `split`, analizando caracteres para formar el token adecuadamente.
- **`classifier`**
  - **Firma:** `fun classifier(char: Char, state: LexerState): Pair<LexerState, List<String>>`
  - **📥 Entradas (Inputs):** `char: Char, state: LexerState`
  - **📤 Salida (Output / Retorno):** `Pair<LexerState, List<String>>`
  - **💡 ¿Qué hace esta función?:** Realiza el procesamiento léxico específico de `classifier`, analizando caracteres para formar el token adecuadamente.
- **`lexIntoStatements`**
  - **Firma:** `fun lexIntoStatements(): Sequence<Container>`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Sequence<Container>`
  - **💡 ¿Qué hace esta función?:** Realiza el procesamiento léxico específico de `lexIntoStatements`, analizando caracteres para formar el token adecuadamente.
- **`from`**
  - **Firma:** `fun from(input: Any, version: String = "1.0"): Lexer`
  - **📥 Entradas (Inputs):** `input: Any, version: String = "1.0"`
  - **📤 Salida (Output / Retorno):** `Lexer`
  - **💡 ¿Qué hace esta función?:** Realiza el procesamiento léxico específico de `from`, analizando caracteres para formar el token adecuadamente.


### 📄 Archivo: [LexerState.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/lexer/src/main/kotlin/LexerState.kt)

- **Ruta:** `lexer/src/main/kotlin/LexerState.kt`
- **Package:** `lexer.src.main.kotlin`
- **Líneas de Código:** `9`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`LexerState`**
  - **Firma completa:** `data class LexerState(     val isInLiteral: Boolean = false,     val currentPiece: String = "",     val pieceReady: Boolean = false )`


### 📄 Archivo: [PeekingIterator.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/lexer/src/main/kotlin/PeekingIterator.kt)

- **Ruta:** `lexer/src/main/kotlin/PeekingIterator.kt`
- **Package:** `lexer.src.main.kotlin`
- **Líneas de Código:** `34`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`PeekingIterator`**
  - **Firma completa:** `class PeekingIterator<T>(private val iterator: Iterator<T>) : Iterator<T>`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`primeNext`**
  - **Firma:** `private fun primeNext()`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Realiza el procesamiento léxico específico de `primeNext`, analizando caracteres para formar el token adecuadamente.
- **`hasNext`**
  - **Firma:** `override fun hasNext(): Boolean`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Boolean`
  - **💡 ¿Qué hace esta función?:** Indica si todavía quedan elementos (caracteres o tokens) pendientes por procesar en la fuente.
- **`next`**
  - **Firma:** `override fun next(): T`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `T`
  - **💡 ¿Qué hace esta función?:** Consume y devuelve el siguiente elemento (carácter o token) de la fuente, avanzando la posición del cursor.
- **`peek`**
  - **Firma:** `fun peek(): T`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `T`
  - **💡 ¿Qué hace esta función?:** Examina el siguiente carácter o token de la fuente de datos sin hacer avanzar el cursor de lectura.


### 📄 Archivo: [QuoteHandler.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/lexer/src/main/kotlin/QuoteHandler.kt)

- **Ruta:** `lexer/src/main/kotlin/QuoteHandler.kt`
- **Package:** `lexer.src.main.kotlin`
- **Líneas de Código:** `27`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`QuoteHandler`**
  - **Firma completa:** `class QuoteHandler : CharacterHandler`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`handle`**
  - **Firma:** `override fun handle(char: Char, state: LexerState): Pair<LexerState, List<String>>`
  - **📥 Entradas (Inputs):** `char: Char, state: LexerState`
  - **📤 Salida (Output / Retorno):** `Pair<LexerState, List<String>>`
  - **💡 ¿Qué hace esta función?:** Evalúa el carácter o token actual. Si coincide con la responsabilidad del manejador, genera el token correspondiente; de lo contrario, delega al siguiente manejador de la cadena.


### 📄 Archivo: [RegularHandler.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/lexer/src/main/kotlin/RegularHandler.kt)

- **Ruta:** `lexer/src/main/kotlin/RegularHandler.kt`
- **Package:** `lexer.src.main.kotlin`
- **Líneas de Código:** `9`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`RegularHandler`**
  - **Firma completa:** `class RegularHandler : CharacterHandler`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`handle`**
  - **Firma:** `override fun handle(char: Char, state: LexerState): Pair<LexerState, List<String>>`
  - **📥 Entradas (Inputs):** `char: Char, state: LexerState`
  - **📤 Salida (Output / Retorno):** `Pair<LexerState, List<String>>`
  - **💡 ¿Qué hace esta función?:** Evalúa el carácter o token actual. Si coincide con la responsabilidad del manejador, genera el token correspondiente; de lo contrario, delega al siguiente manejador de la cadena.


### 📄 Archivo: [SeparatorHandler.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/lexer/src/main/kotlin/SeparatorHandler.kt)

- **Ruta:** `lexer/src/main/kotlin/SeparatorHandler.kt`
- **Package:** `lexer.src.main.kotlin`
- **Líneas de Código:** `23`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`SeparatorHandler`**
  - **Firma completa:** `class SeparatorHandler : CharacterHandler`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`handle`**
  - **Firma:** `override fun handle(char: Char, state: LexerState): Pair<LexerState, List<String>>`
  - **📥 Entradas (Inputs):** `char: Char, state: LexerState`
  - **📤 Salida (Output / Retorno):** `Pair<LexerState, List<String>>`
  - **💡 ¿Qué hace esta función?:** Evalúa el carácter o token actual. Si coincide con la responsabilidad del manejador, genera el token correspondiente; de lo contrario, delega al siguiente manejador de la cadena.


### 📄 Archivo: [StringCharSource.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/lexer/src/main/kotlin/StringCharSource.kt)

- **Ruta:** `lexer/src/main/kotlin/StringCharSource.kt`
- **Package:** `lexer.src.main.kotlin`
- **Líneas de Código:** `10`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`StringCharSource`**
  - **Firma completa:** `class StringCharSource(private val content: String) : CharSource`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`openReader`**
  - **Firma:** `override fun openReader(): Reader`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Reader`
  - **💡 ¿Qué hace esta función?:** Realiza el procesamiento léxico específico de `openReader`, analizando caracteres para formar el token adecuadamente.


### 📄 Archivo: [TokenFactory.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/lexer/src/main/kotlin/TokenFactory.kt)

- **Ruta:** `lexer/src/main/kotlin/TokenFactory.kt`
- **Package:** `lexer.src.main.kotlin`
- **Líneas de Código:** `41`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`TokenFactory`**
  - **Firma completa:** `object TokenFactory`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`createTokens`**
  - **Firma:** `fun createTokens(pieces: List<String>, version: String = "1.0"): Container`
  - **📥 Entradas (Inputs):** `pieces: List<String>, version: String = "1.0"`
  - **📤 Salida (Output / Retorno):** `Container`
  - **💡 ¿Qué hace esta función?:** Realiza el procesamiento léxico específico de `createTokens`, analizando caracteres para formar el token adecuadamente.


### 📄 Archivo: [TokenMap.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/lexer/src/main/kotlin/TokenMap.kt)

- **Ruta:** `lexer/src/main/kotlin/TokenMap.kt`
- **Package:** `lexer.src.main.kotlin`
- **Líneas de Código:** `49`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`TokenMap`**
  - **Firma completa:** `object TokenMap`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`classifyTokenMap`**
  - **Firma:** `fun classifyTokenMap(piece: String, version: String = "1.0"): DataType?`
  - **📥 Entradas (Inputs):** `piece: String, version: String = "1.0"`
  - **📤 Salida (Output / Retorno):** `DataType?`
  - **💡 ¿Qué hace esta función?:** Realiza el procesamiento léxico específico de `classifyTokenMap`, analizando caracteres para formar el token adecuadamente.


### 📄 Archivo: [TokenPattern.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/lexer/src/main/kotlin/TokenPattern.kt)

- **Ruta:** `lexer/src/main/kotlin/TokenPattern.kt`
- **Package:** `lexer.src.main.kotlin`
- **Líneas de Código:** `18`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`TokenPattern`**
  - **Firma completa:** `object TokenPattern`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`classifyTokenPattern`**
  - **Firma:** `fun classifyTokenPattern(piece: String): DataType?`
  - **📥 Entradas (Inputs):** `piece: String`
  - **📤 Salida (Output / Retorno):** `DataType?`
  - **💡 ¿Qué hace esta función?:** Realiza el procesamiento léxico específico de `classifyTokenPattern`, analizando caracteres para formar el token adecuadamente.


### 📄 Archivo: [WhiteSpaceHandler.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/lexer/src/main/kotlin/WhiteSpaceHandler.kt)

- **Ruta:** `lexer/src/main/kotlin/WhiteSpaceHandler.kt`
- **Package:** `lexer.src.main.kotlin`
- **Líneas de Código:** `23`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`WhiteSpaceHandler`**
  - **Firma completa:** `class WhiteSpaceHandler : CharacterHandler`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`handle`**
  - **Firma:** `override fun handle(char: Char, state: LexerState): Pair<LexerState, List<String>>`
  - **📥 Entradas (Inputs):** `char: Char, state: LexerState`
  - **📤 Salida (Output / Retorno):** `Pair<LexerState, List<String>>`
  - **💡 ¿Qué hace esta función?:** Evalúa el carácter o token actual. Si coincide con la responsabilidad del manejador, genera el token correspondiente; de lo contrario, delega al siguiente manejador de la cadena.



---

## 📦 Módulo `parser`

**Propósito del Módulo:** Parsea la secuencia de tokens en un AST validando las reglas sintácticas mediante Pratt Parsing.

**Cantidad de Archivos:** `6` archivos de código fuente.

### 📄 Archivo: [Association.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/parser/src/main/kotlin/Association.kt)

- **Ruta:** `parser/src/main/kotlin/Association.kt`
- **Package:** `parser.src.main.kotlin`
- **Líneas de Código:** `7`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Association`**
  - **Firma completa:** `enum class Association`


### 📄 Archivo: [Parser.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/parser/src/main/kotlin/Parser.kt)

- **Ruta:** `parser/src/main/kotlin/Parser.kt`
- **Package:** `parser.src.main.kotlin`
- **Líneas de Código:** `710`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Parser`**
  - **Firma completa:** `class Parser`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`parse`**
  - **Firma:** `fun parse(): ASTNode`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `ASTNode`
  - **💡 ¿Qué hace esta función?:** Procesa la secuencia de tokens de entrada y construye el Árbol de Sintaxis Abstracta (AST) correspondiente.
- **`format`**
  - **Firma:** `private fun format(): Container`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Container`
  - **💡 ¿Qué hace esta función?:** Transforma la secuencia de tokens aplicando las reglas de espaciado, saltos de línea e indentación configuradas.
- **`stmtParse`**
  - **Firma:** `fun stmtParse(tokens: Container): ASTNode`
  - **📥 Entradas (Inputs):** `tokens: Container`
  - **📤 Salida (Output / Retorno):** `ASTNode`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `stmtParse` a partir de la secuencia de tokens.
- **`isDeclarationWithAssignment`**
  - **Firma:** `private fun isDeclarationWithAssignment(tokens: Container): Boolean`
  - **📥 Entradas (Inputs):** `tokens: Container`
  - **📤 Salida (Output / Retorno):** `Boolean`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `isDeclarationWithAssignment` a partir de la secuencia de tokens.
- **`isDeclarationWithoutAssignment`**
  - **Firma:** `private fun isDeclarationWithoutAssignment(tokens: Container): Boolean`
  - **📥 Entradas (Inputs):** `tokens: Container`
  - **📤 Salida (Output / Retorno):** `Boolean`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `isDeclarationWithoutAssignment` a partir de la secuencia de tokens.
- **`parseDeclarationWithoutAssignment`**
  - **Firma:** `private fun parseDeclarationWithoutAssignment(tokens: Container): ASTNode`
  - **📥 Entradas (Inputs):** `tokens: Container`
  - **📤 Salida (Output / Retorno):** `ASTNode`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `parseDeclarationWithoutAssignment` a partir de la secuencia de tokens.
- **`isSimpleAssignment`**
  - **Firma:** `private fun isSimpleAssignment(tokens: Container): Boolean`
  - **📥 Entradas (Inputs):** `tokens: Container`
  - **📤 Salida (Output / Retorno):** `Boolean`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `isSimpleAssignment` a partir de la secuencia de tokens.
- **`isFunctionCall`**
  - **Firma:** `private fun isFunctionCall(tokens: Container): Boolean`
  - **📥 Entradas (Inputs):** `tokens: Container`
  - **📤 Salida (Output / Retorno):** `Boolean`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `isFunctionCall` a partir de la secuencia de tokens.
- **`isArith`**
  - **Firma:** `private fun isArith(tokens: Container): Boolean`
  - **📥 Entradas (Inputs):** `tokens: Container`
  - **📤 Salida (Output / Retorno):** `Boolean`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `isArith` a partir de la secuencia de tokens.
- **`isLiteral`**
  - **Firma:** `private fun isLiteral(tokens: Container): Boolean`
  - **📥 Entradas (Inputs):** `tokens: Container`
  - **📤 Salida (Output / Retorno):** `Boolean`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `isLiteral` a partir de la secuencia de tokens.
- **`isIf`**
  - **Firma:** `private fun isIf(tokens: Container): Boolean`
  - **📥 Entradas (Inputs):** `tokens: Container`
  - **📤 Salida (Output / Retorno):** `Boolean`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `isIf` a partir de la secuencia de tokens.
- **`isIfElse`**
  - **Firma:** `private fun isIfElse(tokens: Container): Boolean`
  - **📥 Entradas (Inputs):** `tokens: Container`
  - **📤 Salida (Output / Retorno):** `Boolean`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `isIfElse` a partir de la secuencia de tokens.
- **`parseDeclarationWithAssignment`**
  - **Firma:** `private fun parseDeclarationWithAssignment(tokens: Container): ASTNode`
  - **📥 Entradas (Inputs):** `tokens: Container`
  - **📤 Salida (Output / Retorno):** `ASTNode`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `parseDeclarationWithAssignment` a partir de la secuencia de tokens.
- **`parseSimpleAssignment`**
  - **Firma:** `private fun parseSimpleAssignment(tokens: Container): ASTNode`
  - **📥 Entradas (Inputs):** `tokens: Container`
  - **📤 Salida (Output / Retorno):** `ASTNode`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `parseSimpleAssignment` a partir de la secuencia de tokens.
- **`ifStmtParse`**
  - **Firma:** `fun ifStmtParse(tokens: Container): ASTNode`
  - **📥 Entradas (Inputs):** `tokens: Container`
  - **📤 Salida (Output / Retorno):** `ASTNode`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `ifStmtParse` a partir de la secuencia de tokens.
- **`parseBlock`**
  - **Firma:** `private fun parseBlock(tokens: Container): ASTNode`
  - **📥 Entradas (Inputs):** `tokens: Container`
  - **📤 Salida (Output / Retorno):** `ASTNode`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `parseBlock` a partir de la secuencia de tokens.
- **`expParse`**
  - **Firma:** `fun expParse(tokens: Container): ASTNode`
  - **📥 Entradas (Inputs):** `tokens: Container`
  - **📤 Salida (Output / Retorno):** `ASTNode`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `expParse` a partir de la secuencia de tokens.
- **`parseFunctionCall`**
  - **Firma:** `private fun parseFunctionCall(tokens: Container): ASTNode`
  - **📥 Entradas (Inputs):** `tokens: Container`
  - **📤 Salida (Output / Retorno):** `ASTNode`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `parseFunctionCall` a partir de la secuencia de tokens.
- **`findTokenIndex`**
  - **Firma:** `private fun findTokenIndex(tokens: Container, type: DataType, startFrom: Int = 0): Int`
  - **📥 Entradas (Inputs):** `tokens: Container, type: DataType, startFrom: Int = 0`
  - **📤 Salida (Output / Retorno):** `Int`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `findTokenIndex` a partir de la secuencia de tokens.
- **`findMatchingClosingParenthesis`**
  - **Firma:** `private fun findMatchingClosingParenthesis(tokens: Container, openIndex: Int): Int`
  - **📥 Entradas (Inputs):** `tokens: Container, openIndex: Int`
  - **📤 Salida (Output / Retorno):** `Int`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `findMatchingClosingParenthesis` a partir de la secuencia de tokens.
- **`findMatchingBrace`**
  - **Firma:** `private fun findMatchingBrace(tokens: Container, openBraceIndex: Int): Int`
  - **📥 Entradas (Inputs):** `tokens: Container, openBraceIndex: Int`
  - **📤 Salida (Output / Retorno):** `Int`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `findMatchingBrace` a partir de la secuencia de tokens.
- **`arithParse`**
  - **Firma:** `fun arithParse(tokens: Container): ASTNode`
  - **📥 Entradas (Inputs):** `tokens: Container`
  - **📤 Salida (Output / Retorno):** `ASTNode`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `arithParse` a partir de la secuencia de tokens.
- **`processTokens`**
  - **Firma:** `fun processTokens(symbols: List<PrattToken>): List<PrattToken>`
  - **📥 Entradas (Inputs):** `symbols: List<PrattToken>`
  - **📤 Salida (Output / Retorno):** `List<PrattToken>`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `processTokens` a partir de la secuencia de tokens.
- **`associateOperation`**
  - **Firma:** `fun associateOperation(symbols: List<PrattToken>, operatorIndex: Int): List<PrattToken>`
  - **📥 Entradas (Inputs):** `symbols: List<PrattToken>, operatorIndex: Int`
  - **📤 Salida (Output / Retorno):** `List<PrattToken>`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `associateOperation` a partir de la secuencia de tokens.
- **`prattify`**
  - **Firma:** `fun prattify(tokens: Container): List<PrattToken>`
  - **📥 Entradas (Inputs):** `tokens: Container`
  - **📤 Salida (Output / Retorno):** `List<PrattToken>`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `prattify` a partir de la secuencia de tokens.
- **`highestPrecedIndex`**
  - **Firma:** `fun highestPrecedIndex(symbols: List<PrattToken>): Int`
  - **📥 Entradas (Inputs):** `symbols: List<PrattToken>`
  - **📤 Salida (Output / Retorno):** `Int`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `highestPrecedIndex` a partir de la secuencia de tokens.
- **`prattToAST`**
  - **Firma:** `fun prattToAST(symbol: PrattToken): ASTNode`
  - **📥 Entradas (Inputs):** `symbol: PrattToken`
  - **📤 Salida (Output / Retorno):** `ASTNode`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `prattToAST` a partir de la secuencia de tokens.
- **`getPrecedence`**
  - **Firma:** `private fun getPrecedence(token: Token): Int`
  - **📥 Entradas (Inputs):** `token: Token`
  - **📤 Salida (Output / Retorno):** `Int`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `getPrecedence` a partir de la secuencia de tokens.
- **`shuntingYard`**
  - **Firma:** `private fun shuntingYard(tokens: Container): ASTNode`
  - **📥 Entradas (Inputs):** `tokens: Container`
  - **📤 Salida (Output / Retorno):** `ASTNode`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `shuntingYard` a partir de la secuencia de tokens.


### 📄 Archivo: [PrattToken.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/parser/src/main/kotlin/PrattToken.kt)

- **Ruta:** `parser/src/main/kotlin/PrattToken.kt`
- **Package:** `parser.src.main.kotlin`
- **Líneas de Código:** `33`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`PrattToken`**
  - **Firma completa:** `class PrattToken(     val token: Token,     val precedence: Int,     val associativity: Association,     val children: List<PrattToken> = emptyList()`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`token`**
  - **Firma:** `fun token(): Token`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Token`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `token` a partir de la secuencia de tokens.
- **`associate`**
  - **Firma:** `fun associate(newChildren: List<PrattToken>): PrattToken`
  - **📥 Entradas (Inputs):** `newChildren: List<PrattToken>`
  - **📤 Salida (Output / Retorno):** `PrattToken`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `associate` a partir de la secuencia de tokens.
- **`precedence`**
  - **Firma:** `fun precedence(): Int`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Int`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `precedence` a partir de la secuencia de tokens.
- **`associativity`**
  - **Firma:** `fun associativity(): Association`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Association`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `associativity` a partir de la secuencia de tokens.
- **`children`**
  - **Firma:** `fun children(index: Int): PrattToken?`
  - **📥 Entradas (Inputs):** `index: Int`
  - **📤 Salida (Output / Retorno):** `PrattToken?`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `children` a partir de la secuencia de tokens.
- **`allChildren`**
  - **Firma:** `fun allChildren(): List<PrattToken>`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `List<PrattToken>`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `allChildren` a partir de la secuencia de tokens.


### 📄 Archivo: [PrattTokenFactory.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/parser/src/main/kotlin/PrattTokenFactory.kt)

- **Ruta:** `parser/src/main/kotlin/PrattTokenFactory.kt`
- **Package:** `parser.src.main.kotlin`
- **Líneas de Código:** `13`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`PrattTokenFactory`**
  - **Firma completa:** `class PrattTokenFactory(private val features: VersionFeatures)`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`createPrattToken`**
  - **Firma:** `fun createPrattToken(token: Token): PrattToken`
  - **📥 Entradas (Inputs):** `token: Token`
  - **📤 Salida (Output / Retorno):** `PrattToken`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `createPrattToken` a partir de la secuencia de tokens.


### 📄 Archivo: [VersionConfig.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/parser/src/main/kotlin/VersionConfig.kt)

- **Ruta:** `parser/src/main/kotlin/VersionConfig.kt`
- **Package:** `parser.src.main.kotlin`
- **Líneas de Código:** `54`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`VersionConfig`**
  - **Firma completa:** `class VersionConfig`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`getFeatures`**
  - **Firma:** `fun getFeatures(version: String): VersionFeatures`
  - **📥 Entradas (Inputs):** `version: String`
  - **📤 Salida (Output / Retorno):** `VersionFeatures`
  - **💡 ¿Qué hace esta función?:** Construye o valida la rama sintáctica del AST asociada a `getFeatures` a partir de la secuencia de tokens.


### 📄 Archivo: [VersionFeatures.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/parser/src/main/kotlin/VersionFeatures.kt)

- **Ruta:** `parser/src/main/kotlin/VersionFeatures.kt`
- **Package:** `parser.src.main.kotlin`
- **Líneas de Código:** `14`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`VersionFeatures`**
  - **Firma completa:** `data class VersionFeatures(     val keywords: Set<String>,     val types: Set<String>,     val functions: Set<String>,     val operators: Map<String, Int>, // operator -> precedence     val associations: Map<String, Association>,     val supportsConst: Boolean = false,     val supportsIfElse: Boolean = false,     val supportsBlocks: Boolean = false,     val supportsBooleans: Boolean = false )`



---

## 📦 Módulo `interpreter`

**Propósito del Módulo:** Motor de ejecución del AST. Evalúa expresiones, asignaciones, variables, bloques `if/else`, `println`, `readInput` y `readEnv`.

**Cantidad de Archivos:** `19` archivos de código fuente.

### 📄 Archivo: [Actions.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/interpreter/src/main/kotlin/Actions.kt)

- **Ruta:** `interpreter/src/main/kotlin/Actions.kt`
- **Package:** `interpreter.src.main.kotlin`
- **Líneas de Código:** `26`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Actions`**
  - **Firma completa:** `enum class Actions`


### 📄 Archivo: [ActionType.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/interpreter/src/main/kotlin/ActionType.kt)

- **Ruta:** `interpreter/src/main/kotlin/ActionType.kt`
- **Package:** `interpreter.src.main.kotlin`
- **Líneas de Código:** `8`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`ActionType`**
  - **Firma completa:** `interface ActionType`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`interpret`**
  - **Firma:** `fun interpret(node: ASTNode, interpreter: Interpreter): Any`
  - **📥 Entradas (Inputs):** `node: ASTNode, interpreter: Interpreter`
  - **📤 Salida (Output / Retorno):** `Any`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.


### 📄 Archivo: [Add.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/interpreter/src/main/kotlin/Add.kt)

- **Ruta:** `interpreter/src/main/kotlin/Add.kt`
- **Package:** `interpreter.src.main.kotlin`
- **Líneas de Código:** `21`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Add`**
  - **Firma completa:** `object Add : ActionType`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`interpret`**
  - **Firma:** `override fun interpret(node: ASTNode, interpreter: Interpreter): Any`
  - **📥 Entradas (Inputs):** `node: ASTNode, interpreter: Interpreter`
  - **📤 Salida (Output / Retorno):** `Any`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.


### 📄 Archivo: [AssignmentToExistingVar.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/interpreter/src/main/kotlin/AssignmentToExistingVar.kt)

- **Ruta:** `interpreter/src/main/kotlin/AssignmentToExistingVar.kt`
- **Package:** `interpreter.src.main.kotlin`
- **Líneas de Código:** `53`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`AssignmentToExistingVar`**
  - **Firma completa:** `object AssignmentToExistingVar : ActionType`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`interpret`**
  - **Firma:** `override fun interpret(node: ASTNode, interpreter: Interpreter): Any`
  - **📥 Entradas (Inputs):** `node: ASTNode, interpreter: Interpreter`
  - **📤 Salida (Output / Retorno):** `Any`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.
- **`coerceValue`**
  - **Firma:** `private fun coerceValue(value: Any?, expectedType: String): Any?`
  - **📥 Entradas (Inputs):** `value: Any?, expectedType: String`
  - **📤 Salida (Output / Retorno):** `Any?`
  - **💡 ¿Qué hace esta función?:** Ejecuta o evalúa la acción correspondiente a `coerceValue` actualizando las variables o estado del entorno.
- **`validateTypeCompatibility`**
  - **Firma:** `private fun validateTypeCompatibility(value: Any?, expectedType: String)`
  - **📥 Entradas (Inputs):** `value: Any?, expectedType: String`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Ejecuta o evalúa la acción correspondiente a `validateTypeCompatibility` actualizando las variables o estado del entorno.


### 📄 Archivo: [Block.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/interpreter/src/main/kotlin/Block.kt)

- **Ruta:** `interpreter/src/main/kotlin/Block.kt`
- **Package:** `interpreter.src.main.kotlin`
- **Líneas de Código:** `13`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Block`**
  - **Firma completa:** `class Block : ActionType`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`interpret`**
  - **Firma:** `override fun interpret(node: ASTNode, interpreter: Interpreter): Any`
  - **📥 Entradas (Inputs):** `node: ASTNode, interpreter: Interpreter`
  - **📤 Salida (Output / Retorno):** `Any`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.


### 📄 Archivo: [ConstDeclarationAndAssignment.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/interpreter/src/main/kotlin/ConstDeclarationAndAssignment.kt)

- **Ruta:** `interpreter/src/main/kotlin/ConstDeclarationAndAssignment.kt`
- **Package:** `interpreter.src.main.kotlin`
- **Líneas de Código:** `37`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`ConstDeclarationAndAssignment`**
  - **Firma completa:** `object ConstDeclarationAndAssignment : ActionType`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`interpret`**
  - **Firma:** `override fun interpret(node: ASTNode, interpreter: Interpreter): Any`
  - **📥 Entradas (Inputs):** `node: ASTNode, interpreter: Interpreter`
  - **📤 Salida (Output / Retorno):** `Any`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.
- **`validateTypeCompatibility`**
  - **Firma:** `private fun validateTypeCompatibility(value: Any?, expectedType: String)`
  - **📥 Entradas (Inputs):** `value: Any?, expectedType: String`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Ejecuta o evalúa la acción correspondiente a `validateTypeCompatibility` actualizando las variables o estado del entorno.


### 📄 Archivo: [Divide.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/interpreter/src/main/kotlin/Divide.kt)

- **Ruta:** `interpreter/src/main/kotlin/Divide.kt`
- **Package:** `interpreter.src.main.kotlin`
- **Líneas de Código:** `23`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Divide`**
  - **Firma completa:** `object Divide : ActionType`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`interpret`**
  - **Firma:** `override fun interpret(node: ASTNode, interpreter: Interpreter): Any`
  - **📥 Entradas (Inputs):** `node: ASTNode, interpreter: Interpreter`
  - **📤 Salida (Output / Retorno):** `Any`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.


### 📄 Archivo: [IfStatement.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/interpreter/src/main/kotlin/IfStatement.kt)

- **Ruta:** `interpreter/src/main/kotlin/IfStatement.kt`
- **Package:** `interpreter.src.main.kotlin`
- **Líneas de Código:** `25`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`IfStatement`**
  - **Firma completa:** `class IfStatement : ActionType`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`interpret`**
  - **Firma:** `override fun interpret(node: ASTNode, interpreter: Interpreter): Any`
  - **📥 Entradas (Inputs):** `node: ASTNode, interpreter: Interpreter`
  - **📤 Salida (Output / Retorno):** `Any`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.


### 📄 Archivo: [Interpreter.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/interpreter/src/main/kotlin/Interpreter.kt)

- **Ruta:** `interpreter/src/main/kotlin/Interpreter.kt`
- **Package:** `interpreter.src.main.kotlin`
- **Líneas de Código:** `199`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Interpreter`**
  - **Firma completa:** `class Interpreter(     private val version: String = "1.0",     private val inputProvider: InputProvider? = null,     val printer: (Any?)`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`enterScope`**
  - **Firma:** `fun enterScope()`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Ejecuta o evalúa la acción correspondiente a `enterScope` actualizando las variables o estado del entorno.
- **`exitScope`**
  - **Firma:** `fun exitScope()`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Ejecuta o evalúa la acción correspondiente a `exitScope` actualizando las variables o estado del entorno.
- **`declareVariable`**
  - **Firma:** `fun declareVariable(name: String, value: Any?, type: String)`
  - **📥 Entradas (Inputs):** `name: String, value: Any?, type: String`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Ejecuta o evalúa la acción correspondiente a `declareVariable` actualizando las variables o estado del entorno.
- **`assignVariable`**
  - **Firma:** `fun assignVariable(name: String, value: Any?)`
  - **📥 Entradas (Inputs):** `name: String, value: Any?`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Ejecuta o evalúa la acción correspondiente a `assignVariable` actualizando las variables o estado del entorno.
- **`resolveVariable`**
  - **Firma:** `fun resolveVariable(name: String): Any?`
  - **📥 Entradas (Inputs):** `name: String`
  - **📤 Salida (Output / Retorno):** `Any?`
  - **💡 ¿Qué hace esta función?:** Ejecuta o evalúa la acción correspondiente a `resolveVariable` actualizando las variables o estado del entorno.
- **`resolveVariableType`**
  - **Firma:** `fun resolveVariableType(name: String): String?`
  - **📥 Entradas (Inputs):** `name: String`
  - **📤 Salida (Output / Retorno):** `String?`
  - **💡 ¿Qué hace esta función?:** Ejecuta o evalúa la acción correspondiente a `resolveVariableType` actualizando las variables o estado del entorno.
- **`declareConstant`**
  - **Firma:** `fun declareConstant(name: String, value: Any?, type: String)`
  - **📥 Entradas (Inputs):** `name: String, value: Any?, type: String`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Ejecuta o evalúa la acción correspondiente a `declareConstant` actualizando las variables o estado del entorno.
- **`interpret`**
  - **Firma:** `fun interpret(node: ASTNode): Any?`
  - **📥 Entradas (Inputs):** `node: ASTNode`
  - **📤 Salida (Output / Retorno):** `Any?`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.
- **`determineAction`**
  - **Firma:** `fun determineAction(node: ASTNode): Actions`
  - **📥 Entradas (Inputs):** `node: ASTNode`
  - **📤 Salida (Output / Retorno):** `Actions`
  - **💡 ¿Qué hace esta función?:** Ejecuta o evalúa la acción correspondiente a `determineAction` actualizando las variables o estado del entorno.
- **`isActionSupportedInVersion`**
  - **Firma:** `private fun isActionSupportedInVersion(action: Actions, version: String): Boolean`
  - **📥 Entradas (Inputs):** `action: Actions, version: String`
  - **📤 Salida (Output / Retorno):** `Boolean`
  - **💡 ¿Qué hace esta función?:** Ejecuta o evalúa la acción correspondiente a `isActionSupportedInVersion` actualizando las variables o estado del entorno.
- **`executeAST`**
  - **Firma:** `fun executeAST(ast: ASTNode): List<String>`
  - **📥 Entradas (Inputs):** `ast: ASTNode`
  - **📤 Salida (Output / Retorno):** `List<String>`
  - **💡 ¿Qué hace esta función?:** Ejecuta o evalúa la acción correspondiente a `executeAST` actualizando las variables o estado del entorno.


### 📄 Archivo: [Literal.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/interpreter/src/main/kotlin/Literal.kt)

- **Ruta:** `interpreter/src/main/kotlin/Literal.kt`
- **Package:** `interpreter.src.main.kotlin`
- **Líneas de Código:** `22`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Literal`**
  - **Firma completa:** `object Literal : ActionType`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`interpret`**
  - **Firma:** `override fun interpret(node: ASTNode, interpreter: Interpreter): Any`
  - **📥 Entradas (Inputs):** `node: ASTNode, interpreter: Interpreter`
  - **📤 Salida (Output / Retorno):** `Any`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.


### 📄 Archivo: [Multiply.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/interpreter/src/main/kotlin/Multiply.kt)

- **Ruta:** `interpreter/src/main/kotlin/Multiply.kt`
- **Package:** `interpreter.src.main.kotlin`
- **Líneas de Código:** `19`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Multiply`**
  - **Firma completa:** `object Multiply : ActionType`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`interpret`**
  - **Firma:** `override fun interpret(node: ASTNode, interpreter: Interpreter): Any`
  - **📥 Entradas (Inputs):** `node: ASTNode, interpreter: Interpreter`
  - **📤 Salida (Output / Retorno):** `Any`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.


### 📄 Archivo: [Print.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/interpreter/src/main/kotlin/Print.kt)

- **Ruta:** `interpreter/src/main/kotlin/Print.kt`
- **Package:** `interpreter.src.main.kotlin`
- **Líneas de Código:** `11`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Print`**
  - **Firma completa:** `object Print : ActionType`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`interpret`**
  - **Firma:** `override fun interpret(node: ASTNode, interpreter: Interpreter): Any`
  - **📥 Entradas (Inputs):** `node: ASTNode, interpreter: Interpreter`
  - **📤 Salida (Output / Retorno):** `Any`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.


### 📄 Archivo: [ReadEnv.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/interpreter/src/main/kotlin/ReadEnv.kt)

- **Ruta:** `interpreter/src/main/kotlin/ReadEnv.kt`
- **Package:** `interpreter.src.main.kotlin`
- **Líneas de Código:** `17`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`ReadEnv`**
  - **Firma completa:** `class ReadEnv(private val inputProvider: InputProvider) : ActionType`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`interpret`**
  - **Firma:** `override fun interpret(node: ASTNode, interpreter: Interpreter): Any`
  - **📥 Entradas (Inputs):** `node: ASTNode, interpreter: Interpreter`
  - **📤 Salida (Output / Retorno):** `Any`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.


### 📄 Archivo: [ReadInput.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/interpreter/src/main/kotlin/ReadInput.kt)

- **Ruta:** `interpreter/src/main/kotlin/ReadInput.kt`
- **Package:** `interpreter.src.main.kotlin`
- **Líneas de Código:** `19`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`ReadInput`**
  - **Firma completa:** `class ReadInput(private val inputProvider: InputProvider) : ActionType`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`interpret`**
  - **Firma:** `override fun interpret(node: ASTNode, interpreter: Interpreter): Any`
  - **📥 Entradas (Inputs):** `node: ASTNode, interpreter: Interpreter`
  - **📤 Salida (Output / Retorno):** `Any`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.


### 📄 Archivo: [Subtract.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/interpreter/src/main/kotlin/Subtract.kt)

- **Ruta:** `interpreter/src/main/kotlin/Subtract.kt`
- **Package:** `interpreter.src.main.kotlin`
- **Líneas de Código:** `19`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Subtract`**
  - **Firma completa:** `object Subtract : ActionType`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`interpret`**
  - **Firma:** `override fun interpret(node: ASTNode, interpreter: Interpreter): Any`
  - **📥 Entradas (Inputs):** `node: ASTNode, interpreter: Interpreter`
  - **📤 Salida (Output / Retorno):** `Any`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.


### 📄 Archivo: [Util.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/interpreter/src/main/kotlin/Util.kt)

- **Ruta:** `interpreter/src/main/kotlin/Util.kt`
- **Package:** `interpreter.src.main.kotlin`
- **Líneas de Código:** `13`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.


### 📄 Archivo: [VarDeclaration.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/interpreter/src/main/kotlin/VarDeclaration.kt)

- **Ruta:** `interpreter/src/main/kotlin/VarDeclaration.kt`
- **Package:** `interpreter.src.main.kotlin`
- **Líneas de Código:** `23`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`VarDeclaration`**
  - **Firma completa:** `object VarDeclaration : ActionType`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`interpret`**
  - **Firma:** `override fun interpret(node: ASTNode, interpreter: Interpreter): Any`
  - **📥 Entradas (Inputs):** `node: ASTNode, interpreter: Interpreter`
  - **📤 Salida (Output / Retorno):** `Any`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.
- **`defaultValueForType`**
  - **Firma:** `private fun defaultValueForType(type: String): Any?`
  - **📥 Entradas (Inputs):** `type: String`
  - **📤 Salida (Output / Retorno):** `Any?`
  - **💡 ¿Qué hace esta función?:** Ejecuta o evalúa la acción correspondiente a `defaultValueForType` actualizando las variables o estado del entorno.


### 📄 Archivo: [VarDeclarationAndAssignment.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/interpreter/src/main/kotlin/VarDeclarationAndAssignment.kt)

- **Ruta:** `interpreter/src/main/kotlin/VarDeclarationAndAssignment.kt`
- **Package:** `interpreter.src.main.kotlin`
- **Líneas de Código:** `63`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`VarDeclarationAndAssignment`**
  - **Firma completa:** `object VarDeclarationAndAssignment : ActionType`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`interpret`**
  - **Firma:** `override fun interpret(node: ASTNode, interpreter: Interpreter): Any`
  - **📥 Entradas (Inputs):** `node: ASTNode, interpreter: Interpreter`
  - **📤 Salida (Output / Retorno):** `Any`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.
- **`coerceValue`**
  - **Firma:** `private fun coerceValue(value: Any?, expectedType: String): Any?`
  - **📥 Entradas (Inputs):** `value: Any?, expectedType: String`
  - **📤 Salida (Output / Retorno):** `Any?`
  - **💡 ¿Qué hace esta función?:** Ejecuta o evalúa la acción correspondiente a `coerceValue` actualizando las variables o estado del entorno.
- **`validateTypeCompatibility`**
  - **Firma:** `private fun validateTypeCompatibility(value: Any?, expectedType: String)`
  - **📥 Entradas (Inputs):** `value: Any?, expectedType: String`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Ejecuta o evalúa la acción correspondiente a `validateTypeCompatibility` actualizando las variables o estado del entorno.


### 📄 Archivo: [VarDeclarationOnly.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/interpreter/src/main/kotlin/VarDeclarationOnly.kt)

- **Ruta:** `interpreter/src/main/kotlin/VarDeclarationOnly.kt`
- **Package:** `interpreter.src.main.kotlin`
- **Líneas de Código:** `34`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`VarDeclarationOnly`**
  - **Firma completa:** `object VarDeclarationOnly : ActionType`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`interpret`**
  - **Firma:** `override fun interpret(node: ASTNode, interpreter: Interpreter): Any`
  - **📥 Entradas (Inputs):** `node: ASTNode, interpreter: Interpreter`
  - **📤 Salida (Output / Retorno):** `Any`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.



---

## 📦 Módulo `linter`

**Propósito del Módulo:** Analizador estático de código que evalúa el AST y tokens frente a reglas configurables (convención de nombres, `const`, etc.).

**Cantidad de Archivos:** `15` archivos de código fuente.

### 📄 Archivo: [Linter.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/linter/src/main/kotlin/Linter.kt)

- **Ruta:** `linter/src/main/kotlin/Linter.kt`
- **Package:** `linter.src.main.kotlin`
- **Líneas de Código:** `24`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Linter`**
  - **Firma completa:** `class Linter(private val rules: List<LintRule>)`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`lint`**
  - **Firma:** `fun lint(asts: List<ASTNode>): List<LintError>`
  - **📥 Entradas (Inputs):** `asts: List<ASTNode>`
  - **📤 Salida (Output / Retorno):** `List<LintError>`
  - **💡 ¿Qué hace esta función?:** Recorre los nodos del AST verificando cada regla de linting habilitada y retorna la lista de errores o advertencias encontradas.
- **`all`**
  - **Firma:** `fun all(root: ASTNode): List<LintError>`
  - **📥 Entradas (Inputs):** `root: ASTNode`
  - **📤 Salida (Output / Retorno):** `List<LintError>`
  - **💡 ¿Qué hace esta función?:** Ejecuta la regla de validación estática de `all` notificando cualquier infracción al estándar de código.
- **`lintingPassed`**
  - **Firma:** `fun lintingPassed(asts: List<ASTNode>): Boolean`
  - **📥 Entradas (Inputs):** `asts: List<ASTNode>`
  - **📤 Salida (Output / Retorno):** `Boolean`
  - **💡 ¿Qué hace esta función?:** Ejecuta la regla de validación estática de `lintingPassed` notificando cualquier infracción al estándar de código.


### 📄 Archivo: [LintError.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/linter/src/main/kotlin/LintError.kt)

- **Ruta:** `linter/src/main/kotlin/LintError.kt`
- **Package:** `linter.src.main.kotlin`
- **Líneas de Código:** `10`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`LintError`**
  - **Firma completa:** `data class LintError(     val message: String,     val position: Position )`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`toString`**
  - **Firma:** `override fun toString(): String`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `String`
  - **💡 ¿Qué hace esta función?:** Devuelve la representación en formato de texto (String) de esta estructura para depuración e impresión.


### 📄 Archivo: [ConfigFactory.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/linter/src/main/kotlin/config/ConfigFactory.kt)

- **Ruta:** `linter/src/main/kotlin/config/ConfigFactory.kt`
- **Package:** `linter.src.main.kotlin.config`
- **Líneas de Código:** `31`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`ConfigFactory`**
  - **Firma completa:** `class ConfigFactory`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`createConfig`**
  - **Firma:** `fun createConfig(yamlMap: Map<String, Any>): LinterConfig`
  - **📥 Entradas (Inputs):** `yamlMap: Map<String, Any>`
  - **📤 Salida (Output / Retorno):** `LinterConfig`
  - **💡 ¿Qué hace esta función?:** Ejecuta la regla de validación estática de `createConfig` notificando cualquier infracción al estándar de código.


### 📄 Archivo: [ConfigLoader.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/linter/src/main/kotlin/config/ConfigLoader.kt)

- **Ruta:** `linter/src/main/kotlin/config/ConfigLoader.kt`
- **Package:** `linter.src.main.kotlin.config`
- **Líneas de Código:** `29`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`ConfigLoader`**
  - **Firma completa:** `class ConfigLoader`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`loadYaml`**
  - **Firma:** `fun loadYaml(path: String): Map<String, Any>`
  - **📥 Entradas (Inputs):** `path: String`
  - **📤 Salida (Output / Retorno):** `Map<String, Any>`
  - **💡 ¿Qué hace esta función?:** Ejecuta la regla de validación estática de `loadYaml` notificando cualquier infracción al estándar de código.


### 📄 Archivo: [IdentifierNamingConfig.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/linter/src/main/kotlin/config/IdentifierNamingConfig.kt)

- **Ruta:** `linter/src/main/kotlin/config/IdentifierNamingConfig.kt`
- **Package:** `linter.src.main.kotlin.config`
- **Líneas de Código:** `7`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`IdentifierNamingConfig`**
  - **Firma completa:** `data class IdentifierNamingConfig(val style: String = "camelCase")`


### 📄 Archivo: [LinterConfig.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/linter/src/main/kotlin/config/LinterConfig.kt)

- **Ruta:** `linter/src/main/kotlin/config/LinterConfig.kt`
- **Package:** `linter.src.main.kotlin.config`
- **Líneas de Código:** `20`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`LinterConfig`**
  - **Firma completa:** `data class LinterConfig(     val rules: RulesConfig )`


### 📄 Archivo: [PrintLnConfig.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/linter/src/main/kotlin/config/PrintLnConfig.kt)

- **Ruta:** `linter/src/main/kotlin/config/PrintLnConfig.kt`
- **Package:** `linter.src.main.kotlin.config`
- **Líneas de Código:** `4`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`PrintLnConfig`**
  - **Firma completa:** `data class PrintLnConfig(val enabled: Boolean = true)`


### 📄 Archivo: [ReadInputConfig.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/linter/src/main/kotlin/config/ReadInputConfig.kt)

- **Ruta:** `linter/src/main/kotlin/config/ReadInputConfig.kt`
- **Package:** `linter.src.main.kotlin.config`
- **Líneas de Código:** `5`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`ReadInputConfig`**
  - **Firma completa:** `data class ReadInputConfig(     val enabled: Boolean )`


### 📄 Archivo: [RulesConfig.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/linter/src/main/kotlin/config/RulesConfig.kt)

- **Ruta:** `linter/src/main/kotlin/config/RulesConfig.kt`
- **Package:** `linter.src.main.kotlin.config`
- **Líneas de Código:** `7`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`RulesConfig`**
  - **Firma completa:** `data class RulesConfig(     val identifier_format: IdentifierNamingConfig? = null,     val mandatory_variable_or_literal_in_println: PrintLnConfig? = null,     val mandatory_variable_or_literal_in_readInput: ReadInputConfig? = null )`


### 📄 Archivo: [IdentifierNamingRule.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/linter/src/main/kotlin/rules/IdentifierNamingRule.kt)

- **Ruta:** `linter/src/main/kotlin/rules/IdentifierNamingRule.kt`
- **Package:** `linter.src.main.kotlin.rules`
- **Líneas de Código:** `36`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`IdentifierNamingRule`**
  - **Firma completa:** `class IdentifierNamingRule`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`apply`**
  - **Firma:** `override fun apply(root: ASTNode): List<LintError>`
  - **📥 Entradas (Inputs):** `root: ASTNode`
  - **📤 Salida (Output / Retorno):** `List<LintError>`
  - **💡 ¿Qué hace esta función?:** Ejecuta la regla de validación estática de `apply` notificando cualquier infracción al estándar de código.
- **`checkNode`**
  - **Firma:** `private fun checkNode(node: ASTNode): List<LintError>`
  - **📥 Entradas (Inputs):** `node: ASTNode`
  - **📤 Salida (Output / Retorno):** `List<LintError>`
  - **💡 ¿Qué hace esta función?:** Ejecuta la regla de validación estática de `checkNode` notificando cualquier infracción al estándar de código.


### 📄 Archivo: [IfWithoutElseRule.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/linter/src/main/kotlin/rules/IfWithoutElseRule.kt)

- **Ruta:** `linter/src/main/kotlin/rules/IfWithoutElseRule.kt`
- **Package:** `linter.src.main.kotlin.rules`
- **Líneas de Código:** `40`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`IfWithoutElseRule`**
  - **Firma completa:** `class IfWithoutElseRule : LintRule`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`apply`**
  - **Firma:** `override fun apply(root: ASTNode): List<LintError>`
  - **📥 Entradas (Inputs):** `root: ASTNode`
  - **📤 Salida (Output / Retorno):** `List<LintError>`
  - **💡 ¿Qué hace esta función?:** Ejecuta la regla de validación estática de `apply` notificando cualquier infracción al estándar de código.
- **`isControlFlow`**
  - **Firma:** `fun isControlFlow(node: ASTNode): Boolean`
  - **📥 Entradas (Inputs):** `node: ASTNode`
  - **📤 Salida (Output / Retorno):** `Boolean`
  - **💡 ¿Qué hace esta función?:** Ejecuta la regla de validación estática de `isControlFlow` notificando cualquier infracción al estándar de código.
- **`traverse`**
  - **Firma:** `fun traverse(node: ASTNode)`
  - **📥 Entradas (Inputs):** `node: ASTNode`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Ejecuta la regla de validación estática de `traverse` notificando cualquier infracción al estándar de código.


### 📄 Archivo: [ImmutableValRule.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/linter/src/main/kotlin/rules/ImmutableValRule.kt)

- **Ruta:** `linter/src/main/kotlin/rules/ImmutableValRule.kt`
- **Package:** `linter.src.main.kotlin.rules`
- **Líneas de Código:** `49`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`ImmutableValRule`**
  - **Firma completa:** `class ImmutableValRule : LintRule`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`apply`**
  - **Firma:** `override fun apply(root: ASTNode): List<LintError>`
  - **📥 Entradas (Inputs):** `root: ASTNode`
  - **📤 Salida (Output / Retorno):** `List<LintError>`
  - **💡 ¿Qué hace esta función?:** Ejecuta la regla de validación estática de `apply` notificando cualquier infracción al estándar de código.
- **`traverse`**
  - **Firma:** `fun traverse(node: ASTNode)`
  - **📥 Entradas (Inputs):** `node: ASTNode`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Ejecuta la regla de validación estática de `traverse` notificando cualquier infracción al estándar de código.


### 📄 Archivo: [LintRule.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/linter/src/main/kotlin/rules/LintRule.kt)

- **Ruta:** `linter/src/main/kotlin/rules/LintRule.kt`
- **Package:** `linter.src.main.kotlin.rules`
- **Líneas de Código:** `8`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`LintRule`**
  - **Firma completa:** `interface LintRule`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`apply`**
  - **Firma:** `fun apply(root: ASTNode): List<LintError>`
  - **📥 Entradas (Inputs):** `root: ASTNode`
  - **📤 Salida (Output / Retorno):** `List<LintError>`
  - **💡 ¿Qué hace esta función?:** Ejecuta la regla de validación estática de `apply` notificando cualquier infracción al estándar de código.


### 📄 Archivo: [PrintLnRule.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/linter/src/main/kotlin/rules/PrintLnRule.kt)

- **Ruta:** `linter/src/main/kotlin/rules/PrintLnRule.kt`
- **Package:** `linter.src.main.kotlin.rules`
- **Líneas de Código:** `38`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`PrintLnRule`**
  - **Firma completa:** `class PrintLnRule`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`apply`**
  - **Firma:** `override fun apply(root: ASTNode): List<LintError>`
  - **📥 Entradas (Inputs):** `root: ASTNode`
  - **📤 Salida (Output / Retorno):** `List<LintError>`
  - **💡 ¿Qué hace esta función?:** Ejecuta la regla de validación estática de `apply` notificando cualquier infracción al estándar de código.
- **`checkNode`**
  - **Firma:** `private fun checkNode(node: ASTNode): List<LintError>`
  - **📥 Entradas (Inputs):** `node: ASTNode`
  - **📤 Salida (Output / Retorno):** `List<LintError>`
  - **💡 ¿Qué hace esta función?:** Ejecuta la regla de validación estática de `checkNode` notificando cualquier infracción al estándar de código.


### 📄 Archivo: [ReadInputRule.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/linter/src/main/kotlin/rules/ReadInputRule.kt)

- **Ruta:** `linter/src/main/kotlin/rules/ReadInputRule.kt`
- **Package:** `linter.src.main.kotlin.rules`
- **Líneas de Código:** `38`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`ReadInputRule`**
  - **Firma completa:** `class ReadInputRule`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`apply`**
  - **Firma:** `override fun apply(root: ASTNode): List<LintError>`
  - **📥 Entradas (Inputs):** `root: ASTNode`
  - **📤 Salida (Output / Retorno):** `List<LintError>`
  - **💡 ¿Qué hace esta función?:** Ejecuta la regla de validación estática de `apply` notificando cualquier infracción al estándar de código.
- **`checkNode`**
  - **Firma:** `private fun checkNode(node: ASTNode): List<LintError>`
  - **📥 Entradas (Inputs):** `node: ASTNode`
  - **📤 Salida (Output / Retorno):** `List<LintError>`
  - **💡 ¿Qué hace esta función?:** Ejecuta la regla de validación estática de `checkNode` notificando cualquier infracción al estándar de código.



---

## 📦 Módulo `formatter`

**Propósito del Módulo:** Formateador de código fuente que aplica reglas de estilo (espaciado, saltos de línea e indentación).

**Cantidad de Archivos:** `16` archivos de código fuente.

### 📄 Archivo: [ConfigLoader.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/formatter/src/main/kotlin/ConfigLoader.kt)

- **Ruta:** `formatter/src/main/kotlin/ConfigLoader.kt`
- **Package:** `formatter.src.main.kotlin`
- **Líneas de Código:** `92`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`ConfigLoader`**
  - **Firma completa:** `class ConfigLoader(private val configFile: String)`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`loadConfig`**
  - **Firma:** `fun loadConfig(version: String = "1.0"): List<FormatRule>`
  - **📥 Entradas (Inputs):** `version: String = "1.0"`
  - **📤 Salida (Output / Retorno):** `List<FormatRule>`
  - **💡 ¿Qué hace esta función?:** Aplica la regla de transformación de estilo de `loadConfig` sobre el flujo de tokens o texto de salida.
- **`extractMandatoryRulesFromConfig`**
  - **Firma:** `internal fun extractMandatoryRulesFromConfig(config: Map<String, Any>): List<FormatRule>`
  - **📥 Entradas (Inputs):** `config: Map<String, Any>`
  - **📤 Salida (Output / Retorno):** `List<FormatRule>`
  - **💡 ¿Qué hace esta función?:** Aplica la regla de transformación de estilo de `extractMandatoryRulesFromConfig` sobre el flujo de tokens o texto de salida.
- **`createConfigurableRules`**
  - **Firma:** `internal fun createConfigurableRules(config: Map<String, Any>): List<FormatRule>`
  - **📥 Entradas (Inputs):** `config: Map<String, Any>`
  - **📤 Salida (Output / Retorno):** `List<FormatRule>`
  - **💡 ¿Qué hace esta función?:** Aplica la regla de transformación de estilo de `createConfigurableRules` sobre el flujo de tokens o texto de salida.
- **`readConfig`**
  - **Firma:** `internal fun readConfig(): Map<String, Any>`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Map<String, Any>`
  - **💡 ¿Qué hace esta función?:** Aplica la regla de transformación de estilo de `readConfig` sobre el flujo de tokens o texto de salida.


### 📄 Archivo: [Formatter.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/formatter/src/main/kotlin/Formatter.kt)

- **Ruta:** `formatter/src/main/kotlin/Formatter.kt`
- **Package:** `formatter.src.main.kotlin`
- **Líneas de Código:** `26`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Formatter`**
  - **Firma completa:** `class Formatter()`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`loadRules`**
  - **Firma:** `fun loadRules(configFilePath: String): List<FormatRule>`
  - **📥 Entradas (Inputs):** `configFilePath: String`
  - **📤 Salida (Output / Retorno):** `List<FormatRule>`
  - **💡 ¿Qué hace esta función?:** Aplica la regla de transformación de estilo de `loadRules` sobre el flujo de tokens o texto de salida.
- **`execute`**
  - **Firma:** `fun execute(statements: List<Container>, configFile: File): List<Container>`
  - **📥 Entradas (Inputs):** `statements: List<Container>, configFile: File`
  - **📤 Salida (Output / Retorno):** `List<Container>`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.


### 📄 Archivo: [FormatRule.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/formatter/src/main/kotlin/formatrule/FormatRule.kt)

- **Ruta:** `formatter/src/main/kotlin/formatrule/FormatRule.kt`
- **Package:** `formatter.src.main.kotlin.formatrule`
- **Líneas de Código:** `7`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`FormatRule`**
  - **Firma completa:** `interface FormatRule`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`format`**
  - **Firma:** `fun format(statements: List<Container>): List<Container>`
  - **📥 Entradas (Inputs):** `statements: List<Container>`
  - **📤 Salida (Output / Retorno):** `List<Container>`
  - **💡 ¿Qué hace esta función?:** Transforma la secuencia de tokens aplicando las reglas de espaciado, saltos de línea e indentación configuradas.


### 📄 Archivo: [LineBreakAfterSemicolonRule.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/formatter/src/main/kotlin/formatrule/mandatory/LineBreakAfterSemicolonRule.kt)

- **Ruta:** `formatter/src/main/kotlin/formatrule/mandatory/LineBreakAfterSemicolonRule.kt`
- **Package:** `formatter.src.main.kotlin.formatrule.mandatory`
- **Líneas de Código:** `66`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`LineBreakAfterSemicolonRule`**
  - **Firma completa:** `class LineBreakAfterSemicolonRule(private val enabled: Boolean = true) : FormatRule`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`format`**
  - **Firma:** `override fun format(statements: List<Container>): List<Container>`
  - **📥 Entradas (Inputs):** `statements: List<Container>`
  - **📤 Salida (Output / Retorno):** `List<Container>`
  - **💡 ¿Qué hace esta función?:** Transforma la secuencia de tokens aplicando las reglas de espaciado, saltos de línea e indentación configuradas.


### 📄 Archivo: [SpaceAroundOperatorRule.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/formatter/src/main/kotlin/formatrule/mandatory/SpaceAroundOperatorRule.kt)

- **Ruta:** `formatter/src/main/kotlin/formatrule/mandatory/SpaceAroundOperatorRule.kt`
- **Package:** `formatter.src.main.kotlin.formatrule.mandatory`
- **Líneas de Código:** `54`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`SpaceAroundOperatorRule`**
  - **Firma completa:** `class SpaceAroundOperatorRule : FormatRule`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`format`**
  - **Firma:** `override fun format(statements: List<Container>): List<Container>`
  - **📥 Entradas (Inputs):** `statements: List<Container>`
  - **📤 Salida (Output / Retorno):** `List<Container>`
  - **💡 ¿Qué hace esta función?:** Transforma la secuencia de tokens aplicando las reglas de espaciado, saltos de línea e indentación configuradas.


### 📄 Archivo: [SpaceBetweenTokensRule.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/formatter/src/main/kotlin/formatrule/mandatory/SpaceBetweenTokensRule.kt)

- **Ruta:** `formatter/src/main/kotlin/formatrule/mandatory/SpaceBetweenTokensRule.kt`
- **Package:** `formatter.src.main.kotlin.formatrule.mandatory`
- **Líneas de Código:** `54`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`SpaceBetweenTokensRule`**
  - **Firma completa:** `class SpaceBetweenTokensRule(val enabled: Boolean = true) : FormatRule`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`format`**
  - **Firma:** `override fun format(statements: List<Container>): List<Container>`
  - **📥 Entradas (Inputs):** `statements: List<Container>`
  - **📤 Salida (Output / Retorno):** `List<Container>`
  - **💡 ¿Qué hace esta función?:** Transforma la secuencia de tokens aplicando las reglas de espaciado, saltos de línea e indentación configuradas.


### 📄 Archivo: [AssignSpacingRule.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/formatter/src/main/kotlin/formatrule/optional/AssignSpacingRule.kt)

- **Ruta:** `formatter/src/main/kotlin/formatrule/optional/AssignSpacingRule.kt`
- **Package:** `formatter.src.main.kotlin.formatrule.optional`
- **Líneas de Código:** `69`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`AssignSpacingRule`**
  - **Firma completa:** `class AssignSpacingRule(     private val spaceBefore: Boolean = true,     private val spaceAfter: Boolean = true ) : FormatRule`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`format`**
  - **Firma:** `override fun format(statements: List<Container>): List<Container>`
  - **📥 Entradas (Inputs):** `statements: List<Container>`
  - **📤 Salida (Output / Retorno):** `List<Container>`
  - **💡 ¿Qué hace esta función?:** Transforma la secuencia de tokens aplicando las reglas de espaciado, saltos de línea e indentación configuradas.


### 📄 Archivo: [IfBraceBelowLineRule.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/formatter/src/main/kotlin/formatrule/optional/IfBraceBelowLineRule.kt)

- **Ruta:** `formatter/src/main/kotlin/formatrule/optional/IfBraceBelowLineRule.kt`
- **Package:** `formatter.src.main.kotlin.formatrule.optional`
- **Líneas de Código:** `60`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`IfBraceBelowLineRule`**
  - **Firma completa:** `class IfBraceBelowLineRule : FormatRule`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`format`**
  - **Firma:** `override fun format(statements: List<Container>): List<Container>`
  - **📥 Entradas (Inputs):** `statements: List<Container>`
  - **📤 Salida (Output / Retorno):** `List<Container>`
  - **💡 ¿Qué hace esta función?:** Transforma la secuencia de tokens aplicando las reglas de espaciado, saltos de línea e indentación configuradas.


### 📄 Archivo: [IfBraceOnSameLineRule.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/formatter/src/main/kotlin/formatrule/optional/IfBraceOnSameLineRule.kt)

- **Ruta:** `formatter/src/main/kotlin/formatrule/optional/IfBraceOnSameLineRule.kt`
- **Package:** `formatter.src.main.kotlin.formatrule.optional`
- **Líneas de Código:** `64`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`IfBraceOnSameLineRule`**
  - **Firma completa:** `class IfBraceOnSameLineRule : FormatRule`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`format`**
  - **Firma:** `override fun format(statements: List<Container>): List<Container>`
  - **📥 Entradas (Inputs):** `statements: List<Container>`
  - **📤 Salida (Output / Retorno):** `List<Container>`
  - **💡 ¿Qué hace esta función?:** Transforma la secuencia de tokens aplicando las reglas de espaciado, saltos de línea e indentación configuradas.


### 📄 Archivo: [IndentationRule.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/formatter/src/main/kotlin/formatrule/optional/IndentationRule.kt)

- **Ruta:** `formatter/src/main/kotlin/formatrule/optional/IndentationRule.kt`
- **Package:** `formatter.src.main.kotlin.formatrule.optional`
- **Líneas de Código:** `48`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`IndentationRule`**
  - **Firma completa:** `class IndentationRule(private val indentSize: Int) : FormatRule`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`format`**
  - **Firma:** `override fun format(statements: List<Container>): List<Container>`
  - **📥 Entradas (Inputs):** `statements: List<Container>`
  - **📤 Salida (Output / Retorno):** `List<Container>`
  - **💡 ¿Qué hace esta función?:** Transforma la secuencia de tokens aplicando las reglas de espaciado, saltos de línea e indentación configuradas.


### 📄 Archivo: [LineBreakAfterPrintRule.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/formatter/src/main/kotlin/formatrule/optional/LineBreakAfterPrintRule.kt)

- **Ruta:** `formatter/src/main/kotlin/formatrule/optional/LineBreakAfterPrintRule.kt`
- **Package:** `formatter.src.main.kotlin.formatrule.optional`
- **Líneas de Código:** `63`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`LineBreakAfterPrintRule`**
  - **Firma completa:** `class LineBreakAfterPrintRule(private val lineBreaks: Int) : FormatRule`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`format`**
  - **Firma:** `override fun format(statements: List<Container>): List<Container>`
  - **📥 Entradas (Inputs):** `statements: List<Container>`
  - **📤 Salida (Output / Retorno):** `List<Container>`
  - **💡 ¿Qué hace esta función?:** Transforma la secuencia de tokens aplicando las reglas de espaciado, saltos de línea e indentación configuradas.


### 📄 Archivo: [LineBreakBeforePrintRule.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/formatter/src/main/kotlin/formatrule/optional/LineBreakBeforePrintRule.kt)

- **Ruta:** `formatter/src/main/kotlin/formatrule/optional/LineBreakBeforePrintRule.kt`
- **Package:** `formatter.src.main.kotlin.formatrule.optional`
- **Líneas de Código:** `36`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`LineBreakBeforePrintRule`**
  - **Firma completa:** `class LineBreakBeforePrintRule(private val count: Int = 1) : FormatRule`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`format`**
  - **Firma:** `override fun format(statements: List<Container>): List<Container>`
  - **📥 Entradas (Inputs):** `statements: List<Container>`
  - **📤 Salida (Output / Retorno):** `List<Container>`
  - **💡 ¿Qué hace esta función?:** Transforma la secuencia de tokens aplicando las reglas de espaciado, saltos de línea e indentación configuradas.


### 📄 Archivo: [NoSpaceAfterColonRule.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/formatter/src/main/kotlin/formatrule/optional/NoSpaceAfterColonRule.kt)

- **Ruta:** `formatter/src/main/kotlin/formatrule/optional/NoSpaceAfterColonRule.kt`
- **Package:** `formatter.src.main.kotlin.formatrule.optional`
- **Líneas de Código:** `38`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`NoSpaceAfterColonRule`**
  - **Firma completa:** `class NoSpaceAfterColonRule : FormatRule`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`format`**
  - **Firma:** `override fun format(statements: List<Container>): List<Container>`
  - **📥 Entradas (Inputs):** `statements: List<Container>`
  - **📤 Salida (Output / Retorno):** `List<Container>`
  - **💡 ¿Qué hace esta función?:** Transforma la secuencia de tokens aplicando las reglas de espaciado, saltos de línea e indentación configuradas.


### 📄 Archivo: [NoSpaceBeforeColonRule.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/formatter/src/main/kotlin/formatrule/optional/NoSpaceBeforeColonRule.kt)

- **Ruta:** `formatter/src/main/kotlin/formatrule/optional/NoSpaceBeforeColonRule.kt`
- **Package:** `formatter.src.main.kotlin.formatrule.optional`
- **Líneas de Código:** `41`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`NoSpaceBeforeColonRule`**
  - **Firma completa:** `class NoSpaceBeforeColonRule : FormatRule`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`format`**
  - **Firma:** `override fun format(statements: List<Container>): List<Container>`
  - **📥 Entradas (Inputs):** `statements: List<Container>`
  - **📤 Salida (Output / Retorno):** `List<Container>`
  - **💡 ¿Qué hace esta función?:** Transforma la secuencia de tokens aplicando las reglas de espaciado, saltos de línea e indentación configuradas.


### 📄 Archivo: [SpaceAfterColonRule.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/formatter/src/main/kotlin/formatrule/optional/SpaceAfterColonRule.kt)

- **Ruta:** `formatter/src/main/kotlin/formatrule/optional/SpaceAfterColonRule.kt`
- **Package:** `formatter.src.main.kotlin.formatrule.optional`
- **Líneas de Código:** `43`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`SpaceAfterColonRule`**
  - **Firma completa:** `class SpaceAfterColonRule : FormatRule`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`format`**
  - **Firma:** `override fun format(statements: List<Container>): List<Container>`
  - **📥 Entradas (Inputs):** `statements: List<Container>`
  - **📤 Salida (Output / Retorno):** `List<Container>`
  - **💡 ¿Qué hace esta función?:** Transforma la secuencia de tokens aplicando las reglas de espaciado, saltos de línea e indentación configuradas.


### 📄 Archivo: [SpaceBeforeColonRule.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/formatter/src/main/kotlin/formatrule/optional/SpaceBeforeColonRule.kt)

- **Ruta:** `formatter/src/main/kotlin/formatrule/optional/SpaceBeforeColonRule.kt`
- **Package:** `formatter.src.main.kotlin.formatrule.optional`
- **Líneas de Código:** `45`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`SpaceBeforeColonRule`**
  - **Firma completa:** `class SpaceBeforeColonRule : FormatRule`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`format`**
  - **Firma:** `override fun format(statements: List<Container>): List<Container>`
  - **📥 Entradas (Inputs):** `statements: List<Container>`
  - **📤 Salida (Output / Retorno):** `List<Container>`
  - **💡 ¿Qué hace esta función?:** Transforma la secuencia de tokens aplicando las reglas de espaciado, saltos de línea e indentación configuradas.



---

## 📦 Módulo `formatteraction`

**Propósito del Módulo:** Acción que orquesta la tokenización (Lexer) y el formateo (Formatter) de un archivo.

**Cantidad de Archivos:** `1` archivos de código fuente.

### 📄 Archivo: [FormatterAction.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/formatteraction/src/main/kotlin/FormatterAction.kt)

- **Ruta:** `formatteraction/src/main/kotlin/FormatterAction.kt`
- **Package:** `formatteraction.src.main.kotlin`
- **Líneas de Código:** `107`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`FormatterAction`**
  - **Firma completa:** `class FormatterAction`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`execute`**
  - **Firma:** `fun execute(args: List<String>)`
  - **📥 Entradas (Inputs):** `args: List<String>`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.



---

## 📦 Módulo `analyzer`

**Propósito del Módulo:** Orquesta el análisis estático completo (Lexer + Parser + Linter) notificando avances y errores.

**Cantidad de Archivos:** `1` archivos de código fuente.

### 📄 Archivo: [Analyzer.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/analyzer/src/main/kotlin/Analyzer.kt)

- **Ruta:** `analyzer/src/main/kotlin/Analyzer.kt`
- **Package:** `analyzer.src.main.kotlin`
- **Líneas de Código:** `177`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Analyzer`**
  - **Firma completa:** `class Analyzer`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`executeValidation`**
  - **Firma:** `fun executeValidation(args: List<String>)`
  - **📥 Entradas (Inputs):** `args: List<String>`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `executeValidation`, operando sobre los parámetros especificados (`args: List<String>`) y devolviendo un resultado de tipo `Unit`.
- **`execute`**
  - **Firma:** `fun execute(args: List<String>)`
  - **📥 Entradas (Inputs):** `args: List<String>`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.
- **`executeAnalysis`**
  - **Firma:** `private fun executeAnalysis(args: List<String>, includeLinting: Boolean)`
  - **📥 Entradas (Inputs):** `args: List<String>, includeLinting: Boolean`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `executeAnalysis`, operando sobre los parámetros especificados (`args: List<String>, includeLinting: Boolean`) y devolviendo un resultado de tipo `Unit`.
- **`loadLintRules`**
  - **Firma:** `private fun loadLintRules(configFile: String): List<LintRule>`
  - **📥 Entradas (Inputs):** `configFile: String`
  - **📤 Salida (Output / Retorno):** `List<LintRule>`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `loadLintRules`, operando sobre los parámetros especificados (`configFile: String`) y devolviendo un resultado de tipo `List<LintRule>`.



---

## 📦 Módulo `executor`

**Propósito del Módulo:** Orquesta la ejecución completa de un script (Lexer + Parser + Interpreter).

**Cantidad de Archivos:** `1` archivos de código fuente.

### 📄 Archivo: [Executor.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/executor/src/main/kotlin/Executor.kt)

- **Ruta:** `executor/src/main/kotlin/Executor.kt`
- **Package:** `executor.src.main.kotlin`
- **Líneas de Código:** `79`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Executor`**
  - **Firma completa:** `class Executor`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`execute`**
  - **Firma:** `fun execute(args: List<String>)`
  - **📥 Entradas (Inputs):** `args: List<String>`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Evalúa recursivamente los nodos del AST en el entorno de ejecución actual y lleva a cabo las instrucciones correspondientes.
- **`println`**
  - **Firma:** `override fun println(x: Any?)`
  - **📥 Entradas (Inputs):** `x: Any?`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `println`, operando sobre los parámetros especificados (`x: Any?`) y devolviendo un resultado de tipo `Unit`.



---

## 📦 Módulo `runner`

**Propósito del Módulo:** Fachada principal que expone métodos de alto nivel para ejecutar, analizar y formatear código.

**Cantidad de Archivos:** `1` archivos de código fuente.

### 📄 Archivo: [Runner.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/runner/src/main/kotlin/Runner.kt)

- **Ruta:** `runner/src/main/kotlin/Runner.kt`
- **Package:** `org.example`
- **Líneas de Código:** `28`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Runner`**
  - **Firma completa:** `class Runner`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`executionCommand`**
  - **Firma:** `fun executionCommand(args: List<String>)`
  - **📥 Entradas (Inputs):** `args: List<String>`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `executionCommand`, operando sobre los parámetros especificados (`args: List<String>`) y devolviendo un resultado de tipo `Unit`.
- **`analyzerCommand`**
  - **Firma:** `fun analyzerCommand(args: List<String>)`
  - **📥 Entradas (Inputs):** `args: List<String>`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `analyzerCommand`, operando sobre los parámetros especificados (`args: List<String>`) y devolviendo un resultado de tipo `Unit`.
- **`formatterCommand`**
  - **Firma:** `fun formatterCommand(args: List<String>)`
  - **📥 Entradas (Inputs):** `args: List<String>`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `formatterCommand`, operando sobre los parámetros especificados (`args: List<String>`) y devolviendo un resultado de tipo `Unit`.
- **`validationCommand`**
  - **Firma:** `fun validationCommand(args: List<String>)`
  - **📥 Entradas (Inputs):** `args: List<String>`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `validationCommand`, operando sobre los parámetros especificados (`args: List<String>`) y devolviendo un resultado de tipo `Unit`.



---

## 📦 Módulo `container`

**Propósito del Módulo:** Contenedor de elementos y respuestas de remoción en el pipeline.

**Cantidad de Archivos:** `2` archivos de código fuente.

### 📄 Archivo: [Container.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/container/src/main/kotlin/Container.kt)

- **Ruta:** `container/src/main/kotlin/Container.kt`
- **Package:** `container.src.main.kotlin`
- **Líneas de Código:** `91`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Container`**
  - **Firma completa:** `class Container(     val container: List<Token> = listOf()`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`addContainer`**
  - **Firma:** `fun addContainer(token: Token): Container`
  - **📥 Entradas (Inputs):** `token: Token`
  - **📤 Salida (Output / Retorno):** `Container`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `addContainer`, operando sobre los parámetros especificados (`token: Token`) y devolviendo un resultado de tipo `Container`.
- **`addAll`**
  - **Firma:** `fun addAll(contents: List<Token>): Container`
  - **📥 Entradas (Inputs):** `contents: List<Token>`
  - **📤 Salida (Output / Retorno):** `Container`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `addAll`, operando sobre los parámetros especificados (`contents: List<Token>`) y devolviendo un resultado de tipo `Container`.
- **`addAt`**
  - **Firma:** `fun addAt(token: Token, index: Int): Container`
  - **📥 Entradas (Inputs):** `token: Token, index: Int`
  - **📤 Salida (Output / Retorno):** `Container`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `addAt`, operando sobre los parámetros especificados (`token: Token, index: Int`) y devolviendo un resultado de tipo `Container`.
- **`get`**
  - **Firma:** `fun get(index: Int): Token?`
  - **📥 Entradas (Inputs):** `index: Int`
  - **📤 Salida (Output / Retorno):** `Token?`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `get`, operando sobre los parámetros especificados (`index: Int`) y devolviendo un resultado de tipo `Token?`.
- **`remove`**
  - **Firma:** `fun remove(index: Int): RemoveResponse`
  - **📥 Entradas (Inputs):** `index: Int`
  - **📤 Salida (Output / Retorno):** `RemoveResponse`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `remove`, operando sobre los parámetros especificados (`index: Int`) y devolviendo un resultado de tipo `RemoveResponse`.
- **`first`**
  - **Firma:** `fun first(): Token?`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Token?`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `first`, operando sobre los parámetros especificados (`sin parámetros`) y devolviendo un resultado de tipo `Token?`.
- **`last`**
  - **Firma:** `fun last(): Token?`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Token?`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `last`, operando sobre los parámetros especificados (`sin parámetros`) y devolviendo un resultado de tipo `Token?`.
- **`take`**
  - **Firma:** `fun take(at: Int): Container`
  - **📥 Entradas (Inputs):** `at: Int`
  - **📤 Salida (Output / Retorno):** `Container`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `take`, operando sobre los parámetros especificados (`at: Int`) y devolviendo un resultado de tipo `Container`.
- **`slice`**
  - **Firma:** `fun slice(from: Int, to: Int = size()`
  - **📥 Entradas (Inputs):** `from: Int, to: Int = size(`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `slice`, operando sobre los parámetros especificados (`from: Int, to: Int = size(`) y devolviendo un resultado de tipo `Unit`.
- **`size`**
  - **Firma:** `fun size(): Int`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Int`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `size`, operando sobre los parámetros especificados (`sin parámetros`) y devolviendo un resultado de tipo `Int`.
- **`isEmpty`**
  - **Firma:** `fun isEmpty(): Boolean`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Boolean`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `isEmpty`, operando sobre los parámetros especificados (`sin parámetros`) y devolviendo un resultado de tipo `Boolean`.


### 📄 Archivo: [RemoveResponse.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/container/src/main/kotlin/RemoveResponse.kt)

- **Ruta:** `container/src/main/kotlin/RemoveResponse.kt`
- **Package:** `container.src.main.kotlin`
- **Líneas de Código:** `8`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`RemoveResponse`**
  - **Firma completa:** `data class RemoveResponse(     val token: Token?,     val container: Container )`



---

## 📦 Módulo `error`

**Propósito del Módulo:** Gestión centralizada y reporte de errores sintácticos, de linting y de ejecución.

**Cantidad de Archivos:** `1` archivos de código fuente.

### 📄 Archivo: [ErrorReporter.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/error/src/main/kotlin/ErrorReporter.kt)

- **Ruta:** `error/src/main/kotlin/ErrorReporter.kt`
- **Package:** `default`
- **Líneas de Código:** `35`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`ErrorReporter`**
  - **Firma completa:** `class ErrorReporter`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`reportError`**
  - **Firma:** `fun reportError(         operation: String,         exception: Exception,         tokens: Container? = null     )`
  - **📥 Entradas (Inputs):** `operation: String,         exception: Exception,         tokens: Container? = null`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `reportError`, operando sobre los parámetros especificados (`operation: String,         exception: Exception,         tokens: Container? = null`) y devolviendo un resultado de tipo `Unit`.
- **`report`**
  - **Firma:** `fun report(             operation: String,             exception: Exception,             tokens: Container? = null         )`
  - **📥 Entradas (Inputs):** `operation: String,             exception: Exception,             tokens: Container? = null`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `report`, operando sobre los parámetros especificados (`operation: String,             exception: Exception,             tokens: Container? = null`) y devolviendo un resultado de tipo `Unit`.



---

## 📦 Módulo `inputprovider`

**Propósito del Módulo:** Abstracción e implementaciones para la lectura interactiva de entrada de usuario (`readInput`).

**Cantidad de Archivos:** `2` archivos de código fuente.

### 📄 Archivo: [ConsoleInputProvider.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/inputprovider/src/main/kotlin/ConsoleInputProvider.kt)

- **Ruta:** `inputprovider/src/main/kotlin/ConsoleInputProvider.kt`
- **Package:** `inputprovider.src.main.kotlin`
- **Líneas de Código:** `17`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`ConsoleInputProvider`**
  - **Firma completa:** `class ConsoleInputProvider : InputProvider`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`readInput`**
  - **Firma:** `override fun readInput(prompt: String): String`
  - **📥 Entradas (Inputs):** `prompt: String`
  - **📤 Salida (Output / Retorno):** `String`
  - **💡 ¿Qué hace esta función?:** Solicita y lee de forma interactiva la entrada ingresada por el usuario (mediante consola o simulador).
- **`readEnv`**
  - **Firma:** `override fun readEnv(varName: String): String?`
  - **📥 Entradas (Inputs):** `varName: String`
  - **📤 Salida (Output / Retorno):** `String?`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `readEnv`, operando sobre los parámetros especificados (`varName: String`) y devolviendo un resultado de tipo `String?`.


### 📄 Archivo: [InputProvider.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/inputprovider/src/main/kotlin/InputProvider.kt)

- **Ruta:** `inputprovider/src/main/kotlin/InputProvider.kt`
- **Package:** `inputprovider.src.main.kotlin`
- **Líneas de Código:** `7`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`InputProvider`**
  - **Firma completa:** `interface InputProvider`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`readInput`**
  - **Firma:** `fun readInput(prompt: String): String     fun readEnv`
  - **📥 Entradas (Inputs):** `prompt: String`
  - **📤 Salida (Output / Retorno):** `String
    fun readEnv`
  - **💡 ¿Qué hace esta función?:** Solicita y lee de forma interactiva la entrada ingresada por el usuario (mediante consola o simulador).



---

## 📦 Módulo `progress`

**Propósito del Módulo:** Sistema de notificación de progreso e indicadores para tareas compuestas.

**Cantidad de Archivos:** `2` archivos de código fuente.

### 📄 Archivo: [MultiStepProgress.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/progress/src/main/kotlin/MultiStepProgress.kt)

- **Ruta:** `progress/src/main/kotlin/MultiStepProgress.kt`
- **Package:** `progress.src.main.kotlin`
- **Líneas de Código:** `28`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`MultiStepProgress`**
  - **Firma completa:** `class MultiStepProgress`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`initialize`**
  - **Firma:** `fun initialize(steps: Int): Boolean`
  - **📥 Entradas (Inputs):** `steps: Int`
  - **📤 Salida (Output / Retorno):** `Boolean`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `initialize`, operando sobre los parámetros especificados (`steps: Int`) y devolviendo un resultado de tipo `Boolean`.
- **`startStep`**
  - **Firma:** `fun startStep(message: String): ProgressIndicator`
  - **📥 Entradas (Inputs):** `message: String`
  - **📤 Salida (Output / Retorno):** `ProgressIndicator`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `startStep`, operando sobre los parámetros especificados (`message: String`) y devolviendo un resultado de tipo `ProgressIndicator`.
- **`complete`**
  - **Firma:** `fun complete(): Boolean`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Boolean`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `complete`, operando sobre los parámetros especificados (`sin parámetros`) y devolviendo un resultado de tipo `Boolean`.


### 📄 Archivo: [ProgressIndicator.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/progress/src/main/kotlin/ProgressIndicator.kt)

- **Ruta:** `progress/src/main/kotlin/ProgressIndicator.kt`
- **Package:** `progress.src.main.kotlin`
- **Líneas de Código:** `46`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`ProgressIndicator`**
  - **Firma completa:** `class ProgressIndicator(private val message: String)`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`start`**
  - **Firma:** `fun start()`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `start`, operando sobre los parámetros especificados (`sin parámetros`) y devolviendo un resultado de tipo `Unit`.
- **`stop`**
  - **Firma:** `fun stop()`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `stop`, operando sobre los parámetros especificados (`sin parámetros`) y devolviendo un resultado de tipo `Unit`.
- **`complete`**
  - **Firma:** `fun complete(resultMessage: String = "Done")`
  - **📥 Entradas (Inputs):** `resultMessage: String = "Done"`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `complete`, operando sobre los parámetros especificados (`resultMessage: String = "Done"`) y devolviendo un resultado de tipo `Unit`.
- **`fail`**
  - **Firma:** `fun fail(errorMessage: String = "Failed")`
  - **📥 Entradas (Inputs):** `errorMessage: String = "Failed"`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `fail`, operando sobre los parámetros especificados (`errorMessage: String = "Failed"`) y devolviendo un resultado de tipo `Unit`.



---

## 📦 Módulo `list`

**Propósito del Módulo:** Implementación personalizada de lista enlazada (`LinkedList`).

**Cantidad de Archivos:** `1` archivos de código fuente.

### 📄 Archivo: [LinkedList.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/list/src/main/kotlin/org/example/list/LinkedList.kt)

- **Ruta:** `list/src/main/kotlin/org/example/list/LinkedList.kt`
- **Package:** `org.example.list`
- **Líneas de Código:** `86`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`LinkedList`**
  - **Firma completa:** `class LinkedList`
- **`Node`**
  - **Firma completa:** `private data class Node(val data: String)`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`add`**
  - **Firma:** `fun add(element: String)`
  - **📥 Entradas (Inputs):** `element: String`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `add`, operando sobre los parámetros especificados (`element: String`) y devolviendo un resultado de tipo `Unit`.
- **`tail`**
  - **Firma:** `private fun tail(head: Node?): Node?`
  - **📥 Entradas (Inputs):** `head: Node?`
  - **📤 Salida (Output / Retorno):** `Node?`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `tail`, operando sobre los parámetros especificados (`head: Node?`) y devolviendo un resultado de tipo `Node?`.
- **`remove`**
  - **Firma:** `fun remove(element: String): Boolean`
  - **📥 Entradas (Inputs):** `element: String`
  - **📤 Salida (Output / Retorno):** `Boolean`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `remove`, operando sobre los parámetros especificados (`element: String`) y devolviendo un resultado de tipo `Boolean`.
- **`unlink`**
  - **Firma:** `private fun unlink(previousIt: Node?, currentIt: Node)`
  - **📥 Entradas (Inputs):** `previousIt: Node?, currentIt: Node`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `unlink`, operando sobre los parámetros especificados (`previousIt: Node?, currentIt: Node`) y devolviendo un resultado de tipo `Unit`.
- **`size`**
  - **Firma:** `fun size(): Int`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Int`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `size`, operando sobre los parámetros especificados (`sin parámetros`) y devolviendo un resultado de tipo `Int`.
- **`get`**
  - **Firma:** `fun get(idx: Int): String`
  - **📥 Entradas (Inputs):** `idx: Int`
  - **📤 Salida (Output / Retorno):** `String`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `get`, operando sobre los parámetros especificados (`idx: Int`) y devolviendo un resultado de tipo `String`.



---

## 📦 Módulo `cli`

**Propósito del Módulo:** Interfaz de línea de comandos del proyecto.

**Cantidad de Archivos:** `2` archivos de código fuente.

### 📄 Archivo: [Cli.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/cli/src/main/kotlin/Cli.kt)

- **Ruta:** `cli/src/main/kotlin/Cli.kt`
- **Package:** `cli.src.main.kotlin`
- **Líneas de Código:** `24`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Cli`**
  - **Firma completa:** `class Cli`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`run`**
  - **Firma:** `fun run(args: List<String>)`
  - **📥 Entradas (Inputs):** `args: List<String>`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `run`, operando sobre los parámetros especificados (`args: List<String>`) y devolviendo un resultado de tipo `Unit`.


### 📄 Archivo: [Main.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/cli/src/main/kotlin/Main.kt)

- **Ruta:** `cli/src/main/kotlin/Main.kt`
- **Package:** `cli.src.main.kotlin`
- **Líneas de Código:** `9`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`Main`**
  - **Firma completa:** `object Main`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`main`**
  - **Firma:** `fun main(args: Array<String>)`
  - **📥 Entradas (Inputs):** `args: Array<String>`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Punto de entrada principal de la aplicación. Recibe los argumentos de consola y lanza el flujo de trabajo seleccionado.



---

## 📦 Módulo `mainApp`

**Propósito del Módulo:** Punto de entrada de la aplicación principal.

**Cantidad de Archivos:** `1` archivos de código fuente.

### 📄 Archivo: [Main.kt](file:///C:/Users/laris/Downloads/Ingsis/PrintScript-Tools/mainApp/src/main/kotlin/Main.kt)

- **Ruta:** `mainApp/src/main/kotlin/Main.kt`
- **Package:** `default`
- **Líneas de Código:** `93`
- **¿Qué hace este archivo?:** Archivo de implementación del módulo.

#### 🔹 Estructuras y Clases Definidas:
- **`RootCommand`**
  - **Firma completa:** `class RootCommand : Runnable`

#### ⚙️ Funciones y Métodos (Firma, Inputs, Outputs y Comportamiento):
- **`startInteractiveMode`**
  - **Firma:** `fun startInteractiveMode()`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `startInteractiveMode`, operando sobre los parámetros especificados (`sin parámetros`) y devolviendo un resultado de tipo `Unit`.
- **`run`**
  - **Firma:** `override fun run()`
  - **📥 Entradas (Inputs):** `Ninguno (sin argumentos)`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Lleva a cabo la función de `run`, operando sobre los parámetros especificados (`sin parámetros`) y devolviendo un resultado de tipo `Unit`.
- **`main`**
  - **Firma:** `fun main(args: Array<String>)`
  - **📥 Entradas (Inputs):** `args: Array<String>`
  - **📤 Salida (Output / Retorno):** `Unit`
  - **💡 ¿Qué hace esta función?:** Punto de entrada principal de la aplicación. Recibe los argumentos de consola y lanza el flujo de trabajo seleccionado.



---
