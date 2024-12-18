package day

import asInts
import findPath
import neighbours

object Day18 : Day {
    override fun part1(input: List<String>): Result {
        val corruptedBytes = input.map { it.split(',').asInts() }.map { (x, y) -> Rectangle.Coordinate(x, y) }.toSet()
        val end = Rectangle.Coordinate(corruptedBytes.maxOf { it.x }, corruptedBytes.maxOf { it.y })

        val actualCorruptedBytes = corruptedBytes.take(if (input.size > 1000) 1024 else 12)

        val path = findPath(
            start = Rectangle.Coordinate(0, 0),
            isEnd = { it == end },
            edgesForNode = {
                it.neighbours().map { (_ ,c) -> c }
                    .filter { it.x in 0..end.x && it.y in 0..end.y }
                    .filter { it !in actualCorruptedBytes }
                    .map { it to 1.0 }
            })

        return path!!.length.toInt().asSuccess()
    }

    override fun part2(input: List<String>): Result {
        val indexableCorruptedBytes = input.map { it.split(',').asInts() }.map { (x, y) -> Rectangle.Coordinate(x, y) }
        val corruptedBytes = indexableCorruptedBytes.toSet()
        val end = Rectangle.Coordinate(corruptedBytes.maxOf { it.x }, corruptedBytes.maxOf { it.y })

        val firstFailingIndex = corruptedBytes.indices.first { i ->
            val actualCorruptedBytes = corruptedBytes.take(i + 1)
            val path = findPath(
                start = Rectangle.Coordinate(0, 0),
                isEnd = { it == end },
                edgesForNode = {
                    it.neighbours().map { (_, c) -> c }
                        .filter { it.x in 0..end.x && it.y in 0..end.y }
                        .filter { it !in actualCorruptedBytes }
                        .map { it to 1.0 }
                })
            path == null
        }

        return indexableCorruptedBytes[firstFailingIndex].let { "${it.x},${it.y}" }.asSuccess()
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            22,
            "6,1",
            """5,4
4,2
4,5
3,0
2,1
6,3
2,4
1,5
0,6
3,3
2,6
5,1
1,2
5,5
2,5
6,5
1,4
0,4
6,4
1,1
6,1
1,0
0,5
1,6
2,0"""
                .lines()
        )
    }
}