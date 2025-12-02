package interpreter

import ast.src.main.kotlin.ASTNode
import interpreter.src.main.kotlin.Interpreter
import interpreter.src.main.kotlin.VarDeclaration
import tokendata.src.main.kotlin.Position
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class VarDeclarationTest {

    @MockK(relaxed = true)
    lateinit var interpreter: Interpreter

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    private fun node(content: String, children: List<ASTNode> = emptyList()): ASTNode =
        ASTNode(type = null, content = content, position = Position(0, 0), children = children)

    private fun declNode(name: String, type: String): ASTNode =
        node("var", listOf(node(name), node(type)))

    @Test
    fun `interpret returns Unit and declares number`() {
        val node = declNode("x", "number")
        val result = VarDeclaration.interpret(node, interpreter)

        assertEquals(Unit, result)
        verify(exactly = 1) { interpreter.declareVariable("x", 0.0, "number") }
    }

    @Test
    fun `declares string boolean and unknown`() {
        VarDeclaration.interpret(declNode("a", "string"), interpreter)
        VarDeclaration.interpret(declNode("b", "boolean"), interpreter)
        VarDeclaration.interpret(declNode("c", "Custom"), interpreter)

        verify { interpreter.declareVariable("a", "", "string") }
        verify { interpreter.declareVariable("b", false, "boolean") }
        verify { interpreter.declareVariable("c", null, "Custom") }
    }

    @Test
    fun `throws IllegalArgumentException when children are missing`() {
        val node0 = node("var") // sin hijos
        val ex0 = kotlin.test.assertFailsWith<IllegalArgumentException> {
            VarDeclaration.interpret(node0, interpreter)
        }
        assertEquals("Declaración inválida: faltan argumentos", ex0.message)
    }
}
