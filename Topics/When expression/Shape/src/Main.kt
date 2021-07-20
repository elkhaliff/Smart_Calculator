fun main(args: Array<String>) {
    val p = "You have chosen"
    println(when (readLine()!!.toInt()) {
                1 -> "$p a square"
                2 -> "$p a circle"
                3 -> "$p a triangle"
                4 -> "$p a rhombus"
                else -> "There is no such shape!"
        }
    )
}