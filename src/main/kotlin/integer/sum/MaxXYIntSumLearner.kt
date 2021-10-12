package integer.sum

import utils.chooseRandomIndex
import kotlin.math.abs
import kotlin.math.pow

/**
 * [name] is a human-readable hypothesis description
 * [probability] is a probabilistic distribution (x, y, sum) -> probability
 * */
class Hypothesis(val name: String, val probability: (Int, Int, Int) -> Double)

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

class MaxXYIntSumLearner(val pivotValues: List<Int>) : IntegerSumLearner {
    val hypotheses: List<Hypothesis> = pivotValues.map { n -> correctSumUpToNHypothesis(n) }
    val beliefs: MutableList<Double> = MutableList(hypotheses.size) { i -> 1.0 / hypotheses.size }

    override fun updateBeliefs(x: Int, y: Int, ans: Int) {
        val newBeliefs = hypotheses.zip(beliefs).map { (hyp, prob) -> hyp.probability(x, y, ans) * prob }
        val newBeliefsSum = newBeliefs.sum()

        for (i in beliefs.indices)
            beliefs[i] = newBeliefs[i] / newBeliefsSum
    }

    override fun predictAnswer(x: Int, y: Int): Int {
        val selectHypothesis = hypotheses[chooseRandomIndex(beliefs)]
        val reasonableAnswers = (0..100).toList()
        val answerProbabilities = reasonableAnswers.map { ans -> selectHypothesis.probability(x, y, ans) }
        return reasonableAnswers[chooseRandomIndex(answerProbabilities)]
    }

    override fun toString(): String {
        return "${this.javaClass.simpleName}: pivotValues=$pivotValues\n" +
                hypotheses.zip(beliefs).joinToString(separator = "\n", postfix = "\n") { (hypothesis, probability) ->
                    "${hypothesis.name}: $probability"
                }
    }
}

fun main() {
    val learner = MaxXYIntSumLearner(pivotValues = listOf(0, 1, 2, 5, 10, 20, 30, Int.MAX_VALUE))
    println("Initial beliefs: " + learner)

    learner.updateBeliefs(0, 0, 0)
    println(learner)

    learner.updateBeliefs(1, 2, 3)
    println(learner)
}