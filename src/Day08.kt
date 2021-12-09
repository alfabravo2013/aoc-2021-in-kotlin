fun main() {

    fun part1(input: List<String>): Int {
        return input
            .map { pattern -> pattern.substringAfter(" | ") }
            .flatMap { digits -> digits.split(' ') }
            .count { digit -> digit.length in setOf(2, 3, 4, 7) }
    }

    fun mapDigits(string: String): Map<String, String> {
        val samples =  string.split(' ').map { it.toSet() }
        val digit1 = samples.first { it.size == 2 }
        val digit7 = samples.first { it.size == 3 }
        val digit4 = samples.first { it.size == 4 }
        val digit8 = samples.first { it.size == 7 }
        val sixSegments = samples.filter { it.size == 6 } //
        val digit9 = sixSegments.first { it.containsAll(digit4) }
        val digit6 = sixSegments.first { !it.containsAll(digit1) }
        val digit0 = sixSegments.first { !it.containsAll(digit9) && !it.containsAll(digit6) }
        val fiveSegments = samples.filter { it.size == 5 } //
        val digit3 = fiveSegments.first { it.containsAll(digit1) }
        val digit2 = fiveSegments.first { it.contains(digit0.first { segment -> !digit9.contains(segment) }) }
        val digit5 = fiveSegments.first { !it.containsAll(digit2) && !it.containsAll(digit3) }

        return listOf(digit0, digit1, digit2, digit3, digit4, digit5, digit6, digit7, digit8, digit9)
            .mapIndexed { index, list ->
                list.sorted().joinToString("") to index.toString()
            }.toMap()
    }

    fun decipherReadings(map: Map<String, String>, readings: String): Int {
        return readings.split(' ')
            .joinToString("") { reading ->
                map[reading.toList().sorted().joinToString("")] ?: error("Can't decipher $reading")
            }.toInt()
    }

    fun part2(input: List<String>): Int {
        return input
            .map { line -> line.split(" | ") }
            .map { parts ->
                val map = mapDigits(parts[0])
                decipherReadings(map, parts[1])
            }.sumOf { it }
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
