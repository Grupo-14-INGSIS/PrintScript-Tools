package formatter.src.test.kotlin

import common.src.main.kotlin.DataType
import formatter.src.main.kotlin.Formatter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class FormatterTest {

    @Test
    fun onlyMandatoryTest() {
        // Testing only mandatory rules
        val source = "let    x:   string    =    \"Hi\"  ;"
        val configFile = "C:\\Users\\santi\\faculty\\INGSIS\\PrintScript-Tools\\formatter\\src\\test\\resources\\test1rules.yaml"
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
            DataType.SEMICOLON
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
        val configFile = "C:\\Users\\santi\\faculty\\INGSIS\\PrintScript-Tools\\formatter\\src\\test\\resources\\test2rules.yaml"
        val formatter = Formatter(source, configFile)
        val types = listOf(
            DataType.LET_KEYWORD,
            DataType.SPACE,
            DataType.IDENTIFIER,
            DataType.COLON,
            DataType.NUMBER_TYPE,
            DataType.ASSIGNATION,
            DataType.NUMBER_LITERAL,
            DataType.SEMICOLON
        )
        val actual = formatter.execute()
        assertEquals(types.size, actual.size())
        for (i in types.indices) {
            assertEquals(types[i], actual.get(i)!!.type)
        }
    }

}
