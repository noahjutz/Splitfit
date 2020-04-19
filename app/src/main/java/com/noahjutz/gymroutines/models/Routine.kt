package com.noahjutz.gymroutines.models

data class Routine(
    var title: String,
    var note: String = "",
    val exerciseRefs: ArrayList<ExerciseReference> = ArrayList()
)