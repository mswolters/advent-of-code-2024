package day

import Rectangle
import cartesianProduct
import toRectangle

object Day08 : Day {
    override fun part1(input: List<String>): Result {
        val map = input.map { it.toCharArray().toList() }.toRectangle()
        return map.indexedIterator().asSequence()
            // Create a sequence of nodes of the same character
            .filter { (_, l) -> l != '.' }
            .groupBy { (_, l) -> l }
            .mapValues { (_, l) -> l.map { it.first } }
            // Calculate all possible antinodes for that character
            .flatMap { (_, nodes) -> calculateAntinodes(nodes) }
            // Only keep antinodes which are on the map
            .filter { map.isInBounds(it) }
            // Deduplicate
            .distinct()
            .count()
            .asSuccess()
    }

    fun calculateAntinodes(nodes: List<Rectangle.Coordinate>): Sequence<Rectangle.Coordinate> {
        return nodes.cartesianProduct(nodes).filter { (l, r) -> l != r }
            .flatMap { (a, b) ->
                listOf(
                    Rectangle.Coordinate(a.x + (a.x - b.x), a.y + (a.y - b.y)),
                    Rectangle.Coordinate(b.x + (b.x - a.x), b.y + (b.y - a.y))
                )
            }
    }

    override fun part2(input: List<String>): Result {
        val map = input.map { it.toCharArray().toList() }.toRectangle()
        return map.indexedIterator().asSequence()
            // Create a sequence of nodes of the same character
            .filter { (_, l) -> l != '.' }
            .groupBy { (_, l) -> l }
            .mapValues { (_, l) -> l.map { it.first } }
            // Calculate all possible antinodes for that character
            .flatMap { (_, nodes) -> calculateAllAntinodes(map, nodes) }
            // Deduplicate
            .distinct()
            .count()
            .asSuccess()
    }

    fun calculateAllAntinodes(map: Rectangle<*>, nodes: List<Rectangle.Coordinate>): Sequence<Rectangle.Coordinate> {
        return nodes.cartesianProduct(nodes).filter { (l, r) -> l != r }
            .flatMap { (a, b) ->
                sequence {
                    for (i in map.indices) {
                        val positiveNode = Rectangle.Coordinate(a.x + (a.x - b.x) * i, a.y + (a.y - b.y) * i)
                        if (map.isInBounds(positiveNode)) yield(positiveNode) else break
                    }
                    for (i in map.indices) {
                        val negativeNode = Rectangle.Coordinate(b.x + (b.x - a.x) * i, b.y + (b.y - a.y) * i)
                        if (map.isInBounds(negativeNode)) yield(negativeNode) else break
                    }
                }
            }
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            14,
            34,
            """............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............"""
                .lines()
        )
    }
}