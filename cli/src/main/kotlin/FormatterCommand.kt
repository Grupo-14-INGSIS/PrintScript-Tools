package cli.src.main.kotlin

import formatteraction.src.main.kotlin.FormatterAction

class FormatterCommand : Command {

    override fun execute(args: List<String>) {
        val formatter = FormatterAction()
        formatter.execute(args)
    }
}
