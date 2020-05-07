package com.noahjutz.gymroutines.data

import androidx.room.*

@Entity(tableName = "routine_table")
data class Routine(
    var name: String,
    var description: String = "",

    @PrimaryKey(autoGenerate = true)
    val routineId: Int = 0
)

@Entity(tableName = "exercise_table")
data class Exercise(
    var name: String,
    var description: String = "",
    var hidden: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    val exerciseId: Int = 0
)

@Entity(primaryKeys = ["routineId", "exerciseId"])
data class RoutineExerciseCrossRef(
    val routineId: Int,
    val exerciseId: Int
)

data class RoutineWithExercises(
    @Embedded val routine: Routine,
    @Relation(
        parentColumn = "routineId",
        entityColumn = "exerciseId",
        associateBy = Junction(RoutineExerciseCrossRef::class)
    )
    val exercises: List<Exercise>
)

@Entity(tableName = "exercise_wrapper_table")
data class ExerciseWrapper(
    val exerciseId: Int
)