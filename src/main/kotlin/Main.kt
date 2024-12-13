import day.*
import day.Day.TestData
import kotlin.collections.map
import kotlin.time.Duration
import kotlin.time.measureTime

const val ContinueAfterTestThrows = false

fun main(args: Array<String>) {
    val days = listOf(
        Day01, Day02, Day03, Day04, Day05,
        Day06, Day07, Day08, Day09, Day10,
        Day11, Day12, Day13, Day14, Day15,
        Day16, Day17, Day18, Day19, Day20,
        Day21, Day22, Day23, Day24, Day25,
    )

    days.reversed().forEach { execute(it) }
}

fun execute(day: Day) {
    val testData = day.testData()
    val part1TestsSuccess = checkAndPrintTestResults(day, testData.part1Tests.map { it to day::part1.timedRun(it.input) })
    if (part1TestsSuccess) {
        val result = day::part1.timedRun(day.input())
        when (result.result) {
            is Crash -> throw result.result.exception
            NotImplemented -> throw IllegalStateException("Runs should never be NotImplemented if tests succeed")
            is Success -> {
                println("${day.name} Part 1: ${result.result.result}")
                println("Solving took ${result.timeTaken}")
            }
        }
    }
    val part2TestsSuccess = checkAndPrintTestResults(day, testData.part2Tests.map { it to day::part2.timedRun(it.input) })
    if (part2TestsSuccess) {
        val result = day::part2.timedRun(day.input())
        when (result.result) {
            is Crash -> throw result.result.exception
            NotImplemented -> throw IllegalStateException("Runs should never be NotImplemented if tests succeed")
            is Success -> {
                println("${day.name} Part 2: ${result.result.result}")
                println("Solving took ${result.timeTaken}")
            }
        }
    }
}

data class RunResult(val result: Result, val timeTaken: Duration)

fun ((List<String>) -> Result).timedRun(input: List<String>): RunResult {
    val result: Result
    val timeTaken = measureTime {
        result = try {
            this(input)
        } catch (e: Exception) {
            Crash(e)
        }
    }
    return RunResult(result, timeTaken)
}

fun checkAndPrintTestResults(day: Day, results: List<Pair<TestData.Test, RunResult>>): Boolean {
    return results.map { (test, result) ->
        when (result.result) {
            is Crash -> {
                println("${day.name} ${test.name} crashed with exception ${result.result.exception} after ${result.timeTaken}")
                @Suppress("KotlinConstantConditions")
                if (!ContinueAfterTestThrows) throw result.result.exception
                false
            }

            NotImplemented -> false
            is Success -> if (result.result.result == test.expectedOutput.toString()) {
                //println("${day.name} ${test.name} successful after ${result.timeTaken}")
                true
            } else {
                println("${day.name} ${test.name} failed. Expected ${test.expectedOutput}, got ${result.result.result}")
                false
            }
        }
    }.all { it }
}