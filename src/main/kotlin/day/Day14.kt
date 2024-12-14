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
        return NotImplemented
        val robots = input.map(::parse)
        val max = Coordinate(robots.maxOf { it.position.x }, robots.maxOf { it.position.y })

        var wtf = robots
        for (steps in 0..10000) {
            wtf = wtf.map { it.copy(position = step(it, 1L, max)) }
            if (wtf.map { it.position }.containsBlockOf3x3(max)) {
                println(steps + 1)
                debugPrint(wtf.map { it.position }, max, false)
            }
        }
        return 0.asSuccess()
    }

    fun Collection<Coordinate>.containsBlockOf3x3(max: Coordinate): Boolean {
        for (y in 1..<max.y) {
            for (x in 1..<max.x) {
                if (this.containsAll(sequence {
                        for (dx in -1..1) {
                            for (dy in -1..1) {
                                yield(Coordinate(x + dx, y + dy))
                            }
                        }
                    }.toSet())) return true
            }
        }
        return false
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            12,
            0,
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