fun main() {

    fun Array<CharArray>.print() {
        val str = this.map { row -> row.map { char -> char.toString().repeat(2) }.joinToString("") }.joinToString("\n")
        println(str)
        println()
    }

    fun parseInput(input: List<String>): Pair<Array<CharArray>, List<Pair<String, Int>>> {
        val dots = input.takeWhile { it.isNotEmpty() }.map { line ->
            val (col, row) = line.split(',').map { it.toInt() }
            row to col
        }

        val instructions = input.dropWhile { it.isNotEmpty() }.drop(1).map { line ->
            val (axis, num) = line.substringAfter("fold along ").split('=')
            axis to num.toInt()
        }

        val maxRow = dots.maxOf { it.first } + 1
        val maxCol = dots.maxOf { it.second } + 1
        val array = Array(maxRow) { CharArray(maxCol) { '.' } }

        for (dot in dots) {
            array[dot.first][dot.second] = '█'
        }

        return array to instructions
    }

    fun foldVertical(array: Array<CharArray>, column: Int): Array<CharArray> {
        val folded = Array(array.size) { CharArray(column) { '.' } }

        for (y in array.indices) {
            for (x in array[y].indices) {
                if (x < column) {
                    folded[y][x] = array[y][x]
                }

                if (x > column && array[y][x] == '█') {
                    folded[y][2 * column - x] = array[y][x]
                }
            }
        }

        return folded
    }

    fun foldHorizontally(array: Array<CharArray>, row: Int): Array<CharArray> {
        val folded = Array(row) { CharArray(array[0].size) { '.' } }

        for (y in array.indices) {
            for (x in array[y].indices) {
                if (y < row) {
                    folded[y][x] = array[y][x]
                }

                if (y > row && array[y][x] == '█') {
                    folded[2 * row - y][x] = array[y][x]
                }
            }
        }

        return folded
    }

    fun foldArray(array: Array<CharArray>, instruction: Pair<String, Int>): Array<CharArray> {
        return when (instruction.first) {
            "y" -> foldHorizontally(array, instruction.second)
            "x" -> foldVertical(array, instruction.second)
            else -> error("Unknown axis: ${instruction.first}, value=${instruction.second}")
        }
    }

    fun part1(input: List<String>): Int {
        var (array, instructions) = parseInput(input)
        array = foldArray(array, instructions.first())

        return array.flatMap { it.toList() }.count { it == '█' }
    }

    fun part2(input: List<String>) {
        var (array, instructions) = parseInput(input)
        for (instruction in instructions) {
            array = foldArray(array, instruction)
        }
        array.print()
    }


    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)

    val input = readInput("Day13")
    part2(input)
}
