package com.noahjutz.gymroutines.data.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * TODO
 */
@Entity(tableName = "routine_table")
data class Routine(
    var name: String,
    var description: String = "",

    @PrimaryKey(autoGenerate = true)
    val routineId: Int = 0
)
