package com.noahjutz.gymroutines.data.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * TODO
 */
@Entity(tableName = "set_table")
data class Set(
    val exerciseHolderId: Int,
    val reps: Int = -1,
    val weight: Double = -1.0,
    val time: Int = -1,
    val distance: Double = -1.0,

    @PrimaryKey(autoGenerate = true)
    val setId: Int = 0
)
