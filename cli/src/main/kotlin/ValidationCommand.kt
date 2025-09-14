package cli.src.main.kotlin

import validator.src.main.kotlin.Validator

class ValidationCommand : Command {
    override fun execute(args: List<String>) {
        val validator = Validator()
        validator.execute(args)
    }
}
