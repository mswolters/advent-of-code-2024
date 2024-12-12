package day

import MutableRectangle
import Rectangle
import Side
import neighbour
import neighbours
import toMutableRectangle
import toRectangle

object Day12 : Day {
    override fun part1(input: List<String>): Result {
        val map = input.map { it.toCharArray().asList() }.toRectangle()

        val regions = findRegions(map)
        val circumferencePerRegion = mutableMapOf<Set<Rectangle.Coordinate>, Int>()
        fun increaseCircumference(coord: Rectangle.Coordinate?) {
            if (coord != null) {
                val region = regions.first { coord in it }
                circumferencePerRegion[region] = circumferencePerRegion.getOrDefault(region, 0) + 1
            }
        }
        rayCast(map) { old, new, _ -> increaseCircumference(old?.first); increaseCircumference(new?.first) }

        return circumferencePerRegion.map { (region, circumference) -> region.size * circumference }.sum().asSuccess()
    }

    fun findRegions(map: Rectangle<Char>): List<Set<Rectangle.Coordinate>> {
        val mutableMap = map.toMutableRectangle()
        val regions = mutableListOf<Set<Rectangle.Coordinate>>()
        while (true) {
            val newRegion = mutableMap.indexedIterator().asSequence().firstOrNull { (_, l) -> l != '.' }
            if (newRegion == null) break
            val (coord, letter) = newRegion
            regions += floodFillRegion(mutableMap, coord, letter, '.')
        }
        return regions
    }

    fun floodFillRegion(
        map: MutableRectangle<Char>,
        coordinate: Rectangle.Coordinate,
        char: Char,
        replaceWith: Char
    ): Set<Rectangle.Coordinate> {
        map[coordinate] = replaceWith
        val newCoords = map.neighbours(coordinate).values.filter { (l, _) -> l == char }.map { (_, c) -> c }.toSet()
        if (newCoords.isEmpty()) return setOf(coordinate)

        return setOf(coordinate) + newCoords.flatMap { floodFillRegion(map, it, char, replaceWith) }
    }

    fun rayCast(
        map: Rectangle<Char>,
        onChange: (old: Pair<Rectangle.Coordinate, Char>?, new: Pair<Rectangle.Coordinate, Char>?, rayDirection: Side) -> Unit
    ) {
        for (x in 0..<map.width) {
            var old: Pair<Rectangle.Coordinate, Char>? = null
            for (y in 0..map.height) {
                val new = if (map.isInBounds(x, y)) Rectangle.Coordinate(x, y) to map[x, y] else null
                if (old?.second != new?.second) onChange(old, new, Side.South)
                old = new
            }
        }
        for (y in 0..<map.height) {
            var old: Pair<Rectangle.Coordinate, Char>? = null
            for (x in 0..map.width) {
                val new = if (map.isInBounds(x, y)) Rectangle.Coordinate(x, y) to map[x, y] else null
                if (old?.second != new?.second) onChange(old, new, Side.East)
                old = new
            }
        }
    }

    override fun part2(input: List<String>): Result {
        val map = input.map { it.toCharArray().asList() }.toRectangle()

        val regions = findRegions(map)
        val circumferencePerRegion = mutableMapOf<Set<Rectangle.Coordinate>, Int>()
        fun increaseCircumferenceIfChangedInDirection(spot: Pair<Rectangle.Coordinate, Char>?, direction: Side, isNew: Boolean) {
            if (spot != null) {
                val (coord, char) = spot
                val previousSide = when (direction) {
                    Side.North -> TODO()
                    Side.East -> Side.North
                    Side.South -> Side.West
                    Side.West -> TODO()
                }
                // previousValue looks at the neighbour in the previous ray
                val previousValue = if (map.isInBounds(coord.neighbour(previousSide))) map[coord.neighbour(previousSide)] else null
                // previousNextValue looks at the previous ray, taking either a step back or forward depending on which inside corner we should detect
                val previousNextCoord = coord.neighbour(previousSide).neighbour(if (isNew) direction.opposite() else direction)
                val previousNextValue = if (map.isInBounds(previousNextCoord)) map[previousNextCoord] else null

                // Increase the fence count if we detect an inside corner or a wall
                if (previousValue != char || previousNextValue == char) {
                    val region = regions.first { coord in it }
                    circumferencePerRegion[region] = circumferencePerRegion.getOrDefault(region, 0) + 1
                }
            }
        }
        rayCast(map) { old, new, direction ->
            increaseCircumferenceIfChangedInDirection(old, direction, false)
            increaseCircumferenceIfChangedInDirection(new, direction, true)
        }

        return circumferencePerRegion.map { (region, circumference) -> region.size * circumference }.sum().asSuccess()
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            1930,
            368,
            """RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE"""
                .lines(),
            """AAAAAA
AAABBA
AAABBA
ABBAAA
ABBAAA
AAAAAA""".lines()
        )
    }
}