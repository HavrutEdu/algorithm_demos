package demo.text.cli

import demo.text.TextIOModelRunner
import fraction.FractionBayesianLearner
import fraction.FractionLearner
import utils.component6
import kotlin.math.abs


sealed class Actions : TextIOModelRunner.Action() {
    class Exit() : Actions() {
        override val matchers = listOf(Regex("""\s*e\s*"""), Regex("""\s*exit\s*"""))
        override val examples = listOf("exit", "  e", "exit ")

        override fun performAction(parsed: MatchResult) = throw TextIOModelRunner.Action.ExitSignalException()
    }

    class PrintStatus(val model: FractionLearner) : Actions() {
        override val matchers = listOf(Regex("""\s*s\s*"""), Regex("""\s*status\s*"""))
        override val examples = listOf("status ", "  s")

        override fun performAction(parsed: MatchResult) {
            println("status: ${model}")
        }
    }

    class GiveExample(val model: FractionLearner) : Actions() {
        override val matchers = listOf(Regex("""(?:e|e:|example:)?\s*$frac${sep("+")}$frac${sep("=")}$frac\s*"""))
        override val examples = listOf(" 1/2 + 1/3 = 5/6", "example: 1 2 1 3 5 6", "e 1/2+1 3= 5 6")

        override fun performAction(parsed: MatchResult) {
            val (n1, d1, n2, d2, n, d) = parsed.groupValues.drop(1).map { it.toInt() }  // pars error
            model.updateBeliefs(n1, d1, n2, d2, n, d)
            println("example: $n1/$d1 + $n2/$d2 = $n/$d")
        }
    }

    class TestModel(val model: FractionLearner) : Actions() {
        override val matchers = listOf(
            Regex("""(?:t|t:|test:|\?)\s*$frac${sep("+")}$frac${sep("=")}$frac\s*"""),
            Regex("""(?:t|t:|test:|\?)?\s*$frac${sep("+")}$frac${sep("=")}$frac\s*\?"""),
        )
        override val examples = listOf("t 1/2 + 1/3 = 5/6", "test:1 2 1 3 5 6", "1/2+1 3= 5 6?")

        override fun performAction(parsed: MatchResult) {
            val (n1, d1, n2, d2) = parsed.groupValues.drop(1).map { it.toInt() } // pars error
            val (n, d) = model.predictAnswer(n1, d1, n2, d2)
            println("test: $n1/$d1 + $n2/$d2 -(prediction)-> $n/$d")

            val predictedAnswer = n.toDouble() / d
            val trueAnswer = n1.toDouble() / d1 + n2.toDouble() / d2
            println("true answer: $trueAnswer")
            println("|$predictedAnswer - $trueAnswer| = ${abs(predictedAnswer - trueAnswer)}")
        }
    }

    /**
     * Utility functions to represent RegEx
     */
    internal fun sep(optionalChars: String): String = """(?:\s+|\s*[$optionalChars]\s*)"""
    internal val frac = """(\d+)${sep("""\\""")}(\d+)"""
}


/**
 * Interactive shell to demo [model]
 * Possible actions are described in [Actions]
 */
class CliModelRunner(val model: FractionBayesianLearner) {
    private val runner = TextIOModelRunner(
        Actions.Exit(), Actions.TestModel(model),
        Actions.PrintStatus(model), Actions.GiveExample(model)
    )

    fun runCliBlocking() {
        println("${model.javaClass.simpleName} is initialised")
        while (true) {
            print("> ")
            val line = readLine() ?: continue

            try {
                runner.performNextAction(line)
            } catch (e: TextIOModelRunner.Action.ExitSignalException) {
                println("=== end of demo ================")
                break
            } catch (e: TextIOModelRunner.Action.ParsingException) {
                println(e)
            } catch (e: Exception) {
                println(e)
            }
        }
    }
}


fun main() {
    val cli = CliModelRunner(model = fraction.sum.FracSumLearner())
    cli.runCliBlocking()
}