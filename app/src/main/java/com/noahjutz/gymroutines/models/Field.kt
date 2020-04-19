package com.noahjutz.gymroutines.models

data class Field<T>(
    val name: String, // Repetitions, Duration, etc
    val value: T
)