package com.noahjutz.gymroutines.data.dao

import androidx.room.*
import com.noahjutz.gymroutines.data.domain.Set

@Dao
interface SetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(set: Set): Long

    @Delete
    suspend fun delete(set: Set)

    @Query("SELECT * FROM set_table WHERE setId == :id")
    suspend fun getSetById(id: Int): Set
}