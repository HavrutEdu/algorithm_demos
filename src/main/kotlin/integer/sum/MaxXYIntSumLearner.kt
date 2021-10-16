package integer.sum

import integer.Hypothesis
import integer.IntegerBayesianLearner
import kotlin.math.abs
import kotlin.math.pow

fun correctSumUpToNHypothesis(n: Int, alpha: Double = 0.9): Hypothesis =
    Hypothesis("Correct when x, y in 0..$n") { x: Int, y: Int, sum: Int ->
        val trueSum = x + y
        if (x in 0..n && y in 0..n) {
            if (sum == trueSum) 1.0 else 0.0
        } else {
            // Using two-sided geometric distribution, see
            // https://mathoverflow.net/questions/213221/what-is-a-two-sided-geometric-distribution
            (1 - alpha) / (1 + alpha) * alpha.pow(abs(trueSum - sum))
        }
    }

fun MaxXYIntSumLearner(pivotValues: List<Int>): IntegerBayesianLearner {
    val hypotheses: List<Hypothesis> = pivotValues.map { n -> correctSumUpToNHypothesis(n) }
    val beliefs: MutableList<Double> = MutableList(hypotheses.size) { i -> 1.0 / hypotheses.size }

    return IntegerBayesianLearner(hypotheses, beliefs)
}

fun main() {
    val learner = MaxXYIntSumLearner(pivotValues = listOf(0, 1, 2, 5, 10, 20, 30, Int.MAX_VALUE))
    println("Initial beliefs: " + learner)

    learner.updateBeliefs(0, 0, 0)
    println(learner)

    learner.updateBeliefs(1, 2, 3)
    println(learner)
}