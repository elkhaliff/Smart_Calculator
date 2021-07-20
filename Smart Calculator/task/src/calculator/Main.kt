package calculator

import java.util.*
import kotlin.math.pow

class Processor {
    val patternOperator = """([-+*/^()])""".toRegex()
    private val patternBase = """(-?\w+)\s?""".toRegex()
    private val patternValue = """([-+*/]+)\s+(\w+)""".toRegex()
    private val patternVariables = """([a-zA-Z]+)\s*([=])\s*(-?\d+)\s*""".toRegex()
    private val patternVariablesFor = """([a-zA-Z]+)\s*([=])\s*([a-zA-Z]+)\s*""".toRegex()

    private val unknownVariable = "Unknown variable"
    val invalidIdentifier = "Invalid identifier"
    private val invalidAssignment = "Invalid assignment"
    val invalidExpression = "Invalid expression"

    private val variables: MutableMap<String, Int> = mutableMapOf()

    private fun isBase(input: String) = patternBase.containsMatchIn(input)
    private fun isValue(input: String) = patternValue.containsMatchIn(input)
    private fun isVariables(input: String) = patternVariables.matches(input)
    private fun isVariablesFor(input: String) = patternVariablesFor.matches(input)
    fun testString(input: String): Boolean {
        var open = 0
        var close = 0
        for (el in input) {
            when (el) {
                '(' -> open++
                ')' -> close++
            }
        }
        if (open != close) return false

        return if (!input.contains('='))
            isBase(input)
        else
            (isValue(input) || isVariables(input) || isVariablesFor(input))
    }

    fun getValue(element: String) =
        try {
            element.toInt()
        } catch (e: Exception) {
            variables[element]!!
        }

    fun setVariables(input: String) {
        when {
            isVariables(input) -> {
                val matches = patternVariables.findAll(input)
                matches.forEach { matchResult ->
                    val variable = matchResult.groupValues[1]
                    val value = matchResult.groupValues[3]
                    variables[variable] = value.toInt()
                }
            }
            isVariablesFor(input) -> {
                val matches = patternVariablesFor.findAll(input)
                matches.forEach { matchResult ->
                    val variable = matchResult.groupValues[1]
                    val value = matchResult.groupValues[3]
                    if (variables[value] != null)
                        variables[variable] = variables[value]!!
                    else
                        println(unknownVariable)
                }
            }
            else -> println(invalidAssignment)
        }
    }

    private fun elementLevel(str: String) = when (str) {
        "+", "-" -> 1
        "*", "/" -> 2
        "^" -> 3
        "(", ")" -> 0
        else -> -1
    }

    fun infixToPostfix(input: String): MutableMap<Int, String> {
        val inputMap = mutableMapOf<Int, String>()
        var m = 0
        for (e in input) {
            val elm = e.toString()
            if (!patternOperator.matches(elm)) {
                if (inputMap[m] == null) inputMap[m] = ""
                inputMap[m] = inputMap[m] + elm
            } else {
                inputMap[++m] = elm
                m++
            }
        }
        val stack = Stack<String>()
        val postfixMap = mutableMapOf<Int, String>()
        var i = 0
        inputMap.forEach { (_, u) ->
            when {
                elementLevel(u) < 0 -> postfixMap[i++] = u
                u == "(" -> stack.push(u)
                u == ")" -> {
                    while (!stack.isEmpty() && stack.peek() != "(")
                        postfixMap[i++] = stack.pop()
                    stack.pop()
                }
                else -> {
                    while (!stack.isEmpty() && elementLevel(u) <= elementLevel(stack.peek()))
                        postfixMap[i++] = stack.pop()
                    stack.push(u)
                }
            }
        }
        while (!stack.isEmpty()) {
            if (stack.peek() == "(") {
                println(invalidExpression)
                return mutableMapOf()
            }
            postfixMap[i++] = stack.pop()
        }
        return postfixMap
    }

    fun evaluatePostfix(exp: MutableMap<Int, String>): Int {
        val stack = Stack<Int>()

        for ((_, u) in exp) {
            if (elementLevel(u) < 0) {
                stack.push(getValue(u))
            } else {
                val val1 = stack.pop()
                val val2 = stack.pop()
                when (u) {
                    "+" -> stack.push(val2 + val1)
                    "-" -> stack.push(val2 - val1)
                    "/" -> stack.push(val2 / val1)
                    "*" -> stack.push(val2 * val1)
                    "^" -> stack.push(val2.toDouble().pow(val1).toInt())
                }
            }
        }
        return stack.pop()
    }

    fun help() =
        println("The program calculates the sum of numbers.\n" +
                "You can also use any sign of the arithmetic operation.\n" +
                "You can use variables and parentheses..")

    fun exit() =
        println("Bye!")
}

fun main() {
    val proc = Processor()
    while (true) {
        var input = readLine()!!
        if (input.isNotBlank()) {
            if (input.startsWith("/")) {
                when (input.substring(1)) {
                    "exit" -> { proc.exit(); break; }
                    "help" -> proc.help()
                    else -> println("Unknown command")
                }
            } else {
                input = input.replace("\\s+".toRegex(), "").replace("\\+\\++|(--)+".toRegex(), "+").replace("+-", "-")
                if (input.contains("**") || input.contains("//") )
                    println(proc.invalidExpression)
                else if (proc.testString(input)) {
                    if ('=' in input)
                        proc.setVariables(input)
                    else if (!proc.patternOperator.containsMatchIn(input))
                        try {
                            println(proc.getValue(input))
                        } catch (e: Exception) {
                            println(proc.invalidExpression)
                        }
                    else {
                        val postfix = proc.infixToPostfix(input)
                        if (postfix.isNotEmpty())
                            println(proc.evaluatePostfix(postfix))
                    }
                } else
                    println(proc.invalidIdentifier)
            }
        }
    }
}