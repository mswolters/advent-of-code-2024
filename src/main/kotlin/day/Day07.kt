package day

import asLongs
import pow
import readBit

object Day07 : Day {
    override fun part1(input: List<String>): Result {
        return input.asSequence()
            .map { it.split(": ") }
            .map { it.first().toLong() to it[1].split(" ").asLongs() }
            .filter { (check, numbers) -> isEquationPossible(check, numbers) }
            .sumOf { (check, _) -> check }
            .asSuccess()
    }

    fun isEquationPossible(check: Long, numbers: List<Long>): Boolean {
        val numberOfOperators = numbers.size - 1
        for (operations in 0..<1.shl(numberOfOperators)) {
            val result = numbers.reduceIndexed { index, acc, number ->
                if (operations.readBit(index - 1)) {
                    acc * number
                } else {
                    acc + number
                }
            }
            if (result == check) return true
        }
        return false
    }

    override fun part2(input: List<String>): Result {
        return input.asSequence()
            .map { it.split(": ") }
            .map { it.first().toLong() to it[1].split(" ").asLongs() }
            .filter { (check, numbers) -> isEquationPossibleWithConcatenation(check, numbers) }
            .sumOf { (check, _) -> check }
            .asSuccess()
    }

    fun isEquationPossibleWithConcatenation(check: Long, numbers: List<Long>): Boolean {
        val numberOfOperators = numbers.size - 1
        for (operations in 0..<3.pow(numberOfOperators)) {
            val result = numbers.reduceIndexed { index, acc, number ->
                val op = (operations / 3.pow(index -  1)) % 3
                when (op) {
                    0 -> acc + number
                    1 -> acc * number
                    2 -> "$acc$number".toLong()
                    else -> throw IllegalStateException()
                }
            }
            if (result == check) return true
            if (result > check) return false
        }
        return false
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            3749,
            11387,
            """190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20"""
                .lines()
        )
    }
}