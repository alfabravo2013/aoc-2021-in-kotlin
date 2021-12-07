import kotlin.math.abs

fun main() {

    fun sumOfSeries(n: Int): Int = (n * (n + 1)) / 2

    fun part1Reduce(entry: Map.Entry<Int, Int>, cur: Int): Int = entry.value * abs(entry.key - cur)

    fun part2Reduce(entry: Map.Entry<Int, Int>, cur: Int): Int = entry.value * sumOfSeries(abs(entry.key - cur))

    fun findMinimum(input: List<String>, reducer: (Map.Entry<Int, Int>, Int) -> Int): Int {
        val positions = input.single().split(',').map { it.toInt() }.sorted()
        val distribution = positions.groupingBy { it }.eachCount()

        var minFuel = Int.MAX_VALUE
        var start = positions.first()
        var end = positions.last()

        while (start < end) {
            val med = (start + end) / 2
            val fuelToStart = distribution.entries.sumOf { reducer.invoke(it, start) }
            val fuelToMid = distribution.entries.sumOf { reducer.invoke(it, med) }
            val fuelToEnd = distribution.entries.sumOf { reducer.invoke(it, end) }
            when {
                fuelToMid in (fuelToStart + 1) until fuelToEnd -> end = med - 1
                fuelToMid in (fuelToEnd + 1) until fuelToStart -> start = med + 1
                fuelToMid < fuelToStart && fuelToMid < fuelToEnd && fuelToStart < fuelToEnd -> end = med - 1
                else -> start = med + 1
            }
            minFuel = listOf(fuelToStart, fuelToMid, fuelToEnd).minOf { it }
        }

        return minFuel
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
