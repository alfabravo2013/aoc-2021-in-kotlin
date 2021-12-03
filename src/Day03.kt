fun main() {

    fun part1(input: List<String>): Int {
        val frequencies = HashMap<Int, Int>()
        val array = input.map { it.toCharArray() }

        for (i in 0 until array[0].size) {
            val bitFrequency = array.map { it[i] }.groupingBy { it }.eachCount()
            frequencies[i] = bitFrequency['1'] ?: 0
        }

        val gammaRate = CharArray(input[0].length) { if (frequencies[it]!! > input.size / 2) '1' else '0' }
            .joinToString("")
            .toInt(2)

        val epsilonRate = gammaRate.inv().and(if (input[0].length == 5) 0b00011111 else 0b0000111111111111)
        return  gammaRate * epsilonRate
    }

    fun applyBitCriteriaOx(list: List<String>, bit: Int): List<String> {
        val bitFrequency = list.map { it[bit] }.groupingBy { it }.eachCount()
        val prevalentBit = if ((bitFrequency['0'] ?: 0) > (bitFrequency['1'] ?: 0)) '0' else '1'
        return list.filter { it[bit] == prevalentBit }
    }

    fun applyBitCriteriaCo(list: List<String>, bit: Int): List<String> {
        val bitFrequency = list.map { it[bit] }.groupingBy { it }.eachCount()
        val prevalentBit = if ((bitFrequency['0'] ?: 0) <= (bitFrequency['1'] ?: 0)) '0' else '1'
        return list.filter { it[bit] == prevalentBit }
    }

    fun part2(input: List<String>): Int {
        var listOx = input.toList()
        var listCo = input.toList()

        for (i in 0 until input[0].length) {
            listOx = applyBitCriteriaOx(listOx, i)
            if (listOx.size == 1) {
                break
            }
        }

        for (i in 0 until input[0].length) {
            listCo = applyBitCriteriaCo(listCo, i)
            if (listCo.size == 1) {
                break
            }
        }

        val oxRate = listOx.first().toInt(2)
        val coRate = listCo.first().toInt(2)
        return  oxRate * coRate
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
