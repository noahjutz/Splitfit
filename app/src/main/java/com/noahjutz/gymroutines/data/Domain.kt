package com.noahjutz.gymroutines.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Domain Models
 */

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