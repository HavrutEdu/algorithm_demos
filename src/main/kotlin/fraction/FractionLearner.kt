package fraction


interface FractionLearner {
    fun updateBeliefs(num1: Int, den1: Int, num2: Int, den2: Int, num: Int, den: Int)
    fun predictAnswer(num1: Int, den1: Int, num2: Int, den2: Int): Pair<Int, Int>
}