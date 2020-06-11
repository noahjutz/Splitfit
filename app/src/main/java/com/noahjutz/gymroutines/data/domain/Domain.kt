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
data class EwS(
    @Embedded val exercise: Exercise,
    @Relation(
        entity = Set::class,
        parentColumn = "exerciseId",
        entityColumn = "exerciseWrapperId"
    )
    val sets: List<Set>
)

/**
 * [Routine] with [EwS]s
 */
data class RwEwS(
    @Embedded val routine: Routine,
    @Relation(
        entity = Exercise::class,
        parentColumn = "routineId",
        entityColumn = "exerciseId",
        associateBy = Junction(RoutineAndExercise::class)
    )
    val ews: List<EwS>
)