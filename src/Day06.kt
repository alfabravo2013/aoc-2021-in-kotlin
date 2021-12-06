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
            val newGeneration = (0..8).associateWith { daysUntilSpawning ->
                when (daysUntilSpawning) {
                    6 -> population[7]!! + population[0]!!
                    8 -> population[0]!!
                    else -> population[daysUntilSpawning + 1]!!
                }
            }

            newGeneration.entries.forEach { generation ->
                population[generation.key] = generation.value
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
    check(part1(testInput) == 5934L)
    check(part2(testInput) == 26984457539L)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
