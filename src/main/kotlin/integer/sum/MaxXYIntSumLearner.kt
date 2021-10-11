package integer.sum

import utils.chooseRandomIndex
import kotlin.math.abs
import kotlin.math.pow

// (x, y, sum) -> probability
typealias ProbableSumHypothesis = (x: Int, y: Int, sum: Int) -> Double

fun correctSumUpToNHypothesis(n: Int, alpha: Double = 0.9): ProbableSumHypothesis =
    fun(x: Int, y: Int, sum: Int): Double {
        val trueSum = x + y
        if (x in 0..n && y in 0..n) {
            return if (sum == trueSum) 1.0 else 0.0
        } else {
            return (1 - alpha) / (1 + alpha) * alpha.pow(abs(trueSum - sum))
        }
    }

class MaxXYIntSumLearner(val pivotValues: List<Int>) : IntegerSumLearner {
    val hypotheses: List<ProbableSumHypothesis> = pivotValues.map { n -> correctSumUpToNHypothesis(n) }
    val beliefs: MutableList<Double> = MutableList(hypotheses.size) { i -> 1.0 / hypotheses.size }

    override fun updateBeliefs(x: Int, y: Int, ans: Int) {
        val newBeliefs = hypotheses.zip(beliefs).map { (hyp, prob) -> hyp(x, y, ans) * prob }
        val newBeliefsSum = newBeliefs.sum()

        for (i in beliefs.indices)
            beliefs[i] = newBeliefs[i] / newBeliefsSum
    }

    override fun predictAnswer(x: Int, y: Int): Int {
        val selectHypothesis = hypotheses[chooseRandomIndex(beliefs)]
        val reasonableAnswers = (0..100).toList()
        val answerProbabilities = reasonableAnswers.map { ans -> selectHypothesis(x, y, ans) }
        return reasonableAnswers[chooseRandomIndex(answerProbabilities)]
    }
}

fun main() {
    val learner = MaxXYIntSumLearner(pivotValues = listOf(0, 1, 2, 5, 10, 20, 30, Int.MAX_VALUE))
    println("Initial beliefs: " + learner.beliefs)

    learner.updateBeliefs(0, 0, 0)
    println(learner.beliefs)

    learner.updateBeliefs(1, 2, 3)
    println(learner.beliefs)
}