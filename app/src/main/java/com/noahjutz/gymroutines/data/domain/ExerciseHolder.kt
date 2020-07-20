package com.noahjutz.gymroutines.data.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.noahjutz.gymroutines.util.Equatable

/**
 * Holds an [Exercise] and [Set]s. Used in Routines.
 */
@Entity(tableName = "exercise_holder_table")
data class ExerciseHolder(
    val exerciseId: Int,
    val routineId: Int,
    var position: Int = -1,
    @PrimaryKey(autoGenerate = true) val exerciseHolderId: Int = 0
) : Equatable
