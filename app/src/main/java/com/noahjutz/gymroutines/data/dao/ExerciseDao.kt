package com.noahjutz.gymroutines.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noahjutz.gymroutines.data.Exercise

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: Exercise)

    @Delete
    suspend fun delete(exercise: Exercise)

    @Query("DELETE FROM exercise_table")
    suspend fun clearExercises()

    @Query("SELECT * FROM exercise_table ORDER BY name DESC")
    fun getExercises(): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercise_table WHERE exerciseId = :id")
    suspend fun getExerciseById(id: Int): Exercise
}