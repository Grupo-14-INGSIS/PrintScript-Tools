# ??? Arquitectura Modular y Sistema de Plugins - PrintScript-Tools

Este documento detalla la arquitectura modular del proyecto, la justificación de diseño de cada módulo, cómo se estructuran internamente mediante **packages**, y cómo extender el sistema mediante **plugins** para procesar lenguajes distintos a PrintScript.

---

## ?? 1. Reestructuración de Módulos (De 18 Nano-módulos a Módulos Cohesivos)

Se eliminaron los "nano-módulos" de un solo archivo y se agruparon por **Common Closure Principle (CCP)** en los módulos naturales de un compilador:

| Módulo | Contenido y Módulos Absorbidos | Responsabilidad Única (SRP del Módulo) |
| :--- | :--- | :--- |
| **`token`** | `Token`, `DataType`, `Position`, `Container`, `RemoveResponse`, `ErrorReporter` (absorbió `tokendata`, `container`, `error`). | **Modelos y contratos léxicos:** Estructura atómica del token, tipos de datos, posiciones espaciales y flujos de tokens. |
| **`ast`** | `ASTNode` | **Modelo sintáctico:** Definición y jerarquía del Árbol de Sintaxis Abstracta (`ASTNode`). |
| **`lexer`** | Motor de streaming, `CharSource`, `CharacterClassifier`, `TokenPlugin`, `ExactMatchTokenPlugin`, `RegexTokenPlugin`, `TokenPluginFactory`. | **Motor léxico:** Transforma secuencias de caracteres en streams de tokens mediante plugins de reconocimiento. |
| **`parser`** | Pratt Parser, `ExpressionParser`, `StatementParser`, `StatementParserFactory`, `PrattToken`, etc. | **Motor sintáctico:** Transforma secuencias de tokens en un AST validando la gramática. |
| **`interpreter`** | Motor de evaluación, `Environment`, `ExecutionContext`, `InputProvider`, `ConsoleInputProvider`, `ActionType` (`Add`, `Print`, `If`, etc.). | **Motor de ejecución semántica:** Evalúa el AST en memoria y gestiona ámbitos/variables e I/O. |
| **`formatter`** | `Formatter`, `FormatRule` (reglas obligatorias y opcionales), `ConfigLoader`. | **Motor de formateo:** Aplica reglas de estilo, espaciado y saltos de línea sobre tokens. |
| **`linter`** | `Linter`, `LintRule` (`IdentifierNamingRule`, `PrintLnRule`, etc.), `ConfigLoader`. | **Motor de análisis estático:** Valida reglas de buenas prácticas sobre el AST. |
| **`cli`** | `MainApp` (JLine REPL / Picocli), `Cli`, `Runner`, `Executor`, `Analyzer`, `FormatterAction`, `MultiStepProgress`, `ProgressIndicator`. | **Capa de aplicación y presentación:** Orquesta los comandos de usuario, CLI interactiva y reporta el progreso. |
| **`globalTests`** | Tests de integración End-to-End, TCK y tests agnósticos. | **Validación integral del sistema.** |

---

## ?? 2. ¿Por qué `token` y `ast` están separados? (Evitando la "Bolsa de Gatos")

Juntar `token` y `ast` en un único módulo genérico (como `core` o `common`) introduce el riesgo del **anti-patrón "Junk Drawer" (Cajón de cachivaches / Bolsa de gatos)**, donde se arroja cualquier clase sin identidad clara. 

Mantener `token` y `ast` como módulos independientes aporta 3 beneficios fundamentales:

1. **Identidad de Dominio Clara:**
   * **`token`:** Modela la **representación léxica lineal y plana** (caracteres, palabras clave, posiciones en el archivo).
   * **`ast`:** Modela la **representación sintáctica jerárquica** (árbol de nodos con relaciones padre-hijo).
2. **El Compilador Previene Acoplamientos Accidentales:**
   * Si estuvieran juntos, un desarrollador podría importar `ASTNode` dentro del `lexer` o dentro del `formatter` por error y Gradle lo compilaría.
   * Al estar separados, `lexer` y `formatter` **físicamente no tienen `ast` en su classpath**. El compilador de Kotlin impide romper las capas.
3. **Fases Formales de Compiladores:**
   * Fase Léxica: Texto $\rightarrow$ Tokens (`token`).
   * Fase Sintáctica: Tokens $\rightarrow$ AST (`ast`).
   * Fase Semántica: AST $\rightarrow$ Ejecución / Análisis.

---

## ?? 3. ¿Por qué dentro de un Módulo se usan Packages en lugar de crear más Módulos/Carpetas Gradle?

Es fundamental diferenciar los 3 conceptos:

1. **Módulo Gradle (Unidad de Compilación y Despliegue):**
   * Cada módulo tiene su propio `build.gradle`, genera su propio archivo `.jar` y tiene un classpath aislado.
   * Crear un módulo Gradle para 1 o 2 archivos genera sobrecarga de configuración, lentitud en Gradle y dependencias cruzadas innecesarias.
2. **Package en Kotlin/Java (Espacio de Nombres Lógico):**
   * Es la herramienta nativa del lenguaje para organizar y categorizar clases dentro de un mismo módulo.
   * Permite usar modificadores de visibilidad (`internal` en Kotlin) para que ciertas clases sean accesibles dentro del módulo pero invisibles para los módulos externos.
3. **Carpetas Físicas en el Disco:**
   * La estructura `src/main/kotlin/...` es simplemente la convención de carpetas del sistema de archivos donde residen los archivos `.kt`.

> **Regla de Oro:** Se crea un **Módulo Gradle** cuando hay un límite de subsistema/despliegue (ej. `lexer` vs `parser`). Dentro de ese subsistema, se usan **Packages** para organizar las clases lógicamente sin sobrecargar el build.

---

## ?? 4. ¿Cómo se Enlazan los Módulos entre Sí?

El grafo de dependencias es **estrictamente unidireccional y acíclico**:

```mermaid
graph TD
    subgraph Modelos["Capa de Modelos (Contratos de Datos)"]
        TOKEN["token (Token, DataType, Position, Container)"]
        AST["ast (ASTNode)"]
        AST --> TOKEN
    end

    subgraph Motores["Capa de Motores / Herramientas"]
        LEXER["lexer"] --> TOKEN
        FORMATTER["formatter"] --> TOKEN
        PARSER["parser"] --> TOKEN
        PARSER --> AST
        INTERPRETER["interpreter"] --> AST
        INTERPRETER --> TOKEN
        LINTER["linter"] --> AST
        LINTER --> TOKEN
    end

    subgraph App["Capa de Aplicación y Pruebas"]
        CLI["cli (Runner, Executor, Analyzer, FormatterAction, Progress)"]
        TESTS["globalTests"]
        
        CLI --> LEXER
        CLI --> PARSER
        CLI --> INTERPRETER
        CLI --> FORMATTER
        CLI --> LINTER
        
        TESTS --> CLI
        TESTS --> Motores
        TESTS --> Modelos
    end
```

### Flujo de Ejecución en el Pipeline:
1. **`cli`** recibe el comando del usuario (`execution`, `analyzer`, `formatter`, etc.) y el archivo fuente.
2. **`lexer`** lee los caracteres y, mediante su lista de `TokenPlugin`, produce un `Container` con los `Token`s.
3. **`parser`** recibe los `Token`s y ejecuta sus `StatementParser`s para construir el `ASTNode`.
4. **`interpreter`** recibe el `ASTNode` y despacha cada nodo a su `ActionType` handler correspondiente.
5. **`formatter`** recibe los `Token`s y ejecuta sus `FormatRule`s para producir el código formateado.
6. **`linter`** recibe el `ASTNode` y ejecuta sus `LintRule`s para reportar advertencias o errores de estilo.

---

## ?? 5. Lógica de Plugins en Cada Módulo

Todos los módulos siguen el **Principio Abierto/Cerrado (OCP)** y **Inversión de Dependencias (DIP)**:

1. **Lexer (`TokenPlugin`):**
   * Interfaz: `fun match(piece: String, position: Position): Token?`
   * Permite inyectar nuevos plugins regex o palabras clave exactas (`ExactMatchTokenPlugin`, `RegexTokenPlugin`).
2. **Parser (`StatementParser` & `PrattToken`):**
   * Interfaz: `fun canParse(tokens: Container): Boolean` y `fun parse(tokens: Container, parser: ExpressionParser): ASTNode`.
   * Permite agregar nuevas construcciones sintácticas (ej. bucles `while`, sentencias `match`, etc.) inyectando un nuevo `StatementParser`.
3. **Interpreter (`ActionType`):**
   * Interfaz: `fun interpret(node: ASTNode, interpreter: ExecutionContext): Any`
   * Permite registrar nuevos comportamientos con `interpreter.registerHandler(action, handler)` o `registerFunctionAction(name, action)`.
4. **Formatter (`FormatRule`):**
   * Interfaz: `fun apply(tokens: Container, context: FormatterContext): Container`
   * Permite agregar reglas de espaciado o indentación configurables mediante YAML.
5. **Linter (`LintRule`):**
   * Interfaz: `fun lint(node: ASTNode): List<LintError>`
   * Permite agregar nuevas reglas estáticas configurables mediante YAML.

---

## ?? 6. ¿Cómo Probar el Motor con un Lenguaje Distinto a PrintScript?

Dado que los motores reciben abstracciones (`TokenPlugin`, `StatementParser`, `ActionType`), puedes definir un mini-lenguaje completo **sin tocar una sola línea del código core**.

Ver el test de integración en `globalTests/src/test/kotlin/LanguageAgnosticPluginTest.kt`:

```kotlin
// 1. Definir plugins léxicos para el nuevo lenguaje (sintaxis: echo <expr>;)
val customPlugins: List<TokenPlugin> = listOf(
    ExactMatchTokenPlugin(
        mapOf(
            "echo" to DataType.PRINTLN,
            "+" to DataType.ADDITION,
            ";" to DataType.SEMICOLON,
            " " to DataType.SPACE
        )
    ),
    RegexTokenPlugin(Regex("^[0-9]+$"), DataType.NUMBER_LITERAL)
)

// 2. Lexear con el motor agnóstico
val lexer = Lexer.from("echo 5 + 10;", customPlugins)
val statements = lexer.lexIntoStatements().toList()

// 3. Parser para la sentencia 'echo'
val customEchoStatementParser = object : StatementParser {
    override fun canParse(tokens: Container): Boolean = 
        tokens.size() >= 2 && tokens.first()?.type == DataType.PRINTLN

    override fun parse(tokens: Container, parser: ExpressionParser): ASTNode {
        val exprAst = parser.expParse(tokens.slice(1, tokens.size()))
        return ASTNode(DataType.PRINTLN, "echo", Position(1, 1), listOf(exprAst))
    }
}
val parser = Parser(statements.first(), "1.0", listOf(customEchoStatementParser))
val ast = parser.parse()

// 4. Intérprete con handler personalizado
val outputs = mutableListOf<String>()
val interpreter = Interpreter("1.0", printer = { println(it) })
interpreter.registerHandler(Actions.PRINT, object : ActionType {
    override fun interpret(node: ASTNode, interpreter: ExecutionContext): Any {
        val value = node.children.firstOrNull()?.let { interpreter.interpret(it) }
        interpreter.printer("CUSTOM ECHO: $value")
        return value ?: ""
    }
})

interpreter.interpret(ast)
// Output: CUSTOM ECHO: 15
```
