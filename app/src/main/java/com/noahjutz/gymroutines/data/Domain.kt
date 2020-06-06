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

@Entity(primaryKeys = ["routineId", "exerciseWrapperId"])
data class RoutineExerciseCrossRef(
    val routineId: Int,
    val exerciseWrapperId: Int
)

/**
 * [Routine] with [ExerciseWrapper]s
 */
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

    @PrimaryKey(autoGenerate = true)
    val exerciseWrapperId: Int = 0
)

@Entity(tableName = "set_table")
data class Set(
    val exerciseWrapperId: Int,

    @PrimaryKey(autoGenerate = true)
    val setId: Int = 0
)

/**
 * [ExerciseWrapper] with [Set]s
 */
data class EwS(
    @Embedded val exerciseWrapper: ExerciseWrapper,
    @Relation(
        entity = Set::class,
        parentColumn = "exerciseWrapperId",
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
        parentColumn = "routineId",
        entityColumn = "exerciseWrapperId",
        associateBy = Junction(RoutineExerciseCrossRef::class)
    )
    val exerciseWrappers: List<ExerciseWrapper>
)