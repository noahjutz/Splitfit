/*
 * GymRoutines
 * Copyright (C) 2020  Noah Jutz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.noahjutz.gymroutines.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noahjutz.gymroutines.data.domain.ExerciseHolder

@Dao
interface ExerciseHolderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exerciseHolder: ExerciseHolder): Long

    @Delete
    suspend fun delete(exerciseHolder: ExerciseHolder)

    @Query("SELECT * FROM exercise_holder_table")
    fun getExerciseHolders(): LiveData<List<ExerciseHolder>>
}
