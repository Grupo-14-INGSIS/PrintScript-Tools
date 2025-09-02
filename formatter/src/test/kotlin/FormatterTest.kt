package formatter.src.test.kotlin

import common.src.main.kotlin.DataType
import formatter.src.main.kotlin.Formatter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FormatterTest {

    val source = "let   x: string     =    \"Hello, World!\";"
    val configFile = "C:\\Users\\santi\\faculty\\INGSIS\\PrintScript-Tools\\formatter\\src\\test\\resources\\format_rules.yaml"

    @Test
    fun basicTest1() {
        val formatter = Formatter(source, configFile)
        print("Init formatter")
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
            DataType.SEMICOLON
        )
        print("init types list")
        val actual = formatter.execute()
        print("executed formatter")
        assertEquals(types.size, actual.size())
        if (actual.size() > 0) {
            for (i in types.indices) {
                assertEquals(types[i], actual.get(i))
            }
        }
    }

    @Test
    fun basicTest2() {

    }

}
