package com.noahjutz.gymroutines.util

import com.noahjutz.gymroutines.data.domain.*
import com.noahjutz.gymroutines.data.domain.Set

/**
 * Builders for classes in [com.noahjutz.gymroutines.data.domain]
 */
data class FullRoutineBuilder(
    val routine: Routine = Routine(),
    val exercises: List<ExerciseImpl> = listOf()
) {
    fun build() = FullRoutine(routine, exercises)
}

data class ExerciseImplBuilder(
    val routine: Routine = Routine(),
    val exercise: Exercise = Exercise(),
    val exerciseHolder: ExerciseHolder = ExerciseHolder(exercise.exerciseId, routine.routineId),
    val sets: List<Set> = listOf()
) {
    fun build() = ExerciseImpl(exerciseHolder, exercise, sets)
}
