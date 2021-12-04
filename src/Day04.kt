data class BoardCell(val value: Int, var marked: Boolean = false)

data class BingoBoard(val cells: List<List<BoardCell>>) {
    val id: Int = ID++

    fun isWinning(): Boolean {
        if (cells.any { row -> row.all { it.marked } }) {
            return true
        }

        for (i in cells.first().indices) {
            if (cells.all { row -> row[i].marked }) {
                return true
            }
        }

        return false
    }

    fun acceptNumber(number: Int) {
        cells.flatMap { row -> row.asIterable() }
            .firstOrNull { cell -> cell.value == number }
            ?.let { cell -> cell.marked = true }
    }

    fun sumOfUnmarked(): Int {
        return cells.flatMap { row -> row.asIterable() }
            .filter { cell -> !cell.marked }
            .sumOf { cell -> cell.value }
    }

    companion object {
        private var ID = 0
    }
}

fun main() {

    fun to2DNumberList(string: String): List<Int> {
        return string.split("\\s+".toRegex()).filter { it.isNotBlank() }.map { it.toInt() }
    }

    fun List<List<Int>>.toBingoBoard(): BingoBoard {
        val cells = this.map { row -> row.map { BoardCell(it) } }
        return BingoBoard(cells)
    }

    fun createBoards(input: List<String>): List<BingoBoard> {
        return input.asSequence().drop(1)
            .filter { str -> str.isNotBlank() }
            .map(::to2DNumberList)
            .windowed(5, 5)
            .map { it.toBingoBoard() }
            .toList()
    }

    fun part1(input: List<String>): Int {
        val randomNumbers = input.first().split(',').map { it.toInt() }

        val boards = createBoards(input)

        for (rn in randomNumbers) {
            for (board in boards) {
                board.acceptNumber(rn)
                if (board.isWinning()) {
                    return board.sumOfUnmarked() * rn
                }
            }
        }

        error("Couldn't find any winning board")
    }

    fun part2(input: List<String>): Int {
        val randomNumbers = input.first().split(',').map { it.toInt() }

        val boards = createBoards(input)
        val winners = mutableListOf<Int>()

        for (rn in randomNumbers) {
            for (board in boards) {
                board.acceptNumber(rn)
                if (board.isWinning() && board.id !in winners) {
                    winners.add(board.id)
                }
                if (winners.size == boards.size) {
                    return board.sumOfUnmarked() * rn
                }
            }
        }

        error("Couldn't find the last winning board")
    }


    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
