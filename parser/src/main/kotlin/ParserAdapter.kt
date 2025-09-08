package parser.src.main.kotlin

import interpreter.*
import java.io.InputStream

class ParserAdapter : PrintScriptInterpreter {

    override fun execute(
        src: InputStream,
        version: String,
        emitter: PrintEmitter,
        handler: ErrorHandler,
        provider: InputProvider
    ) {
        val sourceCode = src.bufferedReader().readText()

//        val parser = when (version) {
//            "1.0" -> ParserV1()
//            "1.1" -> ParserV1_1()
//            else -> {
//                handler.handle("Unsupported version: $version")
//                return
//            }
//        }
//
//        try {
//            val program = parser.parse(sourceCode)
//            val executor = Executor(emitter, handler, provider)
//            executor.run(program)
//        } catch (e: Exception) {
//            handler.handle("Execution failed: ${e.message}")
//        }
    }
}
