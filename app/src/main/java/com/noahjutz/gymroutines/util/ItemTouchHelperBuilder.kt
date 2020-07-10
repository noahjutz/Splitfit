package com.noahjutz.gymroutines.util

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

// TODO: Find another way of passing the adapter id, including the val itemTouchHelperId is not
//  a very nice way of doing it.
data class ItemTouchHelperBuilder(
    private val dragDirs: Int = 0,
    private val swipeDirs: Int = 0,
    private val onMoveCall: (
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) -> Boolean = { _, _, _ -> false },
    private val onSwipedCall: (viewHolder: RecyclerView.ViewHolder, direction: Int, id: Int) -> Unit = { _, _, _ -> },
    private val itemTouchHelperId: Int = -1
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = onMoveCall(recyclerView, viewHolder, target)

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwipedCall(viewHolder, direction, itemTouchHelperId)
    }

    fun build() = ItemTouchHelper(this)
}
