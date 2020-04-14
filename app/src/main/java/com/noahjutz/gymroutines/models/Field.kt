package com.noahjutz.gymroutines.models

data class Field(
    val name: String, // Repetitions, Duration, etc
    val unit: MeasureUnit
) {
    override fun toString(): String {
        return "[Field: $name, $unit]"
    }
}