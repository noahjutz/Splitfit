package com.noahjutz.gymroutines.models

data class ExerciseReference(
    val exercise: Exercise,
    val sets: ArrayList<Set> = ArrayList()
)