package com.noahjutz.gymroutines.data.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * TODO
 */
@Entity(tableName = "exercise_table")
data class Exercise(
    var name: String,
    var description: String,
    var hidden: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    val exerciseId: Int = 0
)
