package day

import Path
import PathData
import Rectangle.Coordinate
import Side
import findPath
import neighbour
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
        val map = input.map { it.toCharArray().toList() }.toRectangle()
        val (start, _) = map.indexedIterator().asSequence().first { (_, l) -> l == 'S' }
        val (end, _) = map.indexedIterator().asSequence().first { (_, l) -> l == 'E' }

        val pathData = PathData<PathLocation>()
        val path = findPath(
            start = PathLocation(start, Side.East),
            isEnd = { it.coordinate == end },
            data = pathData
        ) {
            sequence {
                val afterStep = it.coordinate.neighbour(it.direction)
                if (map[afterStep] != '#') {
                    yield(PathLocation(afterStep, it.direction) to 1.0)
                }
                it.direction.perpendicular()
                    .forEach { direction -> yield(PathLocation(it.coordinate, direction) to 1000.0) }
            }.toList()
        }!!
        val test = findAllShortestPaths(pathData.visitedNodes, path)
            .flatMap { it.nodes.map { pl -> pl.coordinate } }.toSet()
        return test.count().asSuccess()
    }

    fun findAllShortestPaths(
        visitedNodes: Map<PathLocation, Path<PathLocation>>,
        path: Path<PathLocation>
    ): List<Path<PathLocation>> {
        if (path.nodes.size <= 1) {
            return listOf(path)
        }

        val finalNode = path.nodes.last()
        val shortestToFinalNodeWithoutTurns = visitedNodes[PathLocation(
            finalNode.coordinate.neighbour(finalNode.direction.opposite()),
            finalNode.direction
        )]
        val shortestVisited = visitedNodes[path.nodes[path.nodes.size - 2]]!!
        if (shortestToFinalNodeWithoutTurns != null && shortestToFinalNodeWithoutTurns != shortestVisited && shortestToFinalNodeWithoutTurns.length < shortestVisited.length + 1000.0) {
            return (findAllShortestPaths(visitedNodes, shortestToFinalNodeWithoutTurns) +
                    findAllShortestPaths(visitedNodes, shortestVisited)
                    ).map { buildPath(it, finalNode, path.length - it.length) }
        }

        return findAllShortestPaths(visitedNodes, shortestVisited).map {
            buildPath(it, finalNode, path.length - it.length)
        }
    }

    fun buildPath(subPath: Path<PathLocation>, finalNode: PathLocation, finalLength: Double): Path<PathLocation> {
        return Path(subPath.length + finalLength, subPath.nodes + finalNode)
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
                Day.TestData.Test(
                    "1", 45,
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
                    "2", 64,
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
        )
    }
}