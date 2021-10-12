package integer.sum

import org.junit.jupiter.api.Test
import testUtils.assertSometimesNotEquals
import kotlin.test.assertEquals

class MaxXYIntSumLearnerTest {

    @Test
    fun `learner improves on correct examples`() {
        val learner = MaxXYIntSumLearner(listOf(0, 1, 2, 3, Int.MAX_VALUE))

        learner.updateBeliefs(0, 0, 0)
        println(learner)
        assertEquals(0, learner.predictAnswer(0, 0))
        assertSometimesNotEquals(1) { learner.predictAnswer(0, 1) }

        learner.updateBeliefs(1, 0, 1)
        println(learner)
        assertEquals(1, learner.predictAnswer(0, 1))
        assertSometimesNotEquals(4) { learner.predictAnswer(3, 1) }

        learner.updateBeliefs(3, 3, 6)
        println(learner)
        assertEquals(3, learner.predictAnswer(2, 1))
        assertEquals(4, learner.predictAnswer(3, 1))
    }

    @Test
    fun `learner gives wrong answers after learning incorrect examples`() {
        val learner = MaxXYIntSumLearner(listOf(0, 1, 2, 3, Int.MAX_VALUE))

        learner.updateBeliefs(1, 1, 1)
        println(learner)
        assertSometimesNotEquals(1) { learner.predictAnswer(0, 1) }
    }
}