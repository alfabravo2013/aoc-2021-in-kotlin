fun main() {

    fun List<List<Tile>>.neighborsOf(tile: Tile): List<Tile> {
        val (row, col) = tile
        return when (row) {
            0 -> {
                when (col) {
                    0 -> listOf(this[col][row + 1], this[row][col + 1])
                    this[row].lastIndex -> listOf(this[row + 1][col], this[row][col - 1])
                    else -> listOf(this[row + 1][col], this[row][col - 1], this[row][col + 1])
                }
            }
            lastIndex -> {
                when (col) {
                    0 -> listOf(this[row - 1][col], this[row][col + 1])
                    this[row].lastIndex -> listOf(this[row - 1][col], this[row][col - 1])
                    else -> listOf(this[row - 1][col], this[row][col - 1], this[row][col + 1])
                }
            }
            else -> {
                when (col) {
                    0 -> listOf(this[row - 1][col], this[row][col + 1], this[row + 1][col])
                    this[row].lastIndex -> listOf(this[row - 1][col], this[row][col - 1], this[row + 1][col])
                    else -> listOf(this[row - 1][col], this[row][col - 1], this[row + 1][col], this[row][col + 1])
                }
            }
        }
    }

    fun heatMapFrom(input: List<String>): List<List<Tile>> {
        return input.mapIndexed { row, line ->
            line.toList().mapIndexed { col, char ->
                Tile(row, col, char.digitToInt())
            }
        }
    }

    fun Tile.isLowestAmong(neighbors: List<Tile>): Boolean {
        return neighbors.all { height < it.height }
    }

    fun part1(input: List<String>): Int {
        val heatMap = heatMapFrom(input)
        var lowPoints = 0

        for (row in heatMap.indices) {
            for (col in heatMap[row].indices) {
                val tile = heatMap[row][col]
                val neighbors = heatMap.neighborsOf(tile)

                if (tile.isLowestAmong(neighbors)) {
                    lowPoints += tile.height + 1
                }
            }
        }

        return lowPoints
    }

    fun part2(input: List<String>): Int {
        val heatMap = heatMapFrom(input)
        val basins = mutableListOf<Int>()
        val queue = ArrayDeque<Tile>()

        for (row in heatMap.indices) {
            for (col in heatMap[row].indices) {
                val tile = heatMap[row][col]
                if (tile.marked || tile.height == 9) {
                    continue
                }

                var currentBasin = 0
                queue.addLast(tile).also { tile.marked = true }

                while (queue.isNotEmpty()) {
                    val current = queue.removeFirst().also { currentBasin++ }
                    val neighbors = heatMap.neighborsOf(current)

                    neighbors.filter { neighbor -> !neighbor.marked && neighbor.height != 9 }
                        .forEach { neighbor ->
                            queue.addLast(neighbor).also { neighbor.marked = true } }
                }

                basins.add(currentBasin)
            }
        }

        return basins.sorted().takeLast(3).reduce { acc, i -> acc * i }
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

data class Tile(val row: Int, val col: Int, val height: Int, var marked: Boolean = false)
