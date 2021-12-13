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
        special: MutableMap<String, Int>? = null
    ): Int {

        if (special != null) {
            if (special.containsKey(start)) {
                if (special[start]!! > 0) {
                    visited.add(start)
                }
            } else {
                if (start.isNotBig() && start != "end") {
                    visited.add(start)
                }
            }
        } else {
            if (start.isNotBig() && start != "end") {
                visited.add(start)
            }
        }

        if (start == "end") {
            return 1
        }

        val unvisited = (map[start]!! - visited).toMutableSet()

        if (unvisited.isEmpty()) {
            return 0
        }

        var count = 0
        for (cave in unvisited) {
            val copy = mutableSetOf<String>()
            copy.addAll(visited)

            val newSpecial = if (special == null) null else mutableMapOf<String, Int>()
            if (newSpecial != null) {
                val key = special!!.keys.first()
                val value = if (special.containsKey(start)) special[key]!! + 1 else special[key]!!
                newSpecial[key] = value
            }

            count += countPaths(map, cave, copy, newSpecial)
        }

        return count
    }

    fun part1(input: List<String>): Int {
        val map = buildCaves(input)
        return countPaths(map, "start")
    }

    fun part2(input: List<String>): Int {
        val map = buildCaves(input)
        val smallCaves = map.keys.filter { it != "start" && it != "end" && it.isNotBig() }
        var count = 0
        println(smallCaves)
        for (cave in smallCaves) {
            count += countPaths(map, "start", special = mutableMapOf(cave to 0))
        }
        println(count)
        return count
    }

    val testInput1 = readInput("Day12_test1")
    val testInput2 = readInput("Day12_test2")
    val testInput3 = readInput("Day12_test3")
    check(part1(testInput1) == 10)
    check(part1(testInput2) == 19)
    check(part1(testInput3) == 226)

    check(part2(testInput1) != 36)
    check(part2(testInput2) != 103)
    check(part2(testInput3) != 3509)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
