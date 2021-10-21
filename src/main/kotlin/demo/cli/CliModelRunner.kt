package demo.cli

import fraction.FractionBayesianLearner
import utils.component6
import kotlin.math.abs

/**
 * Interactive shell to demo models
 */
class CliModelRunner(val learner: FractionBayesianLearner) {

    private class ExitException() : Exception()

    fun runCliBlocking() {
        println("${learner.javaClass.simpleName} is initialised")
        while (true) {
            print("> ")
            val line = readLine() ?: continue

            try {
                doAction(line)
            } catch (e: ExitException) {
                println("=== end of demo ================")
                break
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    fun doAction(line: String) {
        val line = line.trim()
        if (line.isEmpty() || line.isBlank()) return

        when (line[0]) {
            't', '?' -> {
                val match = Regex("""\w+\s+(\d+)\s+(\d+)\s+(\d+)\s+(\d+).*""").matchEntire(line)
                if (match == null) {
                    println("incorrect format for 'test': $line")
                    return
                }
                val (n1, d1, n2, d2) = match.groupValues.drop(1).map { it.toInt() }
                val (n, d) = learner.predictAnswer(n1, d1, n2, d2)
                println("test: $n1/$d1 + $n2/$d2 -(prediction)-> $n/$d")

                val predictedAnswer = n.toDouble() / d
                val trueAnswer = n1.toDouble() / d1 + n2.toDouble() / d2
                println("true answer: $trueAnswer")
                println("absolute error: ${abs(predictedAnswer - trueAnswer)}")
            }
            in '0'..'9' -> {
                val match = Regex("""(\d+)\s+(\d+)\s+(\d+)\s+(\d+)\s+(\d+)\s+(\d+).*""").matchEntire(line)
                if (match == null) {
                    println("incorrect format for 'example': $line")
                    return
                }
                val (n1, d1, n2, d2, n, d) = match.groupValues.drop(1).map { it.toInt() }
                learner.updateBeliefs(n1, d1, n2, d2, n, d)
                println("example: $n1/$d1 + $n2/$d2 = $n/$d")
            }
            's' -> println("status: ${learner}")
            'e' -> throw ExitException()
        }
    }
}


fun main() {
    val cli = CliModelRunner(learner = fraction.sum.FracSumLearner())
    cli.runCliBlocking()
}