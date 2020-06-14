package com.noahjutz.gymroutines.data.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "set_table")
data class Set(
    val exerciseId: Int,

    @PrimaryKey(autoGenerate = true)
    val setId: Int = 0
)