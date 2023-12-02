package day02

import readInput

fun main() {
    /**
    As you walk, the Elf shows you a small bag and some cubes which are either red, green, or blue. Each time you play this game, he will hide a secret number of cubes of each color in the bag, and your goal is to figure out information about the number of cubes.

    To get information, once a bag has been loaded with cubes, the Elf will reach into the bag, grab a handful of random cubes, show them to you, and then put them back in the bag. He'll do this a few times per game.

    You play several games and record the information from each game (your puzzle input). Each game is listed with its ID number (like the 11 in Game 11: ...) followed by a semicolon-separated list of subsets of cubes that were revealed from the bag (like 3 red, 5 green, 4 blue).

    For example, the record of a few games might look like this:

    Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
    Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
    Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
    Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
    Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    In game 1, three sets of cubes are revealed from the bag (and then put back again). The first set is 3 blue cubes and 4 red cubes; the second set is 1 red cube, 2 green cubes, and 6 blue cubes; the third set is only 2 green cubes.

    The Elf would first like to know which games would have been possible if the bag contained only 12 red cubes, 13 green cubes, and 14 blue cubes?

    In the example above, games 1, 2, and 5 would have been possible if the bag had been loaded with that configuration. However, game 3 would have been impossible because at one point the Elf showed you 20 red cubes at once; similarly, game 4 would also have been impossible because the Elf showed you 15 blue cubes at once. If you add up the IDs of the games that would have been possible, you get 8.

    Determine which games would have been possible if the bag had been loaded with only 12 red cubes, 13 green cubes, and 14 blue cubes. What is the sum of the IDs of those games?
     */
    fun part1(input: List<String>): Int {

        val games = input.map {
            Game.parseGame(it)
        }
        val red = 12
        val green = 13
        val blue = 14
        val possibleGames = games.filter {
            it.isPossible(red, green, blue)
        }
        return possibleGames.sumOf { it.id }
    }

    /**
     * As you continue your walk, the Elf poses a second question: in each game you played, what is the fewest number of cubes of each color that could have been in the bag to make the game possible?
     *
     * Again consider the example games from earlier:
     *
     * Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
     * Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
     * Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
     * Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
     * Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
     * In game 1, the game could have been played with as few as 4 red, 2 green, and 6 blue cubes. If any color had even one fewer cube, the game would have been impossible.
     * Game 2 could have been played with a minimum of 1 red, 3 green, and 4 blue cubes.
     * Game 3 must have been played with at least 20 red, 13 green, and 6 blue cubes.
     * Game 4 required at least 14 red, 3 green, and 15 blue cubes.
     * Game 5 needed no fewer than 6 red, 3 green, and 2 blue cubes in the bag.
     * The power of a set of cubes is equal to the numbers of red, green, and blue cubes multiplied together. The power of the minimum set of cubes in game 1 is 48. In games 2-5 it was 12, 1560, 630, and 36, respectively. Adding up these five powers produces the sum 2286.
     *
     * For each game, find the minimum set of cubes that must have been present. What is the sum of the power of these sets?
     */
    fun part2(input: List<String>): Int {
        val games = input.map {
            Game.parseGame(it)
        }

        fun minRequiredByColor(sets: List<Set>, color: Color): Int {
            return sets.flatMap { set -> set.cubes }.filter { it.color == color }.maxBy { it.count }.count
        }

        return games.sumOf { game ->
            val sets = game.sets
            val red = minRequiredByColor(sets, Color.RED)

            val blue = minRequiredByColor(sets, Color.BLUE)

            val green = minRequiredByColor(sets, Color.GREEN)

            green * red * blue
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day02/Day02_test")
    println("Day02")
    println("Part 1 test: ${part1(testInput)}")
    check(part1(testInput) == 8)

    val dayInput = readInput("day02/Day02")
    println("Part 1 task: ${part1(dayInput)}")

    val testInputPart2 = readInput("day02/Day02_part2_test")
    println("Part 2 test: ${part2(testInputPart2)}")
    check(part2(testInputPart2) == 2286)

    println("Part 2 task: ${part2(dayInput)}")
}

data class Game(val id: Int, val sets: List<Set>) {

    companion object {
        // sample string : "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"
        fun parseGame(input: String): Game {
            val (idString, setsString) = input.trim().split(":")
            val id = idString.split(" ")[1].trim().toInt()
            val sets = setsString.split(";")
                .map { Set.parseSet(it) }
            return Game(id, sets)
        }
    }

    fun isPossible(red: Int, green: Int, blue: Int): Boolean {
        return sets.all { round ->
            round.cubes.all { cube ->
                when (cube.color) {
                    Color.RED -> red >= cube.count
                    Color.GREEN -> green >= cube.count
                    Color.BLUE -> blue >= cube.count
                }
            }
        }
    }
}

data class Set(val cubes: List<Cube>) {
    companion object {
        // sample string : "3 blue, 4 red"
        fun parseSet(input: String): Set {
            val cubes = input.trim().split(",")
                .map { Cube.parseCube(it) }
            return Set(cubes)
        }
    }
}

// class-enum color:  red, green, blue
enum class Color {
    RED, GREEN, BLUE
}

data class Cube(val color: Color, val count: Int) {
    // 1 green
    companion object {
        fun parseCube(input: String): Cube {
            val (count, colorString) = input.trim().split(" ")
            return Cube(Color.valueOf(colorString.uppercase()), count.toInt())
        }
    }
}
