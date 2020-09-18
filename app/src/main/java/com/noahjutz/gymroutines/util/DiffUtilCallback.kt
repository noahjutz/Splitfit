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

package com.noahjutz.gymroutines.util

import androidx.recyclerview.widget.DiffUtil

/**
 * Implementation of [DiffUtil.ItemCallback] for data classes.
 */
class DiffUtilCallback<T : Equatable>(
    private val itemsEqual: (old: T, new: T) -> Boolean = { old, new -> old == new },
    private val contentsEqual: (old: T, new: T) -> Boolean = { old, new -> old == new }
) : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(old: T, new: T) = itemsEqual(old, new)
    override fun areContentsTheSame(old: T, new: T) = contentsEqual(old, new)
}

interface Equatable {
    override fun equals(other: Any?): Boolean
}
