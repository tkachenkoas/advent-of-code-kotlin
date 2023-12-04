package day03

import readInput

fun main() {
    fun engineNumbers(
        input: List<String>
    ): List<EngineDetail> {
        val width = input[0].length
        val height = input.size

        val details = mutableListOf<EngineDetail>()

        var currentNumber: String? = null
        var currentX: Int? = null

        for (y in 0 until height) {
            for (x in 0 until width) {
                val cell = input[y][x]
                if (!cell.isDigit()) {
                    if (currentNumber == null) {
                        continue
                    }
                    val detail = EngineDetail.parse(
                        currentNumber.toInt(),
                        currentX!!,
                        y,
                        input
                    )

                    detail?.run {
                        details.add(this)
                    }

                    currentNumber = null
                    currentX = null
                    continue
                }

                if (currentNumber == null) {
                    currentNumber = ""
                    currentX = x
                }
                currentNumber += cell

                if (x == width - 1) {
                    val detail = EngineDetail.parse(
                        currentNumber.toInt(),
                        currentX!!,
                        y,
                        input
                    )

                    detail?.run {
                        details.add(this)
                    }
                    currentNumber = null
                    currentX = null
                }
            }
        }
        return details
    }

    /**
     * The engine schematic (your puzzle input) consists of a visual representation of the engine. There are lots of numbers and symbols you don't really understand, but apparently any number adjacent to a symbol, even diagonally, is a "part number" and should be included in your sum. (Periods (.) do not count as a symbol.)
     *
     * Here is an example engine schematic:
     *
     * 467..114..
     * ...*......
     * ..35..633.
     * ......#...
     * 617*......
     * .....+.58.
     * ..592.....
     * ......755.
     * ...$.*....
     * .664.598..
     * In this schematic, two numbers are not part numbers because they are not adjacent to a symbol: 114 (top right) and 58 (middle right). Every other number is adjacent to a symbol and so is a part number; their sum is 4361.
     *
     * Of course, the actual engine schematic is much larger. What is the sum of all of the part numbers in the engine schematic?
     */
    fun part1(input: List<String>): Int {
        val engineDetails = engineNumbers(input)
        return engineDetails.sumOf { it.value }
    }

    /**
     * The missing part wasn't the only issue - one of the gears in the engine is wrong. A gear is any * symbol that is adjacent to exactly two part numbers. Its gear ratio is the result of multiplying those two numbers together.
     *
     * This time, you need to find the gear ratio of every gear and add them all up so that the engineer can figure out which gear needs to be replaced.
     *
     * Consider the same engine schematic again:
     *
     * 467..114..
     * ...*......
     * ..35..633.
     * ......#...
     * 617*......
     * .....+.58.
     * ..592.....
     * ......755.
     * ...$.*....
     * .664.598..
     * In this schematic, there are two gears. The first is in the top left; it has part numbers 467 and 35, so its gear ratio is 16345. The second gear is in the lower right; its gear ratio is 451490. (The * adjacent to 617 is not a gear because it is only adjacent to one part number.) Adding up all of the gear ratios produces 467835.
     */
    fun part2(input: List<String>): Int {
        val engineDetails = engineNumbers(input)

        val coordToGearDetails = mutableMapOf<String, MutableList<EngineDetail>>()

        engineDetails.forEach { detail ->
            if (detail.gears.isEmpty()) {
                return@forEach
            }

            detail.gears.forEach {
                val coord = "${it.x},${it.y}"
                coordToGearDetails.getOrPut(coord) { mutableListOf() }.add(detail)
            }
        }
        val gearRatios = mutableListOf<Int>()

        coordToGearDetails.forEach { (coord, details) ->
            if (details.size != 2) {
                return@forEach
            }
            val gearRatio = details[0].value * details[1].value
            gearRatios.add(gearRatio)
        }

        return gearRatios.sum()
    }

    // test if implementation meets criteria from the description, like:
    /*val testInput = readInput("day03/Day03_test")
    println("Day03")
    val part1Test = part1(testInput)
    println("Part 1 test: $part1Test")
    check(part1Test == 4361)

    val dayInput = readInput("day03/Day03")
    val part1Task = part1(dayInput)
    println("Part 1 task: $part1Task")
    check(part1Task == 536202)*/

    val testInputPart2 = readInput("day03/Day03_part2_test")
    val part2Test = part2(testInputPart2)
    println("Part 2 test: $part2Test")
    check(part2Test == 467835)

    val dayInputPart2 = readInput("day03/Day03")
    val part2Task = part2(dayInputPart2)
    println("Part 2 task: $part2Task")
    check(part2Task == 78272573)
}


data class EngineDetail(
    val value: Int,
    val gears: List<Gear>
) {
    companion object {
        fun parse(value: Int, x: Int, y: Int, input: List<String>): EngineDetail? {
            val width = input[0].length
            val height = input.size

            val length = value.toString().length

            var isEnginePart = false
            val gears = mutableListOf<Gear>()

            for (row in x - 1..x + length) {
                for (col in y - 1..y + 1) {
                    if (row < 0 || row >= width || col < 0 || col >= height) {
                        continue
                    }
                    val current: Char = input[col][row]
                    if (current != '.' && !current.isDigit()) {
                        println("Number $value (at $x, $y) has adjacent symbol $current at $row, $col")
                        isEnginePart = true
                        if (current == '*') {
                            println("Detail $value has a gear at $row, $col")
                            gears.add(Gear(row, col))
                        }
                    }
                }
            }
            if (!isEnginePart) {
                println("Number $value at $x, $y does not have an adjacent symbol")
                return null
            }

            return EngineDetail(value, gears)
        }
    }
}

data class Gear(
    val x: Int,
    val y: Int,
)
