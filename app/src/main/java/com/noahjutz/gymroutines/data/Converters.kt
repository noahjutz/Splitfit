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

@file:Suppress("IllegalIdentifier")

package com.noahjutz.gymroutines.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noahjutz.gymroutines.data.domain.Set
import com.noahjutz.gymroutines.data.domain.SetGroup

class Converters {
    private val gson = Gson()
    private val setGroupsType = object : TypeToken<MutableList<SetGroup>>() {}.type

    @TypeConverter
    fun fromSetGroups(setGroups: List<SetGroup>): String = gson.toJson(setGroups)

    @TypeConverter
    fun toSetGroups(json: String): MutableList<SetGroup> = gson.fromJson(json, setGroupsType)
}
