package day

import Rectangle
import euclideanDistance
import findPath
import neighbour
import neighbours
import toRectangle
import kotlin.math.absoluteValue

object Day20 : Day {
    override fun part1(input: List<String>): Result {
        val map = input.map { it.toCharArray().asList() }.toRectangle()
        val startEnd = map.indexedIterator().asSequence()
            .filter { (_, l) -> l == 'S' || l == 'E' }
            .map { (c, l) -> l to c }
            .toMap()
        val startCoordinate = startEnd['S']!!
        val endCoordinate = startEnd['E']!!

        val pathNodes = findPath(
            start = startCoordinate,
            isEnd = { it == endCoordinate }) { coord ->
            map.neighbours(coord).values
                .filter { (l, _) -> l != '#' }
                .map { (_, c) -> c to 1.0 }
        }!!.nodes

        val distancesToEnd = pathNodes.asReversed().mapIndexed { i, it -> it to i }.toMap()

        val cuts = pathNodes.flatMap { it.tryCut(map, distancesToEnd) }
        return cuts.count { it.save >= 100 }.asSuccess()
    }

    fun Rectangle.Coordinate.tryCut(map: Rectangle<Char>, distancesToEnd: Map<Rectangle.Coordinate, Int>): List<Cut> {
        val originalDistanceToEnd = distancesToEnd[this]!!
        return map.neighbours(this).mapNotNull { (side, data) ->
            val (char, coord) = data
            if (char == '.') null else {
                val extraStep = data.second.neighbour(side)
                if (!map.isInBounds(coord)) null else {
                    // Subtract 2 for the steps it took to take the cut
                    distancesToEnd[extraStep]?.let { Cut(this, extraStep, originalDistanceToEnd - it - 2) }
                }
            }
        }
    }

    data class Cut(val from: Rectangle.Coordinate, val to: Rectangle.Coordinate, val save: Int)

    override fun part2(input: List<String>): Result {
        val map = input.map { it.toCharArray().asList() }.toRectangle()
        val startEnd = map.indexedIterator().asSequence()
            .filter { (_, l) -> l == 'S' || l == 'E' }
            .map { (c, l) -> l to c }
            .toMap()
        val startCoordinate = startEnd['S']!!
        val endCoordinate = startEnd['E']!!

        val pathNodes = findPath(
            start = startCoordinate,
            isEnd = { it == endCoordinate }) { coord ->
            map.neighbours(coord).values
                .filter { (l, _) -> l != '#' }
                .map { (_, c) -> c to 1.0 }
        }!!.nodes

        val distancesToEnd = pathNodes.asReversed().mapIndexed { i, it -> it to i }.toMap()

        val cuts = pathNodes.flatMap { it.tryCutLong(map, distancesToEnd) }
        return cuts.count { it.save >= 100 }.asSuccess()

    }

    fun Rectangle.Coordinate.tryCutLong(
        map: Rectangle<Char>,
        distancesToEnd: Map<Rectangle.Coordinate, Int>
    ): Sequence<Cut> {
        val originalDistanceToEnd = distancesToEnd[this]!!
        return map.coordinatesWithinSteps(this, 20).mapNotNull { end ->
            distancesToEnd[end]?.let { Cut(this, end, originalDistanceToEnd - it - euclideanDistance(end)) }
        }
    }

    fun <T> Rectangle<T>.coordinatesWithinSteps(
        from: Rectangle.Coordinate,
        numberOfSteps: Int
    ): Sequence<Rectangle.Coordinate> {
        return sequence {
            for (x in -numberOfSteps..numberOfSteps) {
                val possibleExtraSteps = numberOfSteps - x.absoluteValue
                for (y in -possibleExtraSteps..possibleExtraSteps) {
                    if (isInBounds(from.x + x, from.y + y)) yield(Rectangle.Coordinate(from.x + x, from.y + y))
                }
            }
        }
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            0,
            0,
            """###############
#...#...#.....#
#.#.#.#.#.###.#
#S#...#.#.#...#
#######.#.#.###
#######.#.#...#
#######.#.###.#
###..E#...#...#
###.#######.###
#...###...#...#
#.#####.#.###.#
#.#...#.#.#...#
#.#.#.#.#.#.###
#...#...#...###
###############"""
                .lines()
        )
    }
}