package formatter.src.test.kotlin

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import formatter.src.main.kotlin.Formatter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URL

class FormatterTest {

    @Test
    fun onlyMandatoryTest() {
        // Testing only mandatory rules
        val source = "let    x:   string    =    \"Hi\"  ;"
        // val configFile = "C:\\Users\\laris\\Downloads\\PrintScript-Tools\\formatter\\src\\test\\resources\\test1rules.yaml"
        val configFile: URL = this::class.java.getResource("/test1rules.yaml") ?: error("Archivo de configuración no encontrado")
        val formatter = Formatter(source, configFile)
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
        val actual = formatter.execute()
        assertEquals(types.size, actual.size())
        for (i in types.indices) {
            assertEquals(types[i], actual.get(i)!!.type)
        }
    }

    @Test
    fun optionalTest() {
        // Testing all rules
        val source = "let  x  :  number = 14;"
        val configFile: URL = this::class.java.getResource("/test2rules.yaml") ?: error("Archivo de configuración no encontrado")
        val formatter = Formatter(source, configFile)
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
        val actual: Container = formatter.execute()
        assertEquals(types.size, actual.size())
        for (i in types.indices) {
            assertEquals(types[i], actual.get(i)!!.type)
        }
    }
}
