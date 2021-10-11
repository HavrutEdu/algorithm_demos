package utils

import kotlin.random.Random

/**
 * Chooses a random integer from [weights.indices] according to the [weights] values.
 */
fun chooseRandomIndex(weights: List<Double>): Int {
    val rand = Random.nextDouble(weights.sum())  // rand < weights.sum()

    var acum = 0.0
    for (i in weights.indices) {
        acum += weights[i]
        if (rand < acum)
            return i
    }

    throw IllegalStateException("ACUM $acum cannot be equal to SUM ${weights.sum()} and be less than RAND $rand")
}