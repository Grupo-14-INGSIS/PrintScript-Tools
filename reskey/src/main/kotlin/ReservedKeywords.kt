package reskey.src.main.kotlin

class ReservedKeywords {
    companion object {

        // Strings que no pueden ser utilizadas para nombrar variables
        private val reservedWords: List<String> = listOf(
            "public",
            "final",
            "let",
            "string",
            "number",
            "println"
        )

        // Caracteres que no pueden estar en ninguna posición del nombre de una variable
        private val forbiddenCharacters: List<Char> = listOf(
            // Aritméticos
            '=', '+', '-', '*', '/', '<', '>',
            // Caracteres especiales
            '\n', '\t',
            // Caracteres de apertura y cierre
            '\'', '"', '¿', '?', '¡', '!', '`', '´',
            '(', ')', '{', '}', '[', ']',
            // Misceláneos
            '.', ',', ':', ';', ' ',
            '#', '$', '%', '&', '@', '|', '\\',
            '^', '~', '¨', '°', '¬'
        )

        // Caracteres con los que no se puede iniciar el nombre de una variable
        private val forbiddenBeginning: List<Char> = listOf(
            '0', '1', '2', '3', '5', '6', '7', '8', '4', '9',
            '_'
        )

        fun nameIsAllowed(varName: String): Boolean {
            // El nombre de la variable no puede estar vacío
            if (varName.isEmpty()) return false
            // El nombre de la variable no puede ser una palabra reservada
            if (reservedWords.contains(varName)) return false
            // El nombre de la variable no puede contener un carácter prohibido
            for (char in varName) if (forbiddenCharacters.contains(char)) return false
            // El nombre de la variable no puede comenzar con un carácter de inicio prohibido
            if (forbiddenBeginning.contains(varName[0])) return false
            // TODO OK
            return true
        }
    }
}
