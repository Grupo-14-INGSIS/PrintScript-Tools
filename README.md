# ?? PrintScript-Tools

Plataforma de compilaci�n, ejecuci�n, formateo y an�lisis est�tico para el lenguaje **PrintScript** (versiones 1.0 y 1.1).

---

## ??? Documentaci�n de Arquitectura y Dise�o

* ?? **[ARQUITECTURA_MODULAR.md](./ARQUITECTURA_MODULAR.md):** Explicaci�n exhaustiva de la arquitectura modular (8 m�dulos), justificaci�n de por qu� `token` y `ast` son m�dulos separados (evitando el anti-patr�n "Junk Drawer / Bolsa de gatos"), uso de Packages vs M�dulos Gradle, l�gica de plugins y gu�a para probar lenguajes no-PrintScript.
* ?? **[DOCUMENTACION.md](./DOCUMENTACION.md):** Documentaci�n t�cnica detallada de componentes y APIs.

---

## ?? Hooks de Pre-commit

Para activar las verificaciones pre-commit se puede:

1) Correr el comando `git config core.hooksPath .githooks` en la terminal estando parados en la ra�z del proyecto.
2) Al correr los tests (se ejecuta una tarea de gradle `./gradlew test`), el archivo se genera/acomoda autom�ticamente.
