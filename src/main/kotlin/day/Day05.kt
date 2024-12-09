package day

import asInts
import split

object Day05 : Day {
    override fun part1(input: List<String>): Result {
        val toParse = input.split("")

        val rules = toParse[0].map { it.split("|").asInts() }
        val orders = toParse[1].map { it.split(",").asInts() }

        return orders.filter { isOrderValid(it, rules) }.sumOf { it[it.size / 2] }.asSuccess()
    }

    fun isOrderValid(order: List<Int>, rules: List<List<Int>>): Boolean {
        order.forEachIndexed { index, page ->
            val relevantRules = rules.filter { it.first() == page }
            if (relevantRules.any { it[1] in order.take(index) }) return false
        }
        return true
    }

    override fun part2(input: List<String>): Result {
        val toParse = input.split("")

        val rules = toParse[0].map { it.split("|").asInts() }
        val orders = toParse[1].map { it.split(",").asInts() }

        val invalidOrders = orders.filter { !isOrderValid(it, rules) }

        return invalidOrders.map { fixOrder(it, rules) }.sumOf { it[it.size / 2] }.asSuccess()
    }

    fun fixOrder(order: List<Int>, rules: List<List<Int>>): List<Int> {
        val result = mutableListOf<Int>()

        for (page in order) {
            val relevantRules = rules.filter { it.first() == page }
            // Find the position of the first page that's already in result which causes a rule break. Add to the end if there are none
            val insertionIndex = relevantRules.map { it[1] }.map { result.indexOf(it) }.filter { it != -1 }.minOrNull() ?: result.size
            result.add(insertionIndex, page)
        }

        return result
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            143,
            123,
            """47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47"""
                .lines()
        )
    }
}