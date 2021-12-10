fun main() {

    fun findCorruptedChar(line: String): Char? {
        val stack = ArrayDeque<Char>()
        for (char in line) {
            when {
                char in opening -> stack.addLast(char)
                opening.indexOf(stack.last()) == closing.indexOf(char) -> stack.removeLast()
                opening.indexOf(stack.last()) != closing.indexOf(char) -> return char
                else -> return null
            }
        }
        return null
    }

    fun findMissingChars(line: String): List<Char> {
        val stack = ArrayDeque<Char>()

        for (char in line) {
            when {
                char in opening -> stack.addLast(char)
                opening.indexOf(stack.last()) == closing.indexOf(char) -> stack.removeLast()
                else -> return emptyList()
            }
        }

        val missingChars = mutableListOf<Char>()
        while (stack.isNotEmpty()) {
            val index = opening.indexOf(stack.removeLast())
            missingChars.add(closing[index])
        }
        return missingChars
    }

    fun part1(input: List<String>): Int {
        val cost = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)

        return input.mapNotNull(::findCorruptedChar).sumOf { cost[it]!! }
    }

    fun countScore(missingChars: List<Char>, cost: Map<Char, Int>): Long {
        var score = 0L
        for (char in missingChars) {
            score *= 5
            score += cost[char]!!
        }
        return score
    }

    fun part2(input: List<String>): Long {
        val cost = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

        val scores = input.map(::findMissingChars).filter { it.isNotEmpty() }.map { countScore(it, cost) }

        return scores.sorted()[scores.size / 2]
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
