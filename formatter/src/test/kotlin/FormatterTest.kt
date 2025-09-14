package formatter.src.test.kotlin

import token.src.main.kotlin.Token
import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import formatter.src.main.kotlin.Formatter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URL

class FormatterTest {

    @Test
    fun onlyMandatoryTest() {
        // Testing only mandatory rules
        // val source = "let    x:   string    =    \"Hi\"  ;"
        // val configFile = "C:\\Users\\laris\\Downloads\\PrintScript-Tools\\formatter\\src\\test\\resources\\test1rules.yaml"
        val configFile: URL = this::class.java.getResource("/test1rules.yaml") ?: error("Archivo de configuración no encontrado")
        val formatter = Formatter()
        val source = Container(
            listOf(
                Token(DataType.LET_KEYWORD, "let", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.IDENTIFIER, "x", Position(0, 0)),
                Token(DataType.COLON, ":", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.STRING_TYPE, "string", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.ASSIGNATION, "=", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.STRING_LITERAL, "Hi", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.SEMICOLON, ";", Position(0, 0))
            )
        )
        val types = listOf(
            DataType.LET_KEYWORD,
            DataType.SPACE,
            DataType.IDENTIFIER,
            DataType.COLON,
            DataType.SPACE,
            DataType.STRING_TYPE,
            DataType.SPACE,
            DataType.ASSIGNATION,
            DataType.SPACE,
            DataType.STRING_LITERAL,
            DataType.SPACE,
            DataType.SEMICOLON,
            DataType.LINE_BREAK
        )
        val actual = formatter.execute(source, configFile)
        assertEquals(types.size, actual.size())
        for (i in types.indices) {
            assertEquals(types[i], actual.get(i)!!.type)
        }
    }

    @Test
    fun optionalTest() {
        // Testing all rules
        // val source = "let  x  :  number = 14;"
        val configFile: URL = this::class.java.getResource("/test2rules.yaml") ?: error("Archivo de configuración no encontrado")
        val formatter = Formatter()
        val source = Container(
            listOf(
                Token(DataType.LET_KEYWORD, "let", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.IDENTIFIER, "x", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.COLON, ":", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.NUMBER_TYPE, "number", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.ASSIGNATION, "=", Position(0, 0)),
                Token(DataType.SPACE, " ", Position(0, 0)),
                Token(DataType.NUMBER_LITERAL, "14", Position(0, 0)),
                Token(DataType.SEMICOLON, ";", Position(0, 0))
            )
        )
        val types = listOf(
            DataType.LET_KEYWORD,
            DataType.SPACE,
            DataType.IDENTIFIER,
            DataType.COLON,
            DataType.NUMBER_TYPE,
            DataType.ASSIGNATION,
            DataType.NUMBER_LITERAL,
            DataType.SEMICOLON,
            DataType.LINE_BREAK
        )
        val actual: Container = formatter.execute(source, configFile)
        assertEquals(types.size, actual.size())
        for (i in types.indices) {
            assertEquals(types[i], actual.get(i)!!.type)
        }
    }
}
