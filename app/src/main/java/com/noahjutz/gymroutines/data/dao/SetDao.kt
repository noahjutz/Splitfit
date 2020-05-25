package com.noahjutz.gymroutines.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noahjutz.gymroutines.data.Set

@Suppress("unused")
private const val TAG = "SetDao"

@Dao
interface SetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(set: Set): Long

    @Delete
    suspend fun delete(set: Set)

    @Query("SELECT * FROM set_table WHERE setId == :id")
    suspend fun getSetById(id: Int): Set?

    @Query("SELECT * FROM set_table")
    fun getSets(): LiveData<List<Set>>
}