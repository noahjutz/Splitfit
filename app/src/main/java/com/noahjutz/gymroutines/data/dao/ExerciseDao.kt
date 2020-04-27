package com.noahjutz.gymroutines.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noahjutz.gymroutines.data.Exercise

@Dao
interface ExerciseDao {
    @Insert
    fun insert(exercise: Exercise)

    @Delete
    fun delete(exercise: Exercise)

    @Update
    fun update(exercise: Exercise)

    @Query("DELETE FROM exercise_table")
    fun clearExercises()

    @Query("SELECT * FROM exercise_table ORDER BY name DESC")
    fun getExercises(): LiveData<List<Exercise>>
}