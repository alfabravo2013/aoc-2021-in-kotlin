import kotlin.math.abs

fun main() {

    fun sumOfSeries(n: Int): Int = (n * (n + 1)) / 2

    fun part1Reduce(entry: Map.Entry<Int, Int>, cur: Int): Int = entry.value * abs(entry.key - cur)

    fun part2Reduce(entry: Map.Entry<Int, Int>, cur: Int): Int = entry.value * sumOfSeries(abs(entry.key - cur))

    fun findMinimum(input: List<String>, reducer: (Map.Entry<Int, Int>, Int) -> Int): Int {
        val positions = input.single().split(',').map { it.toInt() }.sorted()
        val distribution = positions.groupingBy { it }.eachCount()

        var leftSum: Int
        var rightSum: Int
        var minSum = Int.MAX_VALUE

        for (cur in positions.first()..positions.last()) {
            leftSum = distribution.entries.filter { it.key < cur }.sumOf { reducer.invoke(it, cur) }
            rightSum = distribution.entries.filter { it.key > cur }.sumOf { reducer.invoke(it, cur) }
            if (minSum > leftSum + rightSum) {
                minSum = leftSum + rightSum
            }
        }
        return minSum
    }

    fun part1(input: List<String>): Int = findMinimum(input, ::part1Reduce)

    fun part2(input: List<String>): Int = findMinimum(input, ::part2Reduce)

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))

}
