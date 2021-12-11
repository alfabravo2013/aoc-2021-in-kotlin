fun main() {

    fun buildGrid(input: List<String>): Array<Array<Octopus>> {
        return input.mapIndexed { row, line ->
            line.toList().mapIndexed { col, char ->
                Octopus(row, col, char.digitToInt())
            }.toTypedArray()
        }.toTypedArray()
    }

    fun Array<Array<Octopus>>.findEnergizedNeighborsOf(octopus: Octopus): List<Octopus> {
        val (row, col) = octopus
        val neighbors = mutableListOf<Octopus>()
        for (y in row - 1..row + 1) {
            for (x in col - 1..col + 1) {
                if (y < 0 || x < 0 || y == row && x == col || y > lastIndex || x > this[y].lastIndex) {
                    continue
                }
                val candidate = this[y][x]
                if (candidate.energy > 0) {
                    neighbors.add(candidate)
                }
            }
        }
        return neighbors
    }

    fun flash(octopus: Octopus, grid: Array<Array<Octopus>>, queue: ArrayDeque<Octopus>) {
        octopus.energy = 0

        grid.findEnergizedNeighborsOf(octopus).forEach { neighbor ->
            neighbor.energy++
            queue.addLast(neighbor)
        }
    }

    fun makeStep(grid: Array<Array<Octopus>>): Int {
        var stepFlashes = 0
        val queue = ArrayDeque(
            grid.flatMap { it.toList().onEach { octopus -> octopus.energy++ } }
        )

        while (queue.isNotEmpty()) {
            val octopus = queue.removeFirst()
            if (octopus.energy > 9) {
                flash(octopus, grid, queue)
                stepFlashes++
            }
        }

        return stepFlashes
    }

    fun part1(input: List<String>): Int {
        val grid = buildGrid(input)
        var flashes = 0

        repeat(100) {
            flashes += makeStep(grid)
        }

        return flashes
    }

    fun part2(input: List<String>): Int {
        val grid = buildGrid(input)
        var step = 1

        while (makeStep(grid) != 100) {
            step++
        }

        return step
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}

data class Octopus(val row: Int, val col: Int, var energy: Int)
