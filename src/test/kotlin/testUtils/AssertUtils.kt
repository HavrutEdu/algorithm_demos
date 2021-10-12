package testUtils

import kotlin.test.assertNotEquals

inline fun <T> assertSometimesNotEquals(illegal: T, tries: Int = 10, getActual: () -> T) {
    repeat(tries - 1) {
        if (getActual() != illegal)
            return  // OK!
    }
    assertNotEquals(illegal, getActual(), "even after $tries tries")
}