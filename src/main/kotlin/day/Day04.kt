package day

import Rectangle
import Side
import coordinateFrom
import edges
import isEdge
import neighbours
import toRectangle

object Day04 : Day {
    override fun part1(input: List<String>): Result {
        val grid = input.map { it.toCharArray().asList() }.toRectangle()
        val word = "XMAS".toCharArray().toList()
        return grid.indexedIterator().asSequence()
            .map { (c, _) -> findWord(grid, c, word) }
            .sum().asSuccess()
    }

    fun findWord(grid: Rectangle<Char>, coordinate: Rectangle.Coordinate, word: List<Char>): Int {
        if (grid[coordinate] != word.first()) return 0
        val restOfWord = word.drop(1)
        return coordinate.neighbours(true).count { (direction, c) -> findRestOfWord(grid, c, restOfWord, direction) }
    }

    fun findRestOfWord(grid: Rectangle<Char>, coordinate: Rectangle.Coordinate, restOfWord: List<Char>, direction: Set<Side>): Boolean {
        if (restOfWord.isEmpty()) return true
        if (!grid.isInBounds(coordinate)) return false
        if (grid[coordinate] != restOfWord.first()) return false
        return findRestOfWord(grid, direction.coordinateFrom(coordinate), restOfWord.drop(1), direction)
    }

    override fun part2(input: List<String>): Result {
        val grid = input.map { it.toCharArray().asList() }.toRectangle()
        return grid.indexedIterator().asSequence().filter { (c, l) -> !grid.isEdge(c) && l == 'A' }.count { (c, _) -> isXmas(grid, c) }.asSuccess()
    }

    fun isXmas(grid: Rectangle<Char>, coordinate: Rectangle.Coordinate): Boolean {
        val northEast = Side.NorthEast.coordinateFrom(coordinate)
        val northWest = Side.NorthWest.coordinateFrom(coordinate)
        val southEast = Side.SouthEast.coordinateFrom(coordinate)
        val southWest = Side.SouthWest.coordinateFrom(coordinate)
        val letters = setOf('S', 'M')

        return grid[northEast] != grid[southWest] && grid[northEast] in letters && grid[southWest] in letters &&
        grid[northWest] != grid[southEast] && grid[northWest] in letters && grid[southEast] in letters
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            18,
            9,
            """MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX"""
                .lines()
        )
    }
}