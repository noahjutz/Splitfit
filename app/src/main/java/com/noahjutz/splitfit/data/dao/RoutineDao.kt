/*
 * Splitfit
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

package com.noahjutz.splitfit.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noahjutz.splitfit.data.domain.Routine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface RoutineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(routine: Routine): Long

    @Delete
    suspend fun delete(routine: Routine)

    @Query("SELECT * FROM routine_table")
    fun getRoutines(): Flow<List<Routine>>

    @Query("SELECT * FROM routine_table WHERE routineId == :routineId")
    fun getRoutine(routineId: Int): Routine?

    @Query("SELECT * FROM routine_table WHERE routineId == :routineId")
    fun getRoutineLive(routineId: Int): LiveData<Routine?>?
}
