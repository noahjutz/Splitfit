package com.noahjutz.gymroutines.data.domain

import androidx.room.*

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