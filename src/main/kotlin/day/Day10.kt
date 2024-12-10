package day

import MutableRectangle
import neighbours
import toRectangle

object Day10 : Day {
    override fun part1(input: List<String>): Result {
        val map = input.map { line -> line.toCharArray().map { it.digitToInt() } }.toRectangle()
        val reachabilityMap = MutableRectangle(map.width, map.height) { _, _ -> setOf<Rectangle.Coordinate>() }

        // Each 9 is always reachable from a nine, so set those squares to 1 on the reachableMap
        map.indexedIterator().asSequence().filter { (_, n) -> n == 9 }
            .forEach { (c, n) -> reachabilityMap[c] = setOf(c) }

        for (i in 8 downTo 0) {
            map.indexedIterator().asSequence().filter { (_, n) -> n == i }
                .forEach { (c, n) ->
                    reachabilityMap[c] = c.neighbours()
                        .filter { (_, it) -> map.isInBounds(it) }
                        .map { (_, it) -> if (map[it] == n + 1) reachabilityMap[it] else emptySet() }
                        .fold(setOf()) { acc, it -> acc + it }
                }
        }

        return map.indexedIterator().asSequence().filter { (_, n) -> n == 0 }
            .map { (c, _) -> reachabilityMap[c].count() }
            .sum().asSuccess()
    }

    override fun part2(input: List<String>): Result {
        val map = input.map { line -> line.toCharArray().map { it.digitToInt() } }.toRectangle()
        val reachabilityMap = MutableRectangle(map.width, map.height) { _, _ -> 0 }

        // Each 9 is always reachable from a nine, so set those squares to 1 on the reachableMap
        map.indexedIterator().asSequence().filter { (_, n) -> n == 9 }
            .forEach { (c, n) -> reachabilityMap[c] = 1 }

        for (i in 8 downTo 0) {
            map.indexedIterator().asSequence().filter { (_, n) -> n == i }
                .forEach { (c, n) ->
                    reachabilityMap[c] = c.neighbours()
                        .filter { (_, it) -> map.isInBounds(it) }
                        .map { (_, it) -> if (map[it] == n + 1) reachabilityMap[it] else 0 }
                        .sum()
                }
        }

        return map.indexedIterator().asSequence().filter { (_, n) -> n == 0 }
            .map { (c, _) -> reachabilityMap[c] }
            .sum().asSuccess()
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            36,
            81,
            """89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732"""
                .lines()
        )
    }
}