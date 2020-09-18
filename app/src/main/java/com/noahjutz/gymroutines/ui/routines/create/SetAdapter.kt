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

package com.noahjutz.gymroutines.ui.routines.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.Set
import com.noahjutz.gymroutines.util.DiffUtilCallback
import kotlinx.android.synthetic.main.listitem_set.view.*

class SetAdapter : ListAdapter<Set, SetAdapter.SetHolder>(diffUtil) {
    fun getSet(position: Int): Set = getItem(position)

    inner class SetHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SetHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_set, parent, false)
    )

    override fun onBindViewHolder(holder: SetAdapter.SetHolder, position: Int) {
        val set = getItem(position)

        // TODO: Hide fields according to exercise.logReps, logWeight, etc.

        holder.itemView.apply {
            edit_reps.setText(set.reps?.toString() ?: "")
            edit_weight.setText(set.weight?.toString() ?: "")
            edit_time.setText(set.time?.toString() ?: "")
            edit_distance.setText(set.distance?.toString() ?: "")
        }
    }
}

private val diffUtil = DiffUtilCallback<Set>({ old, new -> old.setId == new.setId })
