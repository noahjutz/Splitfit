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

package com.noahjutz.gymroutines.ui.routines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.ExerciseImpl
import com.noahjutz.gymroutines.data.domain.FullRoutine
import com.noahjutz.gymroutines.data.domain.Routine
import com.noahjutz.gymroutines.util.DiffUtilCallback
import com.noahjutz.gymroutines.util.setTextOrHide
import com.noahjutz.gymroutines.util.setTextOrUnnamed
import kotlinx.android.synthetic.main.listitem_routine.view.*

/**
 * [ListAdapter] for [FullRoutine]
 */
class RoutineAdapter(
    private val routineListener: RoutineListener
) : ListAdapter<Routine, RoutineAdapter.RoutineHolder>(diffUtil) {
    fun getRoutine(pos: Int): Routine = getItem(pos)

    inner class RoutineHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RoutineHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_routine, parent, false)
    )

    override fun onBindViewHolder(holder: RoutineHolder, position: Int) {
        val routine = getItem(position)

        holder.itemView.apply {
            name.setTextOrUnnamed(routine.name)

            setOnClickListener { routineListener.onRoutineClick(routine) }
        }
    }

    interface RoutineListener {
        fun onRoutineClick(routine: Routine)
    }
}

private val diffUtil =
    DiffUtilCallback<Routine>({ old, new -> old.routineId == new.routineId })
