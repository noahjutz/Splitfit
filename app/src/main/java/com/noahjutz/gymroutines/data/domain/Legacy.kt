package com.noahjutz.gymroutines.data.domain

import androidx.room.*

// TODO: Remove all of these

@Entity(primaryKeys = ["routineId", "exerciseWrapperId"])
data class RoutineExerciseCrossRef(
    val routineId: Int,
    val exerciseWrapperId: Int
)

data class RwE(
    @Embedded val routine: Routine,
    @Relation(
        parentColumn = "routineId",
        entityColumn = "exerciseWrapperId",
        associateBy = Junction(RoutineExerciseCrossRef::class)
    )
    val exerciseWrappers: List<ExerciseWrapper>
)

@Entity(tableName = "exercise_wrapper_table")
data class ExerciseWrapper(
    val exerciseId: Int,
    val routineId: Int,

    @PrimaryKey(autoGenerate = true)
    val exerciseWrapperId: Int = 0
)