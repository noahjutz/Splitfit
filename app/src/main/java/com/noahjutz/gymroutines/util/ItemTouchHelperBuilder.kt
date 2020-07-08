package com.noahjutz.gymroutines.util

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

// TODO: Find another way of passing the adapter id, including the val itemTouchHelperId is not
//  a very nice way of doing it.
data class ItemTouchHelperBuilder(
    var dragDirs: Int = 0,
    var swipeDirs: Int = 0,
    var onMove: (
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) -> Boolean = { _, _, _ -> false },
    var onSwiped: (viewHolder: RecyclerView.ViewHolder, direction: Int, id: Int) -> Unit = { _, _, _ -> },
    var itemTouchHelperId: Int = -1
) {
    fun build() = ItemTouchHelper(
        object : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
            val id = itemTouchHelperId
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = this@ItemTouchHelperBuilder.onMove(recyclerView, viewHolder, target)

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                this@ItemTouchHelperBuilder.onSwiped(viewHolder, direction, itemTouchHelperId)
            }
        }
    )
}
