package com.noahjutz.gymroutines.data.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * TODO
 */
@Entity(tableName = "set_table")
data class Set(
    val exerciseHolderId: Int,

    @PrimaryKey(autoGenerate = true)
    val setId: Int = 0
)
