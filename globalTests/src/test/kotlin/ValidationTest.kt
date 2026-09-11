package globaltests.src.test.kotlin

import globaltests.src.test.kotlin.dsl.printScriptTest
import org.junit.jupiter.api.Test

class ValidationTest {

    @Test
    fun `v1_0 invalid const declaration`() {
        printScriptTest {
            version = "1.0"
            code = """const a: string = "constant declaration should not be allowed in version 1.0";"""
            expectFailure()
        }
    }

    @Test
    fun `v1_0 invalid expression for type`() {
        printScriptTest {
            version = "1.0"
            code = """let pi: number = "hola";"""
            expectFailure()
        }
    }

    @Test
    fun `v1_0 invalid if statement`() {
        printScriptTest {
            version = "1.0"
            code = """
                let a: number = 21;
                if(a) {
                    println("if should not be supported in version 1.0");
                }
            """.trimIndent()
            expectFailure()
        }
    }

    @Test
    fun `v1_0 invalid missing semi colon`() {
        printScriptTest {
            version = "1.0"
            code = "println(5)"
            expectFailure()
        }
    }

    @Test
    fun `v1_0 invalid string arithmetic op`() {
        printScriptTest {
            version = "1.0"
            code = """
                let result: string = "string" * 5;
                println(result);
            """.trimIndent()
            expectFailure()
        }
    }

    @Test
    fun `v1_0 valid arithmetic ops`() {
        printScriptTest {
            version = "1.0"
            code = "let cuenta: number = 5*5-8/4+2;"
            expectSuccess()
        }
    }

    @Test
    fun `v1_1 invalid argument in if`() {
        printScriptTest {
            version = "1.1"
            code = """
                let a: number = 21;
                if(a) {
                    println("this should fail, invalid argument in if statement");
                }
            """.trimIndent()
            expectFailure()
        }
    }

    @Test
    fun `v1_1 invalid const re assign`() {
        printScriptTest {
            version = "1.1"
            code = """
                const b: number = 5;
                b = 2;
            """.trimIndent()
            expectFailure()
        }
    }

    @Test
    fun `v1_1 invalid expression for type`() {
        printScriptTest {
            version = "1.1"
            code = """let pi: number = "hola";"""
            expectFailure()
        }
    }

    @Test
    fun `v1_1 invalid missing semi colon`() {
        printScriptTest {
            version = "1.1"
            code = "println(5)"
            expectFailure()
        }
    }

    @Test
    fun `v1_1 invalid string arithmetic op`() {
        printScriptTest {
            version = "1.1"
            code = """
                let result: string = "string" * 5;
                println(result);
            """.trimIndent()
            expectFailure()
        }
    }

    @Test
    fun `v1_1 valid arithmetic ops`() {
        printScriptTest {
            version = "1.1"
            code = "let cuenta: number = 5*5-8/4+2;"
            expectSuccess()
        }
    }

    @Test
    fun `v1_1 valid const declaration`() {
        printScriptTest {
            version = "1.1"
            code = """const b: string = "this should be valid in 1.1";"""
            expectSuccess()
        }
    }
}



