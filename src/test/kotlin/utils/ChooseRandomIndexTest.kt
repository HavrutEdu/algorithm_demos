package utils

import org.junit.jupiter.api.Test
import testUtils.assertContentEquals

class ChooseRandomIndexTest {
    private val weights = listOf(0.0, 0.4, 0.1, 0.5)

    @Test
    fun `random values are distributed like weights`() {
        val repeats = 1000

        val counters = MutableList(weights.size, init = { 0 })
        repeat(times = repeats) {
            val rand = chooseRandomIndex(weights)
            counters[rand] += 1
        }

        // Check the counter distribution
        val actualDistribution = counters.map { count -> count * 1.0 / repeats }
        assertContentEquals(weights, actualDistribution, resolutionDecimalPlaces = 1)
    }
}