fun main() {

    fun String.isNotBig(): Boolean = !this[0].isUpperCase()

    fun buildCaves(input: List<String>): Map<String, Set<String>> {
        val map = mutableMapOf<String, MutableSet<String>>()
        for (line in input) {
            val (from, to) = line.split('-')
            if (map.containsKey(from)) {
                map[from]!!.add(to)
            } else {
                map[from] = mutableSetOf(to)
            }
            if (map.containsKey(to)) {
                map[to]!!.add(from)
            } else {
                map[to] = mutableSetOf(from)
            }
        }
        return map
    }

    fun countPaths(
        map: Map<String, Set<String>>,
        start: String,
        visited: MutableSet<String> = mutableSetOf(),
        path: MutableList<String> = mutableListOf(),
        result: MutableSet<List<String>>,
        special: String? = null
    ) {
        path.add(start)
        if (start == "end") {
            result.add(path)
            return
        }

        if (start.isNotBig() && start != "end") {
            visited.add(start)
        }

        val unvisited = map[start]!! - visited
        if (special != null && start == special && path.count{ it == start } < 2) {
            visited.remove(special)
        }

        for (cave in unvisited) {
            val visitedSoFar = mutableSetOf<String>()
            visitedSoFar.addAll(visited)
            countPaths(map, cave, visitedSoFar, path.toMutableList(), result, special)
        }
    }

    fun part1(input: List<String>): Int {
        val map = buildCaves(input)
        val result = mutableSetOf<List<String>>()
        countPaths(map, "start", result = result)
        return result.size
    }

    fun part2(input: List<String>): Int {
        val map = buildCaves(input)
        val result = mutableSetOf<List<String>>()
        val caves = map.keys.filter { it.isNotBig() && it != "start" && it != "end" }
        for (cave in caves) {
            countPaths(map, "start", special = cave, result = result)
        }
        return result.size
    }

    val testInput1 = readInput("Day12_test1")
    val testInput2 = readInput("Day12_test2")
    val testInput3 = readInput("Day12_test3")
    check(part1(testInput1) == 10)
    check(part1(testInput2) == 19)
    check(part1(testInput3) == 226)

    check(part2(testInput1) == 36)
    check(part2(testInput2) == 103)
    check(part2(testInput3) == 3509)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
