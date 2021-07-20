fun main() {
    val (var1, op, var2) = readLine()!!.split(" ")

    val a = var1.toLong()
    val b = var2.toLong()

    when (op) {
        "+" -> println(a + b)
        "-" -> println(a - b)
        "*" -> println(a * b)
        "/" -> {
            if (b == 0.toLong()) {
                println("Division by 0!")
            } else {
                println(a / b)
            }
        }
        else -> println("Unknown operator")
    }
}
