package day

import asInts
import split

object Day17 : Day {
    override fun part1(input: List<String>): Result {
        val (registers, programLine) = input.split("")

        val (regA, regB, regC) = registers.map { it.split(": ").last().toLong() }
        val program = programLine.first().split(": ").last().split(",").asInts()

        val output = mutableListOf<Int>()

        val computer = Computer(regA, regB, regC, program) {
            output += it
        }
        computer.run()

        return output.joinToString(",").asSuccess()
    }

    class Computer(
        var regA: Long,
        var regB: Long,
        var regC: Long,
        val program: List<Int>,
        val onOutput: (out: Int) -> Unit
    ) {
        var ip = 0

        fun run() {
            while (ip < program.size) {
                when (val op = program[ip]) {
                    0 -> adv()
                    1 -> bxl()
                    2 -> bst()
                    3 -> {
                        jnz()
                    }
                    4 -> bxc()
                    5 -> out()
                    6 -> bdv()
                    7 -> cdv()
                    else -> throw IllegalArgumentException("Unknown op $op")
                }
            }
        }

        fun read(): Int = program[ip + 1]
        fun readCombo(): Long {
            return when (val data = read()) {
                in 0..3 -> data.toLong()
                4 -> regA
                5 -> regB
                6 -> regC
                else -> throw IllegalStateException("$data read invalid")
            }
        }

        fun incrementIP() {
            ip += 2
        }

        fun executeDivision(): Long = regA / (1.shl(readCombo().toInt()))

        fun adv() {
            regA = executeDivision()
            incrementIP()
        }

        fun bxl() {
            regB = regB xor read().toLong()
            incrementIP()
        }

        fun bst() {
            regB = readCombo() % 8
            incrementIP()
        }

        fun jnz() {
            if (regA == 0L) {
                incrementIP()
            } else {
                ip = read()
            }
        }

        fun bxc() {
            regB = regB xor regC
            incrementIP()
        }

        fun out() {
            onOutput((readCombo() % 8).toInt())
            incrementIP()
        }

        fun bdv() {
            regB = executeDivision()
            incrementIP()
        }

        fun cdv() {
            regC = executeDivision()
            incrementIP()
        }

    }

    fun debugPrint(program: List<Int>) {
        fun read(i: Int): String = program[i + 1].toString()

        fun readCombo(i: Int): String {
            return when (val data = program[i + 1]) {
                in 0..3 -> data.toString()
                4 -> "regA"
                5 -> "regB"
                6 -> "regC"
                else -> throw IllegalStateException("$data read invalid")
            }
        }

        for (i in 0..<program.size step 2) {
            val (op, value) = when (program[i]) {
                0 -> "adv" to readCombo(i)
                1 -> "bxl" to read(i)
                2 -> "bst" to readCombo(i)
                3 -> "jnz" to read(i)
                4 -> "bxc" to ""
                5 -> "out" to readCombo(i)
                6 -> "bdv" to readCombo(i)
                7 -> "cdv" to readCombo(i)
                else -> throw IllegalArgumentException()
            }
            println("$op $value")
        }
    }

    override fun part2(input: List<String>): Result {
        return NotImplemented
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            "4,6,3,5,6,3,5,2,1,0",
            0,
            """
                Register A: 729
                Register B: 0
                Register C: 0
                
                Program: 0,1,5,4,3,0
                """
                .trimIndent()
                .lines()
        )
    }
}