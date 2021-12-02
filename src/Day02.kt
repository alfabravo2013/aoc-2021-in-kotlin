fun main() {

    fun toCommand(line: String): Pair<String, Int> {
        val splitLine = line.split(' ')
        return splitLine[0] to splitLine[1].toInt()
    }

    fun part1(input: List<String>): Int {
        var position = 0
        var depth = 0

        input.map(::toCommand).forEach { command ->
                run {
                    when (command.first) {
                        "up" -> depth -= command.second
                        "down" -> depth += command.second
                        else -> position += command.second
                    }
                }
            }
        return  position * depth
    }

    fun part2(input: List<String>): Int {
        var aim = 0
        var position = 0
        var depth = 0

        input.map(::toCommand).forEach { command ->
            run {
                when (command.first) {
                    "up" -> aim -= command.second
                    "down" -> aim += command.second
                    else -> {
                        position += command.second
                        depth += aim * command.second
                    }
                }
            }
        }
        return position * depth
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
