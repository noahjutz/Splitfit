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
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.Set

class SetAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = emptyList<Set>()
        set(i) {
            field = i
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        object : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.listitem_set, parent, false)
        ) {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val set = items[position]

        holder.itemView.apply {
            findViewById<TextView>(R.id.edit_reps).text = set.reps?.toString() ?: ""
            findViewById<TextView>(R.id.edit_weight).text = set.weight?.toString() ?: ""
            findViewById<TextView>(R.id.edit_time).text = set.time?.toString() ?: ""
            findViewById<TextView>(R.id.edit_distance).text = set.distance?.toString() ?: ""
        }
    }

    override fun getItemCount(): Int = items.size
}

