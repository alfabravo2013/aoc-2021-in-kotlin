fun main() {

    fun List<List<Tile>>.neighborsOf(tile: Tile): List<Tile> {
        val (row, col) = tile
        return listOf(
            row - 1 to col,
            row to col - 1,
            row + 1 to col,
            row to col + 1
        ).filter { it.first in 0..lastIndex && it.second in 0..this[it.first].lastIndex }
            .map { this[it.first][it.second] }
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

    fun List<List<Tile>>.findLowestPoints(): List<Tile> {
        val list = mutableListOf<Tile>()
        for (row in indices) {
            for (col in this[row].indices) {
                val tile = this[row][col]
                val neighbors = neighborsOf(tile)
                if (tile.isLowestAmong(neighbors)) {
                    list.add(tile)
                }
            }
        }
        return list
    }

    fun part1(input: List<String>): Int {
        val heatMap = heatMapFrom(input)
        val lowestPoints = heatMap.findLowestPoints()
        return lowestPoints.sumOf { it.height + 1 }
    }

    fun part2(input: List<String>): Int {
        val heatMap = heatMapFrom(input)
        val lowestPoints = heatMap.findLowestPoints()
        val basins = mutableListOf<Int>()
        val queue = ArrayDeque<Tile>()

        for (tile in lowestPoints) {
            var currentBasin = 0
            queue.addLast(tile).also { tile.marked = true }

            while (queue.isNotEmpty()) {
                val current = queue.removeFirst().also { currentBasin++ }
                val neighbors = heatMap.neighborsOf(current).filter { !it.marked && it.height != 9 }

                for (neighbor in neighbors) {
                    queue.addLast(neighbor)
                    neighbor.marked = true
                }
            }
            basins.add(currentBasin)
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
