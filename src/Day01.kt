fun main() {
    /**
     * The newly-improved calibration document consists of lines of text; each line originally contained a specific calibration value that the Elves now need to recover. On each line, the calibration value can be found by combining the first digit and the last digit (in that order) to form a single two-digit number.
     *
     * For example:
     *
     * 1abc2
     * pqr3stu8vwx
     * a1b2c3d4e5f
     * treb7uchet
     * In this example, the calibration values of these four lines are 12, 38, 15, and 77. Adding these together produces 142.
     *
     * Consider your entire calibration document. What is the sum of all of the calibration values?
     */
    fun part1(input: List<String>): Int {
        val firstDigit = { str: String -> str.first { it.isDigit() }.digitToInt() }
        val lastDigit = { str: String -> str.last { it.isDigit() }.digitToInt() }

        return input.sumOf { 10 * firstDigit(it) + lastDigit(it) }
    }

    /**
     * Your calculation isn't quite right. It looks like some of the digits are actually spelled out with letters: one, two, three, four, five, six, seven, eight, and nine also count as valid "digits".
     *
     * Equipped with this new information, you now need to find the real first and last digit on each line. For example:
     *
     * two1nine
     * eightwothree
     * abcone2threexyz
     * xtwone3four
     * 4nineeightseven2
     * zoneight234
     * 7pqrstsixteen
     * In this example, the calibration values are 29, 83, 13, 24, 42, 14, and 76. Adding these together produces 281.
     */
    fun part2(input: List<String>): Int {

        val numbersAsWords = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

        val firstDigit = firstDigit@{ str: String ->
            val charArray = str.toCharArray()
            for ((idx, c) in charArray.withIndex()) {
                if (c.isDigit()) {
                    return@firstDigit c.digitToInt()
                }
                for ((numIdx, word) in numbersAsWords.withIndex()) {
                    if (idx + word.length > str.length) {
                        continue
                    }
                    if (str.substring(idx, idx + word.length) == word) {
                        return@firstDigit numIdx + 1
                    }
                }
            }
            throw IllegalArgumentException("No digit found in $str")
        }

        val lastDigit = lastDigit@{ str: String ->
            val charArray = str.toCharArray()
            // reversed iteration from end to beginning
            for (idx in charArray.size - 1 downTo 0) {
                val ch = charArray[idx]
                if (ch.isDigit()) {
                    return@lastDigit ch.digitToInt()
                }
                for ((numIdx, word) in numbersAsWords.withIndex()) {
                    // check out of bounds
                    if (idx + word.length > str.length) {
                        continue
                    }
                    if (str.substring(idx, idx + word.length) == word) {
                        return@lastDigit numIdx + 1
                    }
                }
            }
            throw IllegalArgumentException("No digit found in $str")
        }

        return input.sumOf {
            val value = 10 * firstDigit(it) + lastDigit(it)
            println("$it -> $value")
            value
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val testInputPart2 = readInput("Day01_part2_test")
    check(part2(testInputPart2) == 281)

    println("Day01")
    println("Part 1: ${part1(testInput)}")

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
