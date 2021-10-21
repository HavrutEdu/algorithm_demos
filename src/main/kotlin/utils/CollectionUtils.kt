package utils

/**
 * Allows syntax like
 * `val (a1, a2, a3, a4, a5, a6) = list`
 */
operator fun <E> List<E>.component6(): E = this.get(5)