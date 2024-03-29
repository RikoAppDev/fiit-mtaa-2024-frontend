package core.data.helpers

import kotlin.math.pow
import kotlin.math.round

fun round(x: Double, decimalPrecision: Int): Double {
    val scale = 10.0.pow(decimalPrecision)
    return round(x * scale) / scale
}