package integer

import utils.chooseRandomIndex

/**
 * [name] is a human-readable hypothesis description
 * [hypothesisProbability] is a mutable internal state which is updated based on learning examples
 * [probabilityFunction] is a probabilistic distribution (x, y, ans) -> probability
 *
 * Note that [hypothesisProbability] and [probabilityFunction] *don't* have to be normalised,
 * e.i. their integral sum can be any positive number.
 * */
class Hypothesis(
    val name: String,
    var hypothesisProbability: Double,
    val probabilityFunction: (Int, Int, Int) -> Double
) {
    override fun toString() = "Hypothesis(p=$hypothesisProbability): $name"
}

/**
 * [IntegerBayesianLearner] learns to distinguish between [hypotheses]
 * by updating internal [Hypothesis.hypothesisProbability] when [updateBeliefs] is called with a new example.
 */
class IntegerBayesianLearner(private val hypotheses: List<Hypothesis>) : IntegerLearner {

    override fun updateBeliefs(x: Int, y: Int, ans: Int) {
        hypotheses.forEach { hyp -> hyp.hypothesisProbability *= hyp.probabilityFunction(x, y, ans) }

        // Optional normalisation
        val normalisationFactor = hypotheses.sumOf { it.hypothesisProbability }
        hypotheses.forEach { hyp -> hyp.hypothesisProbability /= normalisationFactor }
    }

    override fun predictAnswer(x: Int, y: Int): Int {
        val selectHypothesis = hypotheses[chooseRandomIndex(hypotheses.map { it.hypothesisProbability })]
        val reasonableAnswers = (0..100).toList()
        val answerProbabilities = reasonableAnswers.map { ans -> selectHypothesis.probabilityFunction(x, y, ans) }
        return reasonableAnswers[chooseRandomIndex(answerProbabilities)]
    }

    override fun toString(): String {
        return "${this.javaClass.simpleName}\n" +
                hypotheses.joinToString(separator = "\n", postfix = "\n")
    }
}