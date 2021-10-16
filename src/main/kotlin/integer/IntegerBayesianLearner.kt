package integer

import utils.chooseRandomIndex

/**
 * [name] is a human-readable hypothesis description
 * [probability] is a probabilistic distribution (x, y, ans) -> probability
 * */
class Hypothesis(val name: String, val probability: (Int, Int, Int) -> Double)

/**
 * [IntegerBayesianLearner] learns to distinguish between [hypotheses]
 * by updating its [beliefs] when [updateBeliefs] is called with a new example.
 */
class IntegerBayesianLearner(
    private val hypotheses: List<Hypothesis>,
    private val beliefs: MutableList<Double>
) : IntegerLearner {

    override fun updateBeliefs(x: Int, y: Int, ans: Int) {
        val newBeliefs = hypotheses.zip(beliefs).map { (hyp, prob) -> hyp.probability(x, y, ans) * prob }
        val newBeliefsSum = newBeliefs.sum()  // Normalisation factor

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
        return "${this.javaClass.simpleName}\n" +
                hypotheses.zip(beliefs).joinToString(separator = "\n", postfix = "\n") { (hypothesis, probability) ->
                    "${hypothesis.name}: $probability"
                }
    }
}