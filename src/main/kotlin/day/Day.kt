package day

import readInput

interface Day {

    fun part1(input: List<String>): Result
    fun part2(input: List<String>): Result

    fun testData(): TestData

    val name: String get() = this.javaClass.kotlin.simpleName!!
    fun input(): List<String> {
        return readInput(name)
    }

    data class TestData(val part1Tests: List<Test>, val part2Tests: List<Test>) {
        data class Test(val name: String, val expectedOutput: Any, val input: List<String>)
        constructor(expected1: String, expected2: String, data: List<String>, data2: List<String> = data) : this(listOf(Test("Test1", expected1, data)), listOf(Test("Test1", expected2, data2)))
        constructor(expected1: Any, expected2: Any, data: List<String>, data2: List<String> = data) : this(expected1.toString(), expected2.toString(), data, data2)
    }

}

sealed interface Result

@JvmInline
value class Success(val result: String) : Result
fun <T> T.asSuccess() = Success("$this")
fun String.asSuccess() = Success(this)

data object NotImplemented : Result
@JvmInline
value class Crash(val exception: Exception) : Result