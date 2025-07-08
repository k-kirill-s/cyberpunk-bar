package by.cyberpunkfandom.barfrontend.core

fun Float.format(digits: Int): String = toDouble().format(digits)

fun Double.format(digits: Int): String = jsRound(this, digits).toString()

private fun jsRound(number: Double, digits: Int): JsNumber = js("number.toFixed(digits)")
