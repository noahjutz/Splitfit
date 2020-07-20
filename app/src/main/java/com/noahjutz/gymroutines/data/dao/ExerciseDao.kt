package com.noahjutz.gymroutines.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noahjutz.gymroutines.data.domain.Exercise

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: Exercise): Long

    @Delete
    suspend fun delete(exercise: Exercise)

    @Query("SELECT * FROM exercise_table")
    fun getExercises(): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercise_table WHERE exerciseId == :id")
    fun getExercise(id: Int): Exercise?
}