package day

import MutableRectangle
import Rectangle
import Side
import coordinateNextTo
import isVertical
import split
import toMutableRectangle

object Day15 : Day {
    override fun part1(input: List<String>): Result {
        val (mapLines, moveLines) = input.split("")

        val map = mapLines.map { it.toCharArray().asList() }.toMutableRectangle()

        val moves = moveLines.flatMap {
            it.toCharArray().map {
                when (it) {
                    '<' -> Side.West
                    'v' -> Side.South
                    '^' -> Side.North
                    '>' -> Side.East
                    else -> throw IllegalArgumentException("$it unknown")
                }
            }
        }

        var currentPosition = map.indexedIterator().asSequence().first { (_, l) -> l == '@' }.first
        moves.forEach { currentPosition = move(map, currentPosition, it) }

        return map.indexedIterator().asSequence()
            .filter { (_, l) -> l == 'O' }
            .map { (c, _) -> c.x + 100 * c.y }.sum()
            .asSuccess()
    }

    fun move(map: MutableRectangle<Char>, coordinate: Rectangle.Coordinate, direction: Side): Rectangle.Coordinate {
        val coordinateInDirection = direction.coordinateNextTo(coordinate)

        fun executeMove(): Rectangle.Coordinate {
            map[coordinateInDirection] = map[coordinate]
            map[coordinate] = '.'
            return coordinateInDirection
        }

        return when (map[coordinateInDirection]) {
            'O' ->
                if (move(map, coordinateInDirection, direction) != coordinateInDirection) {
                    executeMove()
                } else coordinate

            '.' -> executeMove()
            '#' -> coordinate
            else -> throw IllegalArgumentException("Unknown char ${map[coordinateInDirection]}")
        }
    }

    override fun part2(input: List<String>): Result {
        val (mapLines, moveLines) = input.split("")

        val map = mapLines.map {
            it.toCharArray().asList().flatMap {
                when (it) {
                    'O' -> listOf('[', ']')
                    '#' -> listOf('#', '#')
                    '.' -> listOf('.', '.')
                    '@' -> listOf('@', '.')
                    else -> throw IllegalArgumentException("Unknown char $it")
                }
            }
        }.toMutableRectangle()

        val moves = moveLines.flatMap {
            it.toCharArray().map {
                when (it) {
                    '<' -> Side.West
                    'v' -> Side.South
                    '^' -> Side.North
                    '>' -> Side.East
                    else -> throw IllegalArgumentException("$it unknown")
                }
            }
        }

        var currentPosition = map.indexedIterator().asSequence().first { (_, l) -> l == '@' }.first
        moves.forEach {
            //println("Move $it:")
            if (canMove(map, currentPosition, it)) {
                executeMove2(map, currentPosition, it)
                currentPosition = it.coordinateNextTo(currentPosition)
            }
            //println(map)
        }

        return map.indexedIterator().asSequence()
            .filter { (_, l) -> l == '[' }
            .map { (c, _) -> c.x + 100 * c.y }.sum()
            .asSuccess()
    }

    fun canMove(map: Rectangle<Char>, coordinate: Rectangle.Coordinate, direction: Side): Boolean {
        val coordinateInDirection = direction.coordinateNextTo(coordinate)

        return when (val c = map[coordinateInDirection]) {
            '[', ']' -> if (direction.isVertical()) {
                canMove(map, coordinateInDirection, direction) &&
                        if (c == '[')
                            canMove(map, coordinateInDirection.copy(x = coordinateInDirection.x + 1), direction)
                        else
                            canMove(map, coordinateInDirection.copy(x = coordinateInDirection.x - 1), direction)
            } else canMove(map, coordinateInDirection, direction)

            '.' -> true
            '#' -> false
            else -> throw IllegalArgumentException("Unknown char $c")
        }
    }

    fun executeMove2(map: MutableRectangle<Char>, coordinate: Rectangle.Coordinate, direction: Side) {
        val coordinateInDirection = direction.coordinateNextTo(coordinate)
        when (map[coordinateInDirection]) {
            '[' -> {
                executeMove2(map, coordinateInDirection, direction)
                if (direction.isVertical()) {
                    executeMove2(map, Side.East.coordinateNextTo(coordinateInDirection), direction)
                }
            }

            ']' -> {
                executeMove2(map, coordinateInDirection, direction)
                if (direction.isVertical()) {
                    executeMove2(map, Side.West.coordinateNextTo(coordinateInDirection), direction)
                }
            }
        }
        map[coordinateInDirection] = map[coordinate]
        map[coordinate] = '.'
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            listOf(
                Day.TestData.Test(
                    "Small", 2028,
                    """
                        ########
                        #..O.O.#
                        ##@.O..#
                        #...O..#
                        #.#.O..#
                        #...O..#
                        #......#
                        ########

                        <^^>>>vv<v>>v<<
                    """.trimIndent().lines()
                ),
                Day.TestData.Test(
                    "Larger", 10092,
                    """
                        ##########
                        #..O..O.O#
                        #......O.#
                        #.OO..O.O#
                        #..O@..O.#
                        #O#..O...#
                        #O..O..O.#
                        #.OO.O.OO#
                        #....O...#
                        ##########

                        <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
                        vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
                        ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
                        <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
                        ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
                        ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
                        >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
                        <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
                        ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
                        v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
                    """.trimIndent().lines()
                )
            ),
            listOf(
                Day.TestData.Test(
                    "Small", 618,
                    """
                        #######
                        #...#.#
                        #.....#
                        #..OO@#
                        #..O..#
                        #.....#
                        #######

                        <vv<<^^<<^^
                    """.trimIndent().lines()
                ),
                Day.TestData.Test(
                    "Larger", 9021,
                    """
                        ##########
                        #..O..O.O#
                        #......O.#
                        #.OO..O.O#
                        #..O@..O.#
                        #O#..O...#
                        #O..O..O.#
                        #.OO.O.OO#
                        #....O...#
                        ##########

                        <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
                        vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
                        ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
                        <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
                        ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
                        ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
                        >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
                        <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
                        ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
                        v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
                    """.trimIndent().lines()
                )
            )
        )
    }
}