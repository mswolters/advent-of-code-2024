package day

object Day14 : Day {
    override fun part1(input: List<String>): Result {
        val robots = input.map(::parse)
        val max = Coordinate(robots.maxOf { it.position.x }, robots.maxOf { it.position.y })

        val endPositions = robots.map { step(it, 100L, max) }
        //debugPrint(endPositions, max)
        return endPositions
            .filter { it.x != max.x / 2 && it.y != max.y / 2 }
            .groupBy { Pair((it.x - 1) / (max.x / 2), (it.y - 1) / (max.y / 2)) }
            .map { it.value.count() }
            .reduce { a, b -> a * b }
            .asSuccess()
    }

    val regex = """p=(?<pX>\d+),(?<pY>\d+) v=(?<vX>-?\d+),(?<vY>-?\d+)""".toRegex()
    fun parse(input: String): Robot {
        val result = regex.matchEntire(input)!!
        return Robot(
            Coordinate(result.groups["pX"]!!.value.toLong(), result.groups["pY"]!!.value.toLong()),
            Coordinate(result.groups["vX"]!!.value.toLong(), result.groups["vY"]!!.value.toLong())
        )
    }

    fun step(robot: Robot, steps: Long, max: Coordinate): Coordinate {
        val position = robot.position + robot.velocity * steps
        return Coordinate(position.x.mod(max.x + 1), position.y.mod(max.y + 1))
    }

    fun debugPrint(endPositions: List<Coordinate>, max: Coordinate, blankMiddle: Boolean = true) {
        val counts = endPositions.groupBy { it }.mapValues { (_, v) -> v.size }
        for (y in 0..max.y) {
            for (x in 0..max.x) {
                print(
                    counts[Coordinate(x, y)]?.toInt()
                        ?: if (blankMiddle && (x == max.x / 2 || y == max.y / 2)) " " else "."
                )
            }
            println()
        }
    }

    data class Coordinate(val x: Long, val y: Long)

    operator fun Coordinate.times(times: Long) = Coordinate(x * times, y * times)
    operator fun Coordinate.plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)
    data class Robot(val position: Coordinate, val velocity: Coordinate)

    override fun part2(input: List<String>): Result {
        val robots = input.map(::parse)
        val max = Coordinate(robots.maxOf { it.position.x }, robots.maxOf { it.position.y })

        //suurely the robot danger level is lowest when they're forming a tree right
        return (0..10000L).map { i -> i to robots.map { step(it, i, max) } }.minByOrNull { (_, positions) ->
            positions.filter { it.x != max.x / 2 && it.y != max.y / 2 }
                .groupBy { Pair((it.x - 1) / (max.x / 2), (it.y - 1) / (max.y / 2)) }
                .map { it.value.count() }
                .reduce { a, b -> a * b }
        }
            ?.first
            .asSuccess()
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            12,
            7,
            """p=0,4 v=3,-3
p=6,3 v=-1,-3
p=10,3 v=-1,2
p=2,0 v=2,-1
p=0,0 v=1,3
p=3,0 v=-2,-2
p=7,6 v=-1,-3
p=3,0 v=-1,-2
p=9,3 v=2,3
p=7,3 v=-1,2
p=2,4 v=2,-3
p=9,5 v=-3,-3""".trimMargin()
                .lines()
        )
    }
}