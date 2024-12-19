package day

import findPath
import split

object Day19 : Day {
    override fun part1(input: List<String>): Result {
        val (possibilitiesString, targets) = input.split("")
        val possibilities = possibilitiesString.first().split(", ")

        return targets.count { target ->
            findPath(
                start = "",
                isEnd = { target == it },
                edgesForNode = {
                    possibilities.filter { pos -> target.startsWith(it + pos) }.map { pos -> (it + pos) to 1.0 }
                }) != null
        }
            .asSuccess()
    }

    override fun part2(input: List<String>): Result {
        val (possibilitiesString, targets) = input.split("")
        val possibilities = possibilitiesString.first().split(", ")

        // Turns out abusing a pathfinding algorithm to generate all possible words is stupid
        // Turns out generating all possible words is stupid in the first place

        val map = mutableMapOf<String, Long>()
        return targets
            .sumOf { findNumberOfWaysWordCanBeFormedFrom(it, possibilities, map) }
            .asSuccess()
    }

    fun findNumberOfWaysWordCanBeFormedFrom(
        word: String,
        possibilities: List<String>,
        memoization: MutableMap<String, Long>
    ): Long = memoization.getOrPut(word) {
        if (word.isEmpty()) return@getOrPut 1L

        return@getOrPut possibilities.filter { word.endsWith(it) }
            .sumOf { findNumberOfWaysWordCanBeFormedFrom(word.removeSuffix(it), possibilities, memoization) }
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            6,
            16,
            """r, wr, b, g, bwu, rb, gb, br

brwrr
bggr
gbbr
rrbgbr
ubwu
bwurrg
brgr
bbrgwb"""
                .lines()
        )
    }
}