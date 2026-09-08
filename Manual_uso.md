# Manual de Uso de CLIPS (PrintScript CLI)

CLIPS (**C**ommand **L**ine **I**nterface for **P**rint**S**cript) es la interfaz de línea de comandos oficial desarrollada en Kotlin con soporte modular y basada en la librería **Clikt** y **JLine3**. Permite ejecutar scripts, analizar estáticamente el código (linting), validar sintaxis y formatear archivos conforme a reglas configurables.

---

## 1. Cómo Iniciar la CLI

La CLI puede iniciarse principalmente en dos modalidades: **Modo Interactivo (REPL)** o **Modo de Comando Directo (Batch)**.

### A. Modo Interactivo (Recomendado para uso continuo)
Permite ingresar a una consola interactiva con historial y comandos en tiempo real.

- **En Windows (PowerShell / CMD):**
  ```powershell
  .\gradlew.bat :cli:run -q --console=plain
  ```
- **En Linux / macOS:**
  ```bash
  ./gradlew :cli:run
  ```

Al iniciar, se presentará el banner de bienvenida y el prompt interactivo:
```text
=== CLIPS - CLI PrintScript - Modo Interactivo ===
Comandos disponibles: formatter | analyzer | validation | execution
Escribe 'exit' para salir

CLIPS> 
```

### B. Modo Directo / No Interactivo
Ideal para scripts, pipelines de CI/CD o ejecuciones puntuales desde la terminal. Se envía el comando y sus argumentos mediante el parámetro `--args`:

```powershell
.\gradlew.bat :cli:run --args="<comando> <argumentos>"
```

### C. Ejecución como Binario Distribución (Sin Gradle)
Puedes empaquetar la aplicación como distribución nativa:
```powershell
.\gradlew.bat :cli:installDist
```
Y luego ejecutar el script generado:
- **Windows:** `cli\build\install\cli\bin\cli.bat [comando y argumentos]`
- **Linux/macOS:** `cli/build/install/cli/bin/cli [comando y argumentos]`

---

## 2. Comandos Disponibles y Sintaxis

La CLI cuenta con cuatro comandos principales. En todos los casos, la versión de PrintScript es opcional y toma por defecto `1.0` (además admite `1.1`).

### 1. `execution`
Ejecuta el script de PrintScript interpretando sus instrucciones e imprimiendo la salida estándar.

- **Sintaxis:**
  ```text
  execution <source_file> [version]
  ```
- **Argumentos:**
  - `<source_file>`: Ruta al archivo `.ps` a ejecutar (obligatorio).
  - `[version]`: Versión del lenguaje (`1.0` o `1.1`, por defecto: `1.0`).
- **Ejemplo en Modo Interactivo:**
  ```text
  CLIPS> execution test.ps 1.0
  ```
- **Ejemplo en Modo Directo:**
  ```powershell
  .\gradlew.bat :cli:run --args="execution test.ps 1.0"
  ```

---

### 2. `validation`
Verifica el análisis léxico y sintáctico del código fuente, validando que sea un programa válido sin ejecutarlo y sin aplicar reglas de estilo (linting).

- **Sintaxis:**
  ```text
  validation <source_file> [version]
  ```
- **Argumentos:**
  - `<source_file>`: Ruta al archivo `.ps` a validar (obligatorio).
  - `[version]`: Versión del lenguaje (`1.0` o `1.1`, por defecto: `1.0`).
- **Ejemplo en Modo Interactivo:**
  ```text
  CLIPS> validation test.ps 1.0
  ```
- **Ejemplo en Modo Directo:**
  ```powershell
  .\gradlew.bat :cli:run --args="validation test.ps 1.0"
  ```

---

### 3. `analyzer`
Ejecuta validación léxica, sintáctica y **análisis estático de código (linter)** verificando convenciones de estilo mediante un archivo de configuración YAML.

- **Sintaxis:**
  ```text
  analyzer <source_file> <configuration_file> [version]
  ```
- **Argumentos:**
  - `<source_file>`: Ruta al archivo `.ps` a analizar (obligatorio).
  - `<configuration_file>`: Ruta al archivo de reglas de linting `.yaml` (obligatorio).
  - `[version]`: Versión del lenguaje (`1.0` o `1.1`, por defecto: `1.0`).
- **Ejemplo en Modo Interactivo:**
  ```text
  CLIPS> analyzer test.ps analyzer_rules.yaml 1.0
  ```
- **Ejemplo en Modo Directo:**
  ```powershell
  .\gradlew.bat :cli:run --args="analyzer test.ps analyzer_rules.yaml 1.0"
  ```

---

### 4. `formatter`
Aplica reglas de estilo (espacios, saltos de línea, indentaciones) reescribiendo el archivo fuente con el formato especificado en un archivo de configuración YAML.

- **Sintaxis:**
  ```text
  formatter <source_file> <configuration_file> [version]
  ```
- **Argumentos:**
  - `<source_file>`: Ruta al archivo `.ps` a formatear (obligatorio).
  - `<configuration_file>`: Ruta al archivo de reglas de formato `.yaml` (obligatorio).
  - `[version]`: Versión del lenguaje (`1.0` o `1.1`, por defecto: `1.0`).
- **Ejemplo en Modo Interactivo:**
  ```text
  CLIPS> formatter test.ps format_rules.yaml 1.0
  ```
- **Ejemplo en Modo Directo:**
  ```powershell
  .\gradlew.bat :cli:run --args="formatter test.ps format_rules.yaml 1.0"
  ```

---

## 3. Archivos de Prueba y Configuración Existentes

En la raíz del proyecto se encuentran archivos listos para usar en tus pruebas:

### 1. Archivo de código PrintScript (`test.ps`)
```text
let x: number=5;
let name: string="John";
let result: number=x+10;


println(result);


println(name);
let y: number=100;


println(y);
```

### 2. Archivo de reglas de análisis / linter (`analyzer_rules.yaml`)
```yaml
rules:
  identifier_format:
    style: camelCase

  mandatory_variable_or_literal_in_println:
    enabled: true
```

### 3. Archivo de reglas de formateo (`format_rules.yaml`)
```yaml
enforce-spacing-before-colon-in-declaration: false
enforce-spacing-after-colon-in-declaration: true

enforce-no-spacing-around-equals: true

line-breaks-before-println: 2
```

---

## 4. Cómo Salir o Cerrar la CLI

- **En Modo Interactivo:**
  Escribe `exit` (no distingue mayúsculas o minúsculas) y presiona Enter:
  ```text
  CLIPS> exit
  ```
  O también puedes enviar una señal de interrupción presionando **`Ctrl + C`** o **`Ctrl + D`**.
- **En Modo Directo:**
  El proceso se cierra automáticamente una vez finalizado el comando correspondiente. Retorna código de salida `0` en caso de éxito o código distinto de `0` ante errores.

---

## 5. Visualización del Progreso

Cada comando invoca un indicador de progreso (`MultiStepProgress`) con pasos numerados (`[1/3]`, `[1/4]`, etc.) y spinners de consola (`ProgressIndicator`) que notifican el avance de cada fase (análisis léxico, parsing, análisis estático, ejecución o guardado de archivos).
