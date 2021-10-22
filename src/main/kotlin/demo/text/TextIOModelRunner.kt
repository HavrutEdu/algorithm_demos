package demo.text


class TextIOModelRunner(private val actions: Collection<Action>) {
    abstract class Action {
        abstract val matchers: List<Regex>
        abstract val examples: List<String>

        open fun matches(line: String): MatchResult? = this.matchers.firstNotNullOfOrNull { it.matchEntire(line) }
        abstract fun performAction(parsed: MatchResult)

        class ExitSignalException() : Exception()
        class ParsingException(val line: String, val msg: String, val action: Action? = null) :
            Exception(/*message=*/(action?.javaClass?.simpleName?.let { "[$it]" } ?: "") + "$msg: `$line`")
    }

    @Throws(Action.ExitSignalException::class, Action.ParsingException::class)
    fun performNextAction(line: String) {
        if (line.isBlank()) return
        val (action, match) = actions.firstNotNullOfOrNull { action: Action ->
            action.matches(line)?.let { match -> Pair(action, match) }
        } ?: throw Action.ParsingException(line, "unknown input format")

        action.performAction(match)
    }
}

