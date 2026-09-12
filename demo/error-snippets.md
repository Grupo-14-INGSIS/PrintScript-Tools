# PrintScript - Catálogo de Snippets con Errores

Este archivo contiene diversos snippets de código que provocan errores controlados en las distintas fases del pipeline de PrintScript (**Análisis Léxico, Sintaxis, Semántica, Ejecución en Tiempo de Ejecución y Linter Estático**).

---

## 1. Errores por Incompatibilidad de Versión (Version Mismatch)

### 1.1. Uso de `const` en PrintScript 1.0
En la versión 1.0 las constantes no existen; solo se admite `let`.
```printscript
const pi: number = 3.14159;
```
* **Comando para probarlo:**
  ```bash
  ./gradlew :cli:run --args="validation demo/test.ps 1.0"
  ```
* **Error producido:**
  ```text
  SYNTAX ERROR: Invalid AST for statement
  Near token: 'const' (IDENTIFIER)
  ```
* **Explicación:** En 1.0, `const` no es palabra reservada y el lexer lo emite como `IDENTIFIER`. El parser de declaraciones falla porque espera un `LET_KEYWORD`.

---

### 1.2. Uso del tipo `boolean` o literales `true` / `false` en 1.0
```printscript
let isActive: boolean = true;
```
* **Comando para probarlo:**
  ```bash
  ./gradlew :cli:run --args="validation demo/test.ps 1.0"
  ```
* **Error producido:**
  ```text
  SYNTAX ERROR: Error: Unknown or unsupported type 'boolean' in PrintScript 1.0
  ```
* **Explicación:** El parser valida el tipo contra `features.types` de la 1.0, que únicamente incluye `string` y `number`.

---

### 1.3. Estructuras condicionales `if` / `else` en 1.0
```printscript
if (10) {
    let x: number = 5;
} else {
    let y: number = 10;
}
```
* **Comando para probarlo:**
  ```bash
  ./gradlew :cli:run --args="validation demo/test.ps 1.0"
  ```
* **Error producido:**
  ```text
  SYNTAX ERROR: Invalid AST for statement
  Near token: 'if' (IDENTIFIER)
  ```

---

### 1.4. Funciones de entrada `readInput` o `readEnv` en 1.0
```printscript
let name: string = readInput("Nombre: ");
```
* **Comando para probarlo:**
  ```bash
  ./gradlew :cli:run --args="execution demo/test.ps 1.0"
  ```
* **Error producido:**
  ```text
  ERROR during execution: Action READ_INPUT is not supported in version 1.0
  ```

---

## 2. Errores Semánticos y de Inmutabilidad

### 2.1. Reasignación de una constante (`const`) en 1.1
```printscript
const pi: number = 3.14;
pi = 3.14159;
```
* **Comando para probarlo:**
  ```bash
  ./gradlew :cli:run --args="execution demo/test.ps 1.1"
  ```
* **Error producido:**
  ```text
  ERROR during execution: Cannot reassign a constant: 'pi'
  ```
* **Explicación:** El `Environment` detecta que el identificador está registrado en el conjunto de constantes inmutables y bloquea la reasignación.

---

### 2.2. Incompatibilidad de tipos en asignación (Type Mismatch)
```printscript
let count: number = "texto_invalido";
```
* **Comando para probarlo:**
  ```bash
  ./gradlew :cli:run --args="execution demo/test.ps 1.0"
  ```
* **Error producido:**
  ```text
  ERROR during execution: Valor '"texto_invalido"' no se puede convertir a número
  ```

---

### 2.3. Tipos inexistentes o abreviados
```printscript
let text: str = "Hola mundo";
```
* **Comando para probarlo:**
  ```bash
  ./gradlew :cli:run --args="validation demo/test.ps 1.0"
  ```
* **Error producido:**
  ```text
  SYNTAX ERROR: Error: Unknown or unsupported type 'str' in PrintScript 1.0
  ```
* **Explicación:** No se admiten abreviaturas como `str`, `int` o `bool`. Deben escribirse exactamente `string`, `number` y `boolean`.

---

## 3. Errores en Tiempo de Ejecución (Runtime)

### 3.1. División por cero
```printscript
let total: number = 100 / 0;
```
* **Comando para probarlo:**
  ```bash
  ./gradlew :cli:run --args="execution demo/test.ps 1.0"
  ```
* **Error producido:**
  ```text
  ERROR during execution: Cannot divide by zero
  ```

---

### 3.2. Variable no declarada
```printscript
let total: number = variableInexistente + 10;
```
* **Comando para probarlo:**
  ```bash
  ./gradlew :cli:run --args="execution demo/test.ps 1.0"
  ```
* **Error producido:**
  ```text
  ERROR during execution: Variable 'variableInexistente' not declared
  ```

---

### 3.3. Variable de entorno inexistente en `readEnv`
```printscript
let secret: string = readEnv("VARIABLE_DEL_SISTEMA_TOTALMENTE_INEXISTENTE_XYZ");
```
* **Comando para probarlo:**
  ```bash
  ./gradlew :cli:run --args="execution demo/test.ps 1.1"
  ```
* **Error producido:**
  ```text
  ERROR during execution: Environment variable 'VARIABLE_DEL_SISTEMA_TOTALMENTE_INEXISTENTE_XYZ' not found
  ```

---

## 4. Errores Léxicos y de Sintaxis

### 4.1. Intentar usar comentarios (`//`)
```printscript
let x: number = 5; // Esto es un comentario
```
* **Comando para probarlo:**
  ```bash
  ./gradlew :cli:run --args="validation demo/test.ps 1.0"
  ```
* **Error producido:**
  ```text
  ERROR during validation: Statement must end with a semicolon or closing brace. Remaining: [ , /, /,  , Esto, ... ]
  ```
* **Explicación:** PrintScript no tiene comentarios en su gramática. Las barras `/` se tokenizan como operadores de división consecutivos.

---

### 4.2. Falta de punto y coma `;`
```printscript
let x: number = 10
let y: number = 20;
```
* **Comando para probarlo:**
  ```bash
  ./gradlew :cli:run --args="validation demo/test.ps 1.0"
  ```
* **Error producido:**
  ```text
  SYNTAX ERROR: Statement must end with a semicolon
  ```

---

### 4.3. Paréntesis u operadores desbalanceados
```printscript
let x: number = (5 + 3 * 2;
```
* **Comando para probarlo:**
  ```bash
  ./gradlew :cli:run --args="validation demo/test.ps 1.0"
  ```
* **Error producido:**
  ```text
  SYNTAX ERROR: Mismatched parentheses or invalid expression
  ```

---

## 5. Errores de Análisis Estático (Linter)

### 5.1. Violación de estilo de identificadores (`camelCase`)
```printscript
let Snake_case_variable: number = 10;
```
* **Comando para probarlo:**
  ```bash
  ./gradlew :cli:run --args="analyzer demo/test.ps demo/lint-rules.yaml 1.0"
  ```
* **Resultado del Linter:**
  ```text
  ANALYSIS RESULTS: 1 issue(s) found:
    - [Linter] Identifier 'Snake_case_variable' does not match camelCase style
  ```

---

### 5.2. Expresión compleja dentro de `println`
```printscript
let x: number = 5;
println("Valor: " + x);
```
* **Regla configurada:** `mandatory-variable-or-literal-in-println: enabled: true`
* **Resultado del Linter:**
  ```text
  ANALYSIS RESULTS: 1 issue(s) found:
    - [Linter] println argument must be a literal or identifier
  ```
* **Explicación:** La regla obliga a almacenar primero la expresión en una variable intermedia antes de imprimirla.
