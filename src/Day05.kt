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

    fun countIntersections(vectors: List<Vector2D>): Int {
        return vectors.flatMap { vector -> vector.toListOfPoints() }
            .groupingBy { it }.eachCount()
            .values.count { totalCount -> totalCount > 1 }
    }

    fun part1(input: List<String>): Int {
        val vectors = convertInputToVectors(input).filter { it.isHorizontal() || it.isVertical() }
        return countIntersections(vectors)
    }

    fun part2(input: List<String>): Int {
        val vectors = convertInputToVectors(input)
        return countIntersections(vectors)
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

data class Point(val x: Int, val y: Int)

class Vector2D(private val start: Point, private val end: Point) {

    fun isHorizontal(): Boolean = start.x == end.x

    fun isVertical(): Boolean = start.y == end.y

    fun isDiagonal(): Boolean = abs(start.x - end.x) == abs(start.y - end.y)

    fun toListOfPoints(): List<Point> {
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
                (0..diff).map { Point(minX + it, if (isAscending) minY + it else maxY - it) }
            }
            else -> error("Vector can only be vertical, horizontal or diagonal")
        }
    }
}
