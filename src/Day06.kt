fun main() {

    fun countPopulation(input: List<String>, days: Int): Long {
        val firstGeneration = input.first()
            .split(',')
            .map { it.toInt() }
            .groupingBy { it }.eachCount()

        val population = (0..8).associateWith { 0L }.toMutableMap()
        firstGeneration.entries.forEach { f ->
            population[f.key] = f.value.toLong()
        }

        for (day in 1..days) {
            val newGen = (0..8).associateWith { age ->
                when (age) {
                    6 -> population.getOrElse(7) { 0L } + population.getOrElse(0) { 0L }
                    8 -> population.getOrElse(0) { 0L }
                    else -> population.getOrElse(age + 1) { 0L }
                }
            }

            newGen.entries.forEach { ng ->
                population[ng.key] = ng.value
            }
        }

        return population.values.sum()
    }

    fun part1(input: List<String>): Long {
        return countPopulation(input, 80)
    }

    fun part2(input: List<String>): Long {
        return countPopulation(input, 256)
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934L) // == 5934L
    check(part2(testInput) == 26984457539L)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
