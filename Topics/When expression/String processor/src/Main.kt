fun main() {
    val str1 = readLine()!!
    val op = readLine()!!
    val str2 = readLine()!!

    println(
        when (op) {
            "equals" -> str1.equals(str2)
            "plus" -> str1 + str2
            "endsWith" -> str1.endsWith(str2)
            else -> "Unknown operation"
        }
    )
}
