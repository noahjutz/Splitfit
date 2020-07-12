package com.noahjutz.gymroutines.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noahjutz.gymroutines.data.domain.ExerciseHolder

@Suppress("UNUSED")
private const val TAG = "ExerciseHolderDao"

@Dao
interface ExerciseHolderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exerciseHolder: ExerciseHolder): Long

    @Delete
    suspend fun delete(exerciseHolder: ExerciseHolder)

    @Query("SELECT * FROM exercise_holder_table")
    fun getExerciseHolders(): LiveData<List<ExerciseHolder>>
}