package com.noahjutz.gymroutines.data.domain

import androidx.room.*

@Entity(tableName = "routine_and_exercise", primaryKeys = ["routineId", "exerciseId"])
data class RoutineAndExercise(
    val routineId: Int,
    val exerciseId: Int
)

/**
 * [ExerciseHolder] with [Exercise] and [Set]s
 */
data class ExerciseImpl(
    @Embedded val exerciseHolder: ExerciseHolder,
    @Relation(
        entity = Exercise::class,
        parentColumn = "exerciseId",
        entityColumn = "exerciseId"
    ) val exercise: Exercise,
    @Relation(
        entity = Set::class,
        parentColumn = "exerciseHolderId",
        entityColumn = "exerciseHolderId"
    ) val sets: List<Set>
)

/**
 * [Routine] with [ExerciseImpl]s
 */
data class FullRoutine(
    @Embedded val routine: Routine,
    @Relation(
        entity = ExerciseHolder::class,
        parentColumn = "routineId",
        entityColumn = "routineId"
    ) val exercises: List<ExerciseImpl>
)