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

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

data class ItemTouchHelperBuilder(
    private val dragDirs: Int = 0,
    private val swipeDirs: Int = 0,
    private val onMoveCall: (
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) -> Boolean = { _, _, _ -> false },
    private val onSwipedCall: (viewHolder: RecyclerView.ViewHolder, direction: Int) -> Unit = { _, _ -> }
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = onMoveCall(recyclerView, viewHolder, target)

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwipedCall(viewHolder, direction)
    }

    fun build() = ItemTouchHelper(this)
}
