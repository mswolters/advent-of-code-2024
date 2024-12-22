package day

object Day22 : Day {
    override fun part1(input: List<String>): Result {
        return input.asSequence().map { it.toLong() }
            .map { i ->
                (0..<2000).fold(i) { j, _ ->
                    j.let { it.mix(it * 64).prune() }
                        .let { it.mix(it / 32).prune() }
                        .let { it.mix(it * 2048).prune() }
                }
            }.sum().asSuccess()
    }

    fun Long.mix(i: Long): Long = xor(i)

    fun Long.prune() = and(0b1111_1111_1111_1111_1111_1111)

    override fun part2(input: List<String>): Result {
        return NotImplemented
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            37327623,
            0,
            """1
10
100
2024"""
                .lines()
        )
    }
}