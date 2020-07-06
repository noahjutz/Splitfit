package com.noahjutz.gymroutines.util

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

data class ItemTouchHelperBuilder(
    var dragDirs: Int = 0,
    var swipeDirs: Int = 0,
    var onMove: (
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) -> Boolean = { _, _, _ -> false },
    var onSwiped: (viewHolder: RecyclerView.ViewHolder, direction: Int) -> Unit = { _, _ -> }
) {
    fun build() = ItemTouchHelper(
        object : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = this@ItemTouchHelperBuilder.onMove(recyclerView, viewHolder, target)

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                this@ItemTouchHelperBuilder.onSwiped(viewHolder, direction)
            }
        }
    )
}
