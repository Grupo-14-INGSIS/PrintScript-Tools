# ?? PrintScript-Tools

Plataforma de compilación, ejecución, formateo y análisis estático para el lenguaje **PrintScript** (versiones 1.0 y 1.1).

---

## ??? Documentación de Arquitectura y Diseño

* ?? **[ARQUITECTURA_MODULAR.md](./ARQUITECTURA_MODULAR.md):** Explicación exhaustiva de la arquitectura modular (8 módulos), justificación de por qué `token` y `ast` son módulos separados (evitando el anti-patrón "Junk Drawer / Bolsa de gatos"), uso de Packages vs Módulos Gradle, lógica de plugins y guía para probar lenguajes no-PrintScript.
* ?? **[DOCUMENTACION.md](./DOCUMENTACION.md):** Documentación técnica detallada de componentes y APIs.

---

## ?? Hooks de Pre-commit

Para activar las verificaciones pre-commit se puede:

1) Correr el comando `git config core.hooksPath .githooks` en la terminal estando parados en la raíz del proyecto.
2) Al correr los tests (se ejecuta una tarea de gradle `./gradlew test`), el archivo se genera/acomoda automáticamente.
