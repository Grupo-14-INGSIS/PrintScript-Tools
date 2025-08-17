package src.main.model.tools.interpreter.lexer

import java.io.File

class FileReader {

    fun processFile(file: File, lotSize: Int = 8192, processor: (Char) -> Unit){
        file.bufferedReader().use{ reader ->
            val buffer = CharArray(lotSize)
            var charsRead: Int

            while(reader.read(buffer).also { charsRead = it} != -1){
                for(i in 0 until charsRead){
                    processor(buffer[i])
                }
            }
        }
    }

    fun readAsString(file: File): String = file.readText();
}