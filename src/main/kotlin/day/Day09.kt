package day

object Day09 : Day {
    override fun part1(input: List<String>): Result {
        val list = input.first().toCharArray()
            .map { it.digitToInt() }
            .flatMapIndexed { index: Int, c: Int -> generateSequence { if (index % 2 == 0) index / 2 else -1 }.take(c) }
            .toMutableList()
        for (i in list.indices.reversed()) {
            if (i != -1) {
                val newIndex = list.indexOfFirst { it == -1 }
                if (newIndex > i) break

                list[newIndex] = list[i]
                list[i] = -1
            }
        }
        return list.asSequence().filter { it != -1 }.mapIndexed { index, id -> (index * id).toLong() }.sum().asSuccess()
    }

    override fun part2(input: List<String>): Result {
        val list = input.first().toCharArray()
            .map { it.digitToInt() }
            .mapIndexed { index: Int, c: Int -> Sector(if (index % 2 == 0) index / 2 else -1, c) }
            .toMutableList()

        for (i in (0..<input.first().length/2 + 1).reversed()) {
            val index = list.indexOfFirst { it.id == i }
            val sector = list[index]
            val insertionPosition = list.indexOfFirst { it.id == -1 && it.size >= sector.size }
            if (insertionPosition > -1 && insertionPosition < index) {
                list.removeAt(index)
                list.add(index, Sector(-1, sector.size))
                val openSector = list.removeAt(insertionPosition)
                list.add(insertionPosition, sector)
                if (openSector.size > sector.size) {
                    list.add(insertionPosition + 1, Sector(-1, openSector.size - sector.size))
                }
            }
        }

        return list.asSequence()
            .flatMap { (id, size) -> generateSequence { id }.take(size) }
            .mapIndexed { index, id -> index.toLong() * if (id == -1) 0 else id }
            .sum()
            .asSuccess()
    }

    data class Sector(val id: Int, val size: Int)

    override fun testData(): Day.TestData {
        return Day.TestData(
            1928,
            2858,
            """2333133121414131402"""
                .lines()
        )
    }
}