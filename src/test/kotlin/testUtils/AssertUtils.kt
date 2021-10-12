package testUtils

import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.test.assertNotEquals

inline fun <T> assertSometimesNotEquals(illegal: T, tries: Int = 10, getActual: () -> T) {
    repeat(tries - 1) {
        if (getActual() != illegal)
            return  // OK!
    }
    assertNotEquals(illegal, getActual(), "even after $tries tries")
}

fun assertContentEquals(expected: Iterable<Double>, actual: Iterable<Double>, resolutionDecimalPlaces: Int) {
    assert(resolutionDecimalPlaces > 0)
    val decimal = 10.0.pow(resolutionDecimalPlaces)
    val roundedExpected = expected.map { (it * decimal).roundToInt().toDouble() / decimal }
    val roundedActual = actual.map { (it * decimal).roundToInt().toDouble() / decimal }
    kotlin.test.assertContentEquals(
        roundedExpected, roundedActual,
        message = "element resolution is $resolutionDecimalPlaces decimal places"
    )
}