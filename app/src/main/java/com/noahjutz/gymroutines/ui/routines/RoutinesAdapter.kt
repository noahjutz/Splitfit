package com.noahjutz.gymroutines.ui.routines

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.Routine
import kotlinx.android.synthetic.main.listitem_routine.view.*

@Suppress("unused")
private const val TAG = "RoutinesAdapter"

private val diffUtil = object : DiffUtil.ItemCallback<Routine>() {
    override fun areItemsTheSame(oldItem: Routine, newItem: Routine): Boolean {
        return oldItem.routineId == newItem.routineId
    }

    override fun areContentsTheSame(oldItem: Routine, newItem: Routine): Boolean {
        return oldItem == newItem
    }
}

class RoutinesAdapter(
    private val onItemClickListener: OnItemClickListener
) : ListAdapter<Routine, RoutinesAdapter.RoutineHolder>(
    diffUtil
) {
    fun getRoutineAt(pos: Int): Routine = getItem(pos)

    inner class RoutineHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                this@RoutinesAdapter.onItemClickListener.onRoutineClick(getItem(adapterPosition))
            }
            itemView.setOnLongClickListener {
                this@RoutinesAdapter.onItemClickListener.onRoutineLongClick(getItem(adapterPosition))
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_routine, parent, false)
        return RoutineHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineHolder, position: Int) {
        val routine = getItem(position)

        holder.itemView.name.text = routine.name
        holder.itemView.description.text = routine.description

        if (routine.description.trim().isEmpty()) {
            holder.itemView.description.visibility = GONE
        }
    }

    interface OnItemClickListener {
        fun onRoutineClick(routine: Routine)
        fun onRoutineLongClick(routine: Routine)
    }
}

/**
 * Custom decorator for correct margins
 */
class MarginItemDecoration(
    private val margin: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = margin
            }
            left = margin
            right = margin
            bottom = margin
        }
    }
}