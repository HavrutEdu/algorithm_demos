package fraction

import utils.chooseRandomIndex

/**
 * [name] is a human-readable hypothesis description
 * [hypothesisProbability] is a mutable internal state which is updated based on learning examples
 * [probabilityFunction] is a probabilistic distribution (num1, den1, num2, den2, num, den) -> probability
 *
 * Note that [hypothesisProbability] and [probabilityFunction] *don't* have to be normalised,
 * e.i. their integral sum can be any positive number.
 * */
class Hypothesis(
    val name: String,
    var hypothesisProbability: Double,
    val probabilityFunction: (Int, Int, Int, Int, Int, Int) -> Double
) {
    override fun toString() = "Hypothesis(p=$hypothesisProbability): $name"
}

/**
 * Learns to distinguish between [hypotheses]
 * by updating internal [Hypothesis.hypothesisProbability]
 * when [updateBeliefs] is called with a new example.
 */
class FractionBayesianLearner(private val hypotheses: List<Hypothesis>) : FractionLearner {

    override fun updateBeliefs(num1: Int, den1: Int, num2: Int, den2: Int, num: Int, den: Int) {
        hypotheses.forEach { hyp ->
            hyp.hypothesisProbability *= hyp.probabilityFunction(num1, den1, num2, den2, num, den)
        }

        // Optional normalisation
        val normalisationFactor = hypotheses.sumOf { it.hypothesisProbability }
        hypotheses.forEach { hyp -> hyp.hypothesisProbability /= normalisationFactor }
    }

    override fun predictAnswer(num1: Int, den1: Int, num2: Int, den2: Int): Pair<Int, Int> {
        val selectHypothesis = hypotheses[chooseRandomIndex(hypotheses.map { it.hypothesisProbability })]
        val reasonableAnswers = (0..20).zip(1..10)
        val answerProbabilities = reasonableAnswers.map { (num, den) ->
            selectHypothesis.probabilityFunction(num1, den1, num2, den2, num, den)
        }
        return reasonableAnswers[chooseRandomIndex(answerProbabilities)]
    }

    override fun toString(): String {
        return "${this.javaClass.simpleName}\n" +
                hypotheses.joinToString(separator = "\n", postfix = "\n")
    }
}