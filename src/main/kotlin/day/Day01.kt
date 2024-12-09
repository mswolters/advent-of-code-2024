package day

import asInts
import transpose
import kotlin.math.abs

object Day01 : Day {

    override fun part1(input: List<String>): Result {
        val (left, right) = input.map { line -> line.split("   ").asInts() }.transpose().map { it.sorted() }
        return left.zip(right) { a, b -> abs(a - b) }.sum().asSuccess()
    }

    override fun part2(input: List<String>): Result {
        val (left, right) = input.map { line -> line.split("   ").asInts() }.transpose()
        val rightCounts = right.groupBy { it }.mapValues { it.value.count() }
        return left.sumOf { it * rightCounts.getOrDefault(it, 0) }.asSuccess()
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            11,
            31,
            """3   4
4   3
2   5
1   3
3   9
3   3"""
                .lines()
        )
    }
}