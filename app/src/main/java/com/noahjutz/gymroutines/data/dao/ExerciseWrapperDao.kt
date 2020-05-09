package com.noahjutz.gymroutines.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noahjutz.gymroutines.data.ExerciseWrapper

@Suppress("unused")
private const val TAG = "ExerciseWrapperDao"

@Dao
abstract class ExerciseWrapperDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(exerciseWrapper: ExerciseWrapper): Long

    @Delete
    abstract suspend fun delete(exerciseWrapper: ExerciseWrapper)

    @Query("SELECT * FROM exercise_wrapper_table")
    abstract fun getExerciseWrappers(): LiveData<List<ExerciseWrapper>>
}