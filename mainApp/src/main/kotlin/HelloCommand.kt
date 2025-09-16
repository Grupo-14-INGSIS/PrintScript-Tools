import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Command(name = "hello", description = ["aaa"])
class HelloCommand : Runnable {

    @Option(names = ["-n", "--name"], description = ["Nombre a saludar"])
    var name: String = "mundo"

    override fun run() {
        println("Hola, $name!")
    }
}
