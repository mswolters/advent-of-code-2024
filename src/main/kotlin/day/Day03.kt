package day

object Day03 : Day {
    override fun part1(input: List<String>): Result {
        val regex = """mul\((?<left>[0-9]+),(?<right>[0-9]+)\)""".toRegex(setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.MULTILINE))
        return regex.findAll(input.joinToString("\n")).map { it.groupValues[1].toInt() * it.groupValues[2].toInt() }.sum().asSuccess()
    }

    override fun part2(input: List<String>): Result {
        // By capturing everything between "don't()" and "do()" it won't be captured into the "mul" group, so it's effectively filtered out
        val regex = """(mul\((?<left>[0-9]+),(?<right>[0-9]+)\))|(don't\(\).*?(do\(\)|\z))""".toRegex(setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.MULTILINE))
        return regex.findAll(input.joinToString("\n"))
            .filter { it.groupValues[1].isNotEmpty() }
            .map { it.groupValues[2].toInt() * it.groupValues[3].toInt() }
            .sum().asSuccess()
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            161,
            48,
            """xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"""
                .lines(),
            """xmul(2,4)&mul[3,7]!^don't()_mul(5,5)
                |+mul(32,64](mul(11,8)undo()?mul(8,5))""".trimMargin()
                .lines()
        )
    }
}