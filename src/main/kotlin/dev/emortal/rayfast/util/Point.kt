package dev.emortal.rayfast.util

@JvmRecord
data class Point(val x: Double, val y: Double, val z: Double) {
    companion object {
        @JvmStatic
        fun generate(lambda: () -> Double) = Point(lambda(), lambda(), lambda())
    }
}