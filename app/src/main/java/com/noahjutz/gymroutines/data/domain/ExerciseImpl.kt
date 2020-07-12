package com.noahjutz.gymroutines.data.domain

import androidx.room.Embedded
import androidx.room.Relation
import com.noahjutz.gymroutines.util.Equatable

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
    ) var sets: List<Set>
) : Equatable

