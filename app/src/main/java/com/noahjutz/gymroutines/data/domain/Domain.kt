package com.noahjutz.gymroutines.data.domain

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
        entity = ExerciseWrapper::class,
        parentColumn = "routineId",
        entityColumn = "exerciseWrapperId"
    )
    val ews: List<EwS>
)