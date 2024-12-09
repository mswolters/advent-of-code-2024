package day

import asInts
import kotlin.math.abs

object Day02 : Day {
    override fun part1(input: List<String>): Result {
        return input.map { it.split(" ").asInts() }.map { isSafe(it) }.count { it }.asSuccess()
    }

    fun isSafe(report: List<Int>): Boolean {
        val differences = report.zipWithNext { a, b -> a - b }
        return differences.all { abs(it) < 4 } && (differences.all { it > 0 } || differences.all { it < 0 })
    }

    override fun part2(input: List<String>): Result {
        return input.map { it.split(" ").asInts() }.map { isDampenedSafe(it) }.count { it }.asSuccess()
    }

    fun isDampenedSafe(report: List<Int>): Boolean {
        if (isSafe(report)) return true
        for (i in report.indices) {
            if (isSafe(report.filterIndexed { index, _ -> index != i })) return true
        }
        return false
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            2,
            4,
            """7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9"""
                .lines()
        )
    }
}