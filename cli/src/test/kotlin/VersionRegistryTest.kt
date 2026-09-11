package cli.src.test.kotlin

import cli.src.main.kotlin.version.PrintScriptVersion
import cli.src.main.kotlin.version.Version11
import cli.src.main.kotlin.version.VersionRegistry
import executor.Executor
import parser.src.main.kotlin.VersionFeatures
import lexer.src.main.kotlin.TokenPlugin
import lexer.src.main.kotlin.TokenPluginFactory
import interpreter.src.main.kotlin.Actions
import interpreter.src.main.kotlin.ActionType
import inputprovider.src.main.kotlin.InputProvider
import interpreter.src.main.kotlin.Interpreter
import tokendata.src.main.kotlin.DataType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class VersionRegistryTest {

    private val originalOut = System.out
    private lateinit var outputStream: ByteArrayOutputStream

    @BeforeEach
    fun setUp() {
        outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))
    }

    @AfterEach
    fun tearDown() {
        System.setOut(originalOut)
    }

    @Test
    fun `default versions 1_0 and 1_1 are registered`() {
        assertTrue(VersionRegistry.isSupported("1.0"))
        assertTrue(VersionRegistry.isSupported("1.1"))
        assertFalse(VersionRegistry.isSupported("9.9"))

        val v10 = VersionRegistry.get("1.0")
        assertNotNull(v10)
        assertFalse(v10.supportsBlocks)

        val v11 = VersionRegistry.get("1.1")
        assertNotNull(v11)
        assertTrue(v11.supportsBlocks)
    }

    @Test
    fun `can dynamically register a new version 1_2 without modifying engine classes`() {
        assertFalse(VersionRegistry.isSupported("1.2"))

        // Definimos la version 1.2 como una nueva especificacion
        val version1_2 = object : PrintScriptVersion {
            override val name: String = "1.2"
            override val features: VersionFeatures = Version11.features
            override val keywords: Map<String, DataType> = Version11.keywords + ("loop" to DataType.IDENTIFIER)
            override val supportedActions: Set<Actions> = Version11.supportedActions

            override fun createTokenPlugins(): List<TokenPlugin> =
                TokenPluginFactory.createPlugins(keywords)

            override fun createActionHandlers(inputProvider: InputProvider?): Map<Actions, ActionType> =
                Interpreter.defaultV10Handlers + Interpreter.createV11Handlers(inputProvider)
        }

        // Registramos la 1.2
        VersionRegistry.register(version1_2)

        // Verificamos que ahora es soportada
        assertTrue(VersionRegistry.isSupported("1.2"))

        // Verificamos que el Executor reconoce la 1.2 sin error de version no soportada
        val tempFile = File.createTempFile("test_v12", ".ps")
        tempFile.writeText("let x: number = 42;")
        try {
            Executor().execute(tempFile, "1.2")
            val output = outputStream.toString()
            assertFalse(output.contains("Unsupported version"))
            assertTrue(output.contains("Starting execution of '${tempFile.path}' with PrintScript 1.2"))
        } finally {
            tempFile.delete()
        }
    }
}
