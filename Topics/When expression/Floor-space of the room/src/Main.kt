import kotlin.math.sqrt

fun main() {
    val type = readLine()!!
    var s = 0.0
    when (type) {
        "triangle" -> {
            val (a, b, c) = Array(3) { readLine()!!.toDouble() }
            val p = (a + b + c) / 2
            s = sqrt(p * (p - a) * (p - b) * (p - c))
        }
        "rectangle" -> {
            val (a, b) = Array(2) { readLine()!!.toDouble() }
            s = a * b
        }
        "circle" -> {
            val pi = 3.14
            val r = readLine()!!.toDouble()
            s = pi * r * r
        }
    }
    println(s)
}