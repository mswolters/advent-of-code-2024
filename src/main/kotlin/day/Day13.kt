package day

import Rectangle
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

    override fun part2(input: List<String>): Result {
        return NotImplemented
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            480,
            0,
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