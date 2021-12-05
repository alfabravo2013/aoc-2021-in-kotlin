import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun convertInputToVectors(input: List<String>): List<Vector2D> {
        return input.map { line -> line.split(" -> ") }.map { array ->
            val start = array[0].split(',').map { it.toInt() }
            val end = array[1].split(',').map { it.toInt() }

            Vector2D(
                Point(start[0], start[1]),
                Point(end[0], end[1])
            )
        }
    }

    fun part1(input: List<String>): Int {
        val vectors = convertInputToVectors(input).filter { it.isHorizontal() || it.isVertical() }
        val map = VentMap(vectors)
        return map.countIntersections()
    }

    fun part2(input: List<String>): Int {
        val vectors = convertInputToVectors(input)
        val map = VentMap(vectors)
        return map.countIntersections()
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

data class Point(val x: Int, val y: Int)

class Vector2D(val start: Point, val end: Point) {

    fun isHorizontal(): Boolean = start.x == end.x

    fun isVertical(): Boolean = start.y == end.y

    fun isDiagonal(): Boolean = abs(start.x - end.x) == abs(start.y - end.y)

    fun toList(): List<Point> {
        val minX = min(start.x, end.x)
        val maxX = max(start.x, end.x)
        val minY = min(start.y, end.y)
        val maxY = max(start.y, end.y)

        return when {
            isHorizontal() -> (minY..maxY).map { Point(start.x, it) }
            isVertical() -> (minX..maxX).map { Point(it, start.y) }
            isDiagonal() -> {
                val isAscending = start.x < end.x && start.y < end.y || end.x < start.x && end.y < start.y
                val diff = abs(start.x - end.x)
                if (isAscending) {
                    (0..diff).map { Point(minX + it, minY + it) }
                } else {
                    (0..diff).map { Point(minX + it, maxY - it) }
                }
            }
            else -> error("Vector can only be vertical, horizontal or diagonal")
        }
    }
}

class VentMap(vents: List<Vector2D>) {
    private val map: Array<IntArray>

    init {
        val points = vents.flatMap { listOf(it.start, it.end).asIterable() }
        val minX = points.minOf { it.x }
        val maxX = points.maxOf { it.x }
        val minY = points.minOf { it.y }
        val maxY = points.maxOf { it.y }
        map = Array(maxY - minY + 1) { IntArray(maxX - minX + 1) { 0 } }

        val vectors = vents.map { it.toList() }
        vectors.forEach { vector ->
            for (point in vector) {
                map[point.y - minY][point.x - minX] += 1
            }
        }
    }

    fun countIntersections(): Int = map.flatMap { row -> row.asIterable() }.count { it > 1 }
}
