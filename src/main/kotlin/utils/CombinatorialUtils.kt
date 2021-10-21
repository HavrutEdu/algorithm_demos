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


/**
 * Generates a sequence of all combinations of elements from [listA] and [listB]
 */
fun <A, B> allCombinations(listA: Iterable<A>, listB: Iterable<B>): Sequence<Pair<A, B>> =
    sequence {
        listA.forEach { a ->
            listB.forEach { b ->
                yield(Pair(a, b))
            }
        }
    }