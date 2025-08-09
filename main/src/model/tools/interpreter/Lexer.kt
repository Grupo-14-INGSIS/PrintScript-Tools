package model.tools.interpreter

import model.structure.Container
import model.structure.Token
import model.structure.TokenMap
import model.structure.TokenPattern

class Lexer(val input: String) { //conviene que no se pueda reasignar el input, es lo que recibo y no va a variar

    val list = mutableListOf<String>(); //evito caer en primitive obsession
    var newPiece = ""
    var enLiteral = false

    fun splitString() {
        for (c in input) {
            if (c == '"' || c == '\'') {
                enLiteral = !enLiteral // alterna entre entrar y salir del literal
                newPiece += c

                if (!enLiteral) {
                    list.add(newPiece)
                    newPiece = ""
                }

            } else if (enLiteral) {
                newPiece += c // sigue agregando dentro del literal (cuando esta leyendo un string)

            } else if (c == ' ' || c == ';' || c == ':' || c == '\n' || c == '+' || c == '-' || c == '*' || c == '/' || c == '=' || c == '(' || c == ')') { //estos son los separadores
                // Agregar la pieza actual si no está vacía
                if (newPiece.isNotEmpty()) {
                    list.add(newPiece)
                    newPiece = ""
                }

                // Agregar el separador como token independiente
                list.add(c.toString())

            } else {
                newPiece += c // agregar el carácter a la pieza actual
            }
        }
        // Agregar lo que quedó al final
        if (newPiece.isNotEmpty()) {
            list.add(newPiece)
        }
    }

    fun createToken(list: List<String>) : Container {

        var container = Container()
        var position = 0 //es el identificador de el token, no la posicionde cada caracter, por eso no uso POsition
        //no nos sirve UUID como identificador de posicion

        for(piece in list){
            if (piece.isNotEmpty()) { // filtrar strings vacíos
                val type = TokenMap.classifyTokenMap(piece) ?: TokenPattern.classifyTokenPattern(piece) //si el rpimero no devuelve nada, lo completa el segundo el tipo
                val token = Token(type, piece, position)
                container.addContainer(token)
                position++
            }
        }

        return container
    }
}




//generar lista de palabras separando por espacios
//tomar cada elemento y clasificarlo generanodo un token y guardarlo en container