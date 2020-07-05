package com.noahjutz.gymroutines.data.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "set_table")
data class Set(
    val exerciseHolderId: Int,
    val reps: Int? = null,
    val weight: Double? = null,
    val time: Int? = null,
    val distance: Double? = null,

    @PrimaryKey(autoGenerate = true)
    val setId: Int = 0
)
