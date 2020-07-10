package com.noahjutz.gymroutines.data.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.noahjutz.gymroutines.util.Equatable

@Entity(tableName = "routine_table")
data class Routine(
    var name: String = "",
    var description: String = "",

    @PrimaryKey(autoGenerate = true)
    val routineId: Int = 0
) : Equatable
