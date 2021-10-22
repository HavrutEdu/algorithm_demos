package demo.text.cli

import demo.text.TextIOModelRunner
import fraction.sum.FracSumLearner
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

private val aModel = FracSumLearner()
private val aRunner = CliModelRunner(aModel)

fun allRegisteredActions(): List<Actions> {
    return aRunner.actions
}

class CliModelRunnerTest {
    @Test
    fun `runner performs basic flow`() {
        val runner = CliModelRunner(FracSumLearner())

        runner.modelRunner.performNextAction("status")
        runner.modelRunner.performNextAction("1 / 2 + 3 / 4 ?")
        runner.modelRunner.performNextAction("1 / 2 + 3 / 4 = 5 / 4")
        runner.modelRunner.performNextAction("1 / 2 + 3 / 4 ?")
        runner.modelRunner.performNextAction("1 / 2 + 1 / 4 ?")
        runner.modelRunner.performNextAction("status")

        assertThrows<TextIOModelRunner.Action.ExitSignalException> {
            runner.modelRunner.performNextAction("exit")
        }
    }

    @ParameterizedTest
    @MethodSource("demo.text.cli.CliModelRunnerTestKt#allRegisteredActions")
    fun `all Actions examples are valid`(action: Actions) {
        action.examples.forEach { example ->
            assertNotNull(action.matches(example), "`$example` should be parsed by $action")

            assertTrue("`$example` must match exactly one regex") {
                1 == action.matchers.count { it.matchEntire(example) != null }
            }

            (allRegisteredActions() - action).forEach { otherAction ->
                assertNull(
                    otherAction.matches(example),
                    "`$example` matched $otherAction which is different from $action"
                )
            }
        }
    }

}