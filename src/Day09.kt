import java.util.*

fun main() {

    fun Tile.findAdjacentTiles(tiles: List<List<Tile>>): List<Tile> {
        return when (row) {
            0 -> {
                when (col) {
                    0 -> listOf(tiles[col][row + 1], tiles[row][col + 1])
                    tiles[row].lastIndex -> listOf(tiles[row + 1][col], tiles[row][col - 1])
                    else -> listOf(tiles[row + 1][col], tiles[row][col - 1], tiles[row][col + 1])
                }
            }
            tiles.lastIndex -> {
                when (col) {
                    0 -> listOf(tiles[row - 1][col], tiles[row][col + 1])
                    tiles[row].lastIndex -> listOf(tiles[row - 1][col], tiles[row][col - 1])
                    else -> listOf(tiles[row - 1][col], tiles[row][col - 1], tiles[row][col + 1])
                }
            }
            else -> {
                when (col) {
                    0 -> listOf(tiles[row - 1][col], tiles[row][col + 1], tiles[row + 1][col])
                    tiles[row].lastIndex -> listOf(tiles[row - 1][col], tiles[row][col - 1], tiles[row + 1][col])
                    else -> listOf(tiles[row - 1][col], tiles[row][col - 1], tiles[row + 1][col], tiles[row][col + 1])
                }
            }
        }
    }

    fun buildHeatMap(input: List<String>): List<List<Tile>> {
        return input.mapIndexed { row, line ->
            line.toList().mapIndexed { col, char ->
                Tile(row, col, char.digitToInt())
            }
        }
    }

    fun Tile.isLowPoint(adjacent: List<Tile>): Boolean {
        return adjacent.all { height < it.height }
    }

    fun part1(input: List<String>): Int {
        val heatMap = buildHeatMap(input)

        var lowPoints = 0
        for (row in heatMap.indices) {
            for (col in heatMap[row].indices) {
                val tile = heatMap[row][col]
                val adjacentTiles = tile.findAdjacentTiles(heatMap)
                if (tile.isLowPoint(adjacentTiles)) {
                    lowPoints += tile.height + 1
                }
            }
        }
        return lowPoints
    }

    fun part2(input: List<String>): Int {
        val heatMap = buildHeatMap(input)
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
                    val adjacent = current.findAdjacentTiles(heatMap)
                    adjacent.filter { t -> !t.marked && t.height != 9 }
                        .forEach { t -> queue.addLast(t).also { t.marked = true } }
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
