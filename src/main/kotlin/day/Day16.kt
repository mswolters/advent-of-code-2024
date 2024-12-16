package day

import Rectangle.Coordinate
import Side
import findPath
import neighbours
import toRectangle

object Day16 : Day {
    data class PathLocation(val coordinate: Coordinate, val direction: Side)

    override fun part1(input: List<String>): Result {
        val map = input.map { it.toCharArray().toList() }.toRectangle()
        val (start, _) = map.indexedIterator().asSequence().first { (_, l) -> l == 'S' }
        val (end, _) = map.indexedIterator().asSequence().first { (_, l) -> l == 'E' }

        val path = findPath(
            start = PathLocation(start, Side.East),
            isEnd = { it.coordinate == end }
        ) {
            map.neighbours(it.coordinate)
                .filter { (s, _) -> s != it.direction.opposite() }
                .filter { (_, data) -> data.first != '#' }
                .map { (side, data) ->
                    PathLocation(data.second, side) to 1.0 + when (side) {
                        it.direction -> 0.0
                        in it.direction.perpendicular() -> 1000.0
                        else -> throw IllegalStateException()
                    }
                }
        }
        return path!!.length.toInt().asSuccess()
    }

    override fun part2(input: List<String>): Result {
        return NotImplemented
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            listOf(
                Day.TestData.Test(
                    "1", 7036,
                    """
                        ###############
                        #.......#....E#
                        #.#.###.#.###.#
                        #.....#.#...#.#
                        #.###.#####.#.#
                        #.#.#.......#.#
                        #.#.#####.###.#
                        #...........#.#
                        ###.#.#####.#.#
                        #...#.....#.#.#
                        #.#.#.###.#.#.#
                        #.....#...#.#.#
                        #.###.#.#.#.#.#
                        #S..#.....#...#
                        ###############
                    """.trimIndent().lines()
                ),
                Day.TestData.Test(
                    "2", 11048,
                    """
                        #################
                        #...#...#...#..E#
                        #.#.#.#.#.#.#.#.#
                        #.#.#.#...#...#.#
                        #.#.#.#.###.#.#.#
                        #...#.#.#.....#.#
                        #.#.#.#.#.#####.#
                        #.#...#.#.#.....#
                        #.#.#####.#.###.#
                        #.#.#.......#...#
                        #.#.###.#####.###
                        #.#.#...#.....#.#
                        #.#.#.#####.###.#
                        #.#.#.........#.#
                        #.#.#.#########.#
                        #S#.............#
                        #################
                    """.trimIndent().lines()
                )
            ),
            listOf(
            )
        )
    }
}