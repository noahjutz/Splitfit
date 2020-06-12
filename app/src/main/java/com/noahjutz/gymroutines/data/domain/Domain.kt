package com.noahjutz.gymroutines.data.domain

import androidx.room.*

@Entity(tableName = "routine_and_exercise", primaryKeys = ["routineId", "exerciseId"])
data class RoutineAndExercise(
    val routineId: Int,
    val exerciseId: Int
)

/**
 * [ExerciseWrapper] with [Set]s
 */
data class ExerciseImpl(
    @Embedded val exercise: Exercise,
    @Relation(
        entity = Set::class,
        parentColumn = "exerciseId",
        entityColumn = "exerciseWrapperId" // TODO: Rename in [Set]
    )
    val sets: List<Set>
)

/**
 * [Routine] with [ExerciseImpl]s
 */
data class FullRoutine(
    @Embedded val routine: Routine,
    @Relation(
        entity = Exercise::class,
        parentColumn = "routineId",
        entityColumn = "exerciseId",
        associateBy = Junction(RoutineAndExercise::class)
    )
    val exercises: List<ExerciseImpl>
)