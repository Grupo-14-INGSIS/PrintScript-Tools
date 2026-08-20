package cli.src.main.kotlin.command

import formatteraction.src.main.kotlin.FormatterAction

class FormatterCommand(
    private val formatterAction: FormatterAction = FormatterAction()
) : CliCommand {
    override val name: String = "formatter"
    override val description: String = "Format a PrintScript source file according to rules"
    override fun execute(args: List<String>) = formatterAction.execute(args)
}
