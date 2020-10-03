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

package com.noahjutz.gymroutines.ui.exercises

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.Exercise
import com.noahjutz.gymroutines.util.setTextOrUnnamed

class ExercisesAdapter(private val exerciseListener: ExerciseListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = emptyList<Exercise>()
        set(i) {
            field = i
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        object : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.listitem_exercise, parent, false)
        ) {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val exercise = items[position]

        holder.itemView.apply {
            findViewById<TextView>(R.id.name).setTextOrUnnamed(exercise.name)
            setOnClickListener { exerciseListener.onExerciseClick(exercise) }
        }
    }

    interface ExerciseListener {
        fun onExerciseClick(exercise: Exercise)
    }

    override fun getItemCount(): Int = items.size
}

