package day

import cartesianProduct
import split

object Day13 : Day {
    override fun part1(input: List<String>): Result {
        val machines = input.split("").map(::parseMachine)

        return machines.sumOf(::minTokensToSolve).asSuccess()
    }

    val regex = """.*X[+=](?<X>\d+), Y[+=](?<Y>\d+)""".toRegex()
    fun parseMachine(input: List<String>): Machine {
        val (a, b, prize) = input.mapNotNull(regex::matchEntire)
            .map { Coordinate(it.groups["X"]!!.value.toLong(), it.groups["Y"]!!.value.toLong()) }
        return Machine(a, b, prize)
    }

    fun minTokensToSolve(machine: Machine): Long {
        // Threre is definitely a mathy way to do this, but let's brute force
        return (0..100L).cartesianProduct(0..100L)
            .filter { (a, b) -> machine.solves(a, b) }
            .map { (a, b) -> 3 * a + b }
            .minOrNull() ?: 0L
    }

    operator fun Coordinate.times(times: Long) = Coordinate(x * times, y * times)
    operator fun Coordinate.plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)

    data class Machine(
        val buttonA: Coordinate,
        val buttonB: Coordinate,
        val prize: Coordinate
    )

    fun Machine.solves(aPresses: Long, bPresses: Long) = buttonA * aPresses + buttonB * bPresses == prize

    data class Coordinate(val x: Long, val y: Long)

    fun minTokensToSolveSmart(machine: Machine): Long {
        // Using https://www2.math.uconn.edu/~stein/math1070/Slides/math1070-130notes.pdf
        // prizeX = n * A.x + m * B.x
        // prizeY = n * A.y + m * B.y

        // aPresses * A.x + bPresses * B.x = prizeX
        // aPresses * A.y + bPresses * B.y = prizeY

        // bPresses = (prizeX * A.y - prizeY * A.x) / (B.x * A.y - A.x * B.y)
        // aPresses * A.y = prizeY - bPresses * B.y
        // aPresses = (prizeY - bPresses * B.y) / A.Y
        with(machine) {
            // use big decimals to eliminate all possible rounding errors
            val bPresses =
                ((prize.x.toBigDecimal() * buttonA.y.toBigDecimal() - prize.y.toBigDecimal() * buttonA.x.toBigDecimal()) /
                        (buttonB.x.toBigDecimal() * buttonA.y.toBigDecimal() - buttonA.x.toBigDecimal() * buttonB.y.toBigDecimal())).toLong()
            val aPresses =
                ((prize.y.toBigDecimal() - bPresses.toBigDecimal() * buttonB.y.toBigDecimal()) /
                    buttonA.y.toBigDecimal()).toLong()

            return if (buttonA * aPresses + buttonB * bPresses == prize) 3 * aPresses + bPresses else 0
        }
    }

    override fun part2(input: List<String>): Result {
        val offset = Coordinate(10000000000000L, 10000000000000L)
        val machines = input.split("").map(::parseMachine).map { it.copy(prize = it.prize + offset) }

        return machines.sumOf(::minTokensToSolveSmart).asSuccess()
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            480,
            875318608908L,
            """
                Button A: X+94, Y+34
                Button B: X+22, Y+67
                Prize: X=8400, Y=5400
                
                Button A: X+26, Y+66
                Button B: X+67, Y+21
                Prize: X=12748, Y=12176
                
                Button A: X+17, Y+86
                Button B: X+84, Y+37
                Prize: X=7870, Y=6450
                
                Button A: X+69, Y+23
                Button B: X+27, Y+71
                Prize: X=18641, Y=10279
                """
                .trimIndent()
                .lines()
        )
    }
}