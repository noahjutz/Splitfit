package com.noahjutz.gymroutines.data.domain

import androidx.room.*

/**
 * [ExerciseHolder] n..1 [Routine]
 * [ExerciseHolder] n..1 [Exercise]
 * [ExerciseHolder] 1..n [Set]
 */
@Entity(tableName = "exercise_holder_table")
data class ExerciseHolder(
    val exerciseId: Int,
    val routineId: Int,
    @PrimaryKey(autoGenerate = true) val exerciseHolderId: Int
)