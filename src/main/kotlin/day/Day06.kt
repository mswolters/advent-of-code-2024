package day

import MutableRectangle
import Rectangle
import Side
import coordinateNextTo
import toMutableRectangle
import toRectangle
import java.util.EnumSet

object Day06 : Day {
    override fun part1(input: List<String>): Result {
        val map = input.map { it.toCharArray().toList() }.toMutableRectangle()

        var (guardLocation, _) = map.indexedIterator().asSequence().first { (_, c) -> c == '^' }
        val directions = listOf(Side.North, Side.East, Side.South, Side.West)
        var direction = Side.North
        while (map.isInBounds(guardLocation)) {
            map[guardLocation] = 'X'

            var nextStep = direction.coordinateNextTo(guardLocation)
            while (map.isInBounds(nextStep) && map[nextStep] == '#') {
                direction = directions[(directions.indexOf(direction) + 1).mod(directions.size)]
                nextStep = direction.coordinateNextTo(guardLocation)
            }
            guardLocation = nextStep
        }

        return map.count { it == 'X' }.asSuccess()
    }

    override fun part2(input: List<String>): Result {
        val map = input.map { it.toCharArray().toList() }.toRectangle()
        val (initialGuardLocation, _) = map.indexedIterator().asSequence().first { (_, c) -> c == '^' }

        return map.indexedIterator().asSequence().filter { (c, _) -> c != initialGuardLocation }.count { (c, _) ->
            mapResultsInLoop(
                map.toMutableRectangle().apply { this[c] = '#' },
                initialGuardLocation
            )
        }.asSuccess()
    }

    fun mapResultsInLoop(map: Rectangle<Char>, initialGuardLocation: Rectangle.Coordinate): Boolean {
        val walkedMap = MutableRectangle(map.width, map.height) { _, _ -> EnumSet.noneOf(Side::class.java) }

        var guardLocation = initialGuardLocation
        val directions = listOf(Side.North, Side.East, Side.South, Side.West)
        var direction = Side.North
        while (map.isInBounds(guardLocation)) {
            if (direction in walkedMap[guardLocation]) return true
            walkedMap[guardLocation] += direction

            var nextStep = direction.coordinateNextTo(guardLocation)
            while (map.isInBounds(nextStep) && map[nextStep] == '#') {
                direction = directions[(directions.indexOf(direction) + 1).mod(directions.size)]
                nextStep = direction.coordinateNextTo(guardLocation)
            }
            guardLocation = nextStep
        }
        return false
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            41,
            6,
            """....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#..."""
                .lines()
        )
    }
}