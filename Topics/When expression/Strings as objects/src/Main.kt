fun main() {
    val input = readLine()!!
    // write code here
    println(
        when {
            input.isEmpty() -> ""
            input.first() == 'i' -> input.drop(1).toInt() + 1
            input.first() == 's' -> input.drop(1).reversed()
            else -> input
        }
    )
}
