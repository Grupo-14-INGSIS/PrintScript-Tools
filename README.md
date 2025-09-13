Para activar las verificaciones pre-commit se puede:

1) Correr el comando " git config core.hooksPath .githooks " en la terminal estando parados en la raiz del pryecto. 
2) Al correr los tests (se ejecuta una tarea de gradle, el ./gradlew test) el archivo se genera/acomodoara automatico.

Asi el hook del pre commit estara disponiblie ;)

La tercera opcion es con Husky, pero seria necesario correr npm install --> no hay gran difernecia respecto con el primer caso. 
No se encuentra disponible. 
