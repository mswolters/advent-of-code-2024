package day

import asLongs
import base10Length
import pow

object Day11 : Day {
    override fun part1(input: List<String>): Result {
        val initialStones = input.first().split(" ").asLongs()

        var stones = initialStones.asSequence()
        repeat(25) {
            stones = blink(stones)
        }
        return stones.count().asSuccess()
    }

    fun blink(stones: Sequence<Long>): Sequence<Long> {
        return stones.flatMap { replaceStone(it) }
    }

    fun replaceStone(stone: Long): List<Long> {
        check(stone >= 0)

        if (stone == 0L) return listOf(1L)
        val length = stone.base10Length()
        if (length % 2 == 0) {
            val lengthValue = 10L.pow(length / 2)
            return listOf(stone / lengthValue, stone % lengthValue)
        }
        return listOf(stone * 2024)
    }

    override fun part2(input: List<String>): Result {
        val initialStones = input.first().split(" ").asLongs()

        return initialStones.sumOf { calculateCountForStone(it, 75) }.asSuccess()
    }

    private val _calculateCountForStone = mutableMapOf<Pair<Long, Int>, Long>()
    fun calculateCountForStone(stone: Long, depth: Int): Long = _calculateCountForStone.getOrPut(Pair(stone, depth)) {
        if (depth == 0) return@getOrPut 1L
        if (stone == 0L) return@getOrPut calculateCountForStone(1L, depth - 1)
        val length = stone.base10Length()
        if (length % 2 == 0) {
            val lengthValue = 10L.pow(length / 2)
            return@getOrPut calculateCountForStone(stone / lengthValue, depth - 1) + calculateCountForStone(stone % lengthValue, depth - 1)
        }
        return@getOrPut calculateCountForStone(stone * 2024L, depth - 1)
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            55312,
            65601038650482L,
            """125 17"""
                .lines()
        )
    }
}