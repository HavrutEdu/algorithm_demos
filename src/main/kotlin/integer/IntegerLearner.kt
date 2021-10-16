package integer

interface IntegerLearner {
    fun updateBeliefs(x: Int, y: Int, ans: Int)
    fun predictAnswer(x: Int, y: Int): Int
}