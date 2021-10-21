package fraction.sum

import fraction.FractionBayesianLearner
import fraction.Hypothesis
import kotlin.math.exp
import kotlin.math.pow

fun fracSum(num1: Int, den1: Int, num2: Int, den2: Int): Pair<Int, Int> =
    if (den1 == den2) {
        Pair(num1 + num2, den1)
    } else {
        Pair(num1 * den2 + num2 * den1, den1 * den2)
    }

fun trueSum(): Hypothesis =
    Hypothesis("true sum", hypothesisProbability = 1.0) { num1, den1, num2, den2, num, den ->
        val (trueNum, trueDen) = fracSum(num1, den1, num2, den2)

        if (num == trueNum && den == trueDen) {
            1.0
        } else {
            0.0
        }
    }

fun wrongAnswerValueBy(delta: Int): Hypothesis =
    Hypothesis("Answer values + $delta", hypothesisProbability = 1.0) { num1, den1, num2, den2, num, den ->
        val (trueNum, trueDen) = fracSum(num1, den1, num2, den2)

        if (num == trueNum + delta || den == trueDen + delta) {
            1.0
        } else {
            0.0
        }
    }

fun approximateAnswerExponential(scale: Double, maxDensity: Double, shift: Double = 0.0): Hypothesis =
    Hypothesis("truth + ($shift)", hypothesisProbability = 1.0) { num1, den1, num2, den2, num, den ->
        val (trueNum, trueDen) = fracSum(num1, den1, num2, den2)

        if (den != 0) {
            val trueFrac = trueNum.toDouble() / trueDen + shift
            val receivedFrac = num.toDouble() / den
            exp(-((receivedFrac - trueFrac) * scale).pow(2)) * maxDensity
        } else {
            0.0
        }
    }


fun FracSumLearner(): FractionBayesianLearner {
    val hypotheses = listOf<Hypothesis>() +
            //trueSum() +
            approximateAnswerExponential(10.0, 1.0) +
            (-15..15 step 3).map { i -> approximateAnswerExponential(3.0, 0.3, shift = i / 10.0) }

    return FractionBayesianLearner(hypotheses)
}

fun main() {
    val learner = FracSumLearner()
    println("Initial beliefs: " + learner)

    learner.updateBeliefs(1, 1, 1, 1, 2, 1)
    println(learner)

    learner.updateBeliefs(1, 2, 1, 2, 2, 2)
    println(learner)

    learner.updateBeliefs(1, 3, 3, 4, 17, 12)
    println(learner)
}