# ??? Arquitectura Modular y Sistema de Plugins - PrintScript-Tools

Este documento detalla la arquitectura modular del proyecto, la justificación de diseño de cada módulo, cómo se estructuran internamente mediante **packages**, y cómo extender el sistema mediante **plugins** para procesar lenguajes distintos a PrintScript o agregar nuevos comandos CLI.

---

## ?? 1. Reestructuración de Módulos (De 18 Nano-módulos a Módulos Cohesivos)

Se eliminaron los "nano-módulos" de un solo archivo y se agruparon por **Common Closure Principle (CCP)** en los módulos naturales de un compilador:

| Módulo | Contenido y Módulos Absorbidos | Responsabilidad Única (SRP del Módulo) |
| :--- | :--- | :--- |
| **`token`** | `Token`, `DataType`, `Position`, `Container`, `RemoveResponse`, `ErrorReporter` (absorbió `tokendata`, `container`, `error`). | **Modelos y contratos léxicos:** Estructura atómica del token, tipos de datos, posiciones espaciales y flujos de tokens. |
| **`ast`** | `ASTNode`, `ASTNodeType` | **Modelo sintáctico puro:** Definición de la estructura en árbol (`ASTNode`) y su clasificación semántica (`ASTNodeType`), desacoplado de los tokens léxicos. |
| **`lexer`** | Motor de streaming, `CharSource`, `CharacterClassifier`, `TokenPlugin`, `ExactMatchTokenPlugin`, `RegexTokenPlugin`, `TokenPluginFactory`. | **Motor léxico:** Transforma secuencias de caracteres en streams de tokens mediante plugins de reconocimiento. |
| **`parser`** | Pratt Parser, `ExpressionParser`, `StatementParser`, `StatementParserFactory`, `PrattToken`, etc. | **Motor sintáctico y traductor:** Transforma secuencias de `Token` (`DataType`) en un AST puro (`ASTNodeType`). |
| **`interpreter`** | Motor de evaluación, `Environment`, `ExecutionContext`, `InputProvider`, `ConsoleInputProvider`, `ActionType` (`Add`, `Print`, `If`, etc.). | **Motor de ejecución semántica:** Evalúa los nodos del AST (`ASTNodeType`) en memoria y gestiona ámbitos/variables e I/O. |
| **`formatter`** | `Formatter`, `FormatRule` (reglas obligatorias y opcionales), `ConfigLoader`. | **Motor de formateo:** Aplica reglas de estilo, espaciado y saltos de línea sobre tokens. |
| **`linter`** | `Linter`, `LintRule` (`IdentifierNamingRule`, `PrintLnRule`, etc.), `ConfigLoader`. | **Motor de análisis estático:** Valida reglas de buenas prácticas sobre el AST (`ASTNodeType`). |
| **`cli`** | `MainApp` (JLine REPL / Picocli), `Cli`, `CliCommand` (`ExecutionCommand`, `AnalyzerCommand`, `FormatterCommand`, `ValidationCommand`), `Runner`, `Executor`, `Analyzer`, `FormatterAction`, `MultiStepProgress`, `ProgressIndicator`. | **Capa de aplicación extensible:** Despacha comandos desacoplados mediante el patrón Command / Plugins y maneja la UI interactiva. |
| **`globalTests`** | Tests de integración End-to-End, TCK y tests agnósticos. | **Validación integral del sistema.** |

---

## ?? 2. ¿Por qué `token` y `ast` están separados? (Evitando la "Bolsa de Gatos")

Juntar `token` y `ast` en un único módulo genérico (como `core` o `common`) introduce el riesgo del **anti-patrón "Junk Drawer" (Cajón de cachivaches / Bolsa de gatos)**, donde se arroja cualquier clase sin identidad clara. 

Mantener `token` y `ast` como módulos independientes aporta 3 beneficios fundamentales:

1. **Identidad de Dominio Clara:**
   * **`token`:** Modela la **representación léxica lineal y plana** (palabras clave, operadores, caracteres, posiciones en el archivo).
   * **`ast`:** Modela la **representación sintáctica jerárquica** (`ASTNodeType` con relaciones padre-hijo).
2. **El Compilador Previene Acoplamientos Accidentales:**
   * Al estar separados, `lexer` y `formatter` **físicamente no tienen `ast` en su classpath**. Si un desarrollador intenta importar `ASTNode` dentro del lexer, el compilador de Kotlin arroja error y previene acoplamientos indebidos.
3. **Fases Formales de Compiladores:**
   * Fase Léxica: Texto $\rightarrow$ Tokens (`token`).
   * Fase Sintáctica: Tokens $\rightarrow$ AST (`ast`).
   * Fase Semántica: AST $\rightarrow$ Ejecución / Análisis.

---

## ?? 3. Desacoplamiento Léxico vs Sintáctico (`DataType` vs `ASTNodeType`)

Anteriormente, los nodos del AST reutilizaban el enum `DataType` del módulo `token`. Esto obligaba al AST a depender del vocabulario léxico (signos de puntuación como `;`, `{`, espaciados, etc.).

Para resolver esto:
1. **`ASTNodeType` vive en `ast`:** Describe la semántica pura del nodo (`DECLARATION`, `ASSIGNATION`, `IF_STATEMENT`, `BINARY_OPERATION`, `LITERAL`, etc.).
2. **`Parser` actúa como único puente de traducción:** Convierte `DataType` léxico en `ASTNodeType` sintáctico.
3. **`Interpreter` y `Linter` consumen `ASTNodeType`:** Ya no necesitan importar `DataType` ni saber cómo se leyeron los tokens originales.

---

## ?? 4. ¿Por qué dentro de un Módulo se usan Packages en lugar de crear más Módulos/Carpetas Gradle?

Es fundamental diferenciar los 3 conceptos:

1. **Módulo Gradle (Unidad de Compilación y Despliegue):**
   * Cada módulo tiene su propio `build.gradle`, genera su propio archivo `.jar` y tiene un classpath aislado.
   * Crear un módulo Gradle para 1 o 2 archivos genera sobrecarga de configuración, lentitud en Gradle y dependencias cruzadas innecesarias.
2. **Package en Kotlin/Java (Espacio de Nombres Lógico):**
   * Es la herramienta nativa del lenguaje para organizar y clasificar las clases **dentro del mismo módulo**.
   * Permite usar modificadores de visibilidad (`internal` en Kotlin) para que ciertas clases sean accesibles dentro del módulo pero invisibles para los módulos externos.
3. **Carpetas Físicas en el Disco:**
   * La estructura `src/main/kotlin/...` es simplemente la convención de carpetas del sistema de archivos donde residen los archivos `.kt`.

---

## ?? 5. Grafo de Dependencias Unidireccional y Acíclico

```mermaid
flowchart TD
    subgraph CapaModelos["Modelos de Datos"]
        TOKEN["token (Token, DataType, Position)"]
        AST["ast (ASTNode, ASTNodeType)"]
        AST -->|solo usa Position| TOKEN
    end

    subgraph CapaMotores["Motores del Compilador"]
        LEXER["lexer"] --> TOKEN
        FORMATTER["formatter"] --> TOKEN
        
        PARSER["parser (Puente Traductor)"] --> TOKEN
        PARSER --> AST
        
        INTERPRETER["interpreter"] --> AST
        LINTER["linter"] --> AST
    end

    subgraph CapaApp["Capa de Aplicación y Pruebas"]
        CLI["cli (Command Plugins, Runner, Executor, Analyzer)"]
        TESTS["globalTests"]
        
        CLI --> LEXER
        CLI --> PARSER
        CLI --> INTERPRETER
        CLI --> FORMATTER
        CLI --> LINTER
        
        TESTS --> CLI
        TESTS --> CapaMotores
        TESTS --> CapaModelos
    end
```

---

## ?? 6. Lógica de Plugins en Cada Módulo (100% Extensible)

Todos los módulos siguen el **Principio Abierto/Cerrado (OCP)** y el **Command Pattern**:

1. **CLI (`CliCommand`):**
   * Interfaz: `interface CliCommand { val name: String; val description: String; fun execute(args: List<String>) }`
   * Los comandos ya no usan switches hardcodeados: `Cli` mantiene un registro dinámico (`cli.registerCommand(...)`). Se pueden registrar nuevos comandos en caliente sin modificar `Cli.kt`.
2. **Lexer (`TokenPlugin`):**
   * Interfaz: `fun match(piece: String, position: Position): Token?`
   * Permite inyectar nuevos plugins regex o palabras clave exactas (`ExactMatchTokenPlugin`, `RegexTokenPlugin`).
3. **Parser (`StatementParser` & `PrattToken`):**
   * Interfaz: `fun canParse(tokens: Container): Boolean` y `fun parse(tokens: Container, parser: ExpressionParser): ASTNode`.
   * Permite agregar nuevas construcciones sintácticas (ej. bucles `while`, `match`, etc.) inyectando un nuevo `StatementParser`.
4. **Interpreter (`ActionType`):**
   * Interfaz: `fun interpret(node: ASTNode, interpreter: ExecutionContext): Any`
   * Permite registrar nuevos comportamientos con `interpreter.registerHandler(action, handler)` o `registerFunctionAction(name, action)`.
5. **Formatter (`FormatRule`):**
   * Interfaz: `fun format(statements: List<Container>): List<Container>`
   * Permite agregar reglas de espaciado o indentación configurables mediante YAML.
6. **Linter (`LintRule`):**
   * Interfaz: `fun lint(node: ASTNode): List<LintError>`
   * Permite agregar nuevas reglas estáticas configurables mediante YAML.

---

## ?? 7. ¿Cómo Probar el Motor con un Lenguaje Distinto a PrintScript?

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

// 3. Parser para la sentencia 'echo' que emite ASTNodeType
val customEchoStatementParser = object : StatementParser {
    override fun canParse(tokens: Container): Boolean = 
        tokens.size() >= 2 && tokens.first()?.type == DataType.PRINTLN

    override fun parse(tokens: Container, parser: ExpressionParser): ASTNode {
        val exprAst = parser.expParse(tokens.slice(1, tokens.size()))
        return ASTNode(ASTNodeType.PRINTLN, "echo", Position(1, 1), listOf(exprAst))
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
