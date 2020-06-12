package com.noahjutz.gymroutines.ui.routines

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.RwE
import kotlinx.android.synthetic.main.listitem_routine.view.*
import com.noahjutz.gymroutines.data.domain.FullRoutine

@Suppress("unused")
private const val TAG = "RoutinesAdapter"

/**
 * [ListAdapter] for [FullRoutine]
 * TODO: Replace [RwE] with [FullRoutine]
 */
class RoutineAdapter(
    private val onRoutineClickListener: OnRoutineClickListener
) : ListAdapter<RwE, RoutineAdapter.RoutineHolder>(diffUtil) {
    fun getRoutine(pos: Int): RwE = getItem(pos)

    inner class RoutineHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                this@RoutineAdapter.onRoutineClickListener.onRoutineClick(getItem(adapterPosition))
            }
            itemView.setOnLongClickListener {
                this@RoutineAdapter.onRoutineClickListener.onRoutineLongClick(getItem(adapterPosition))
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
        val rwe = getItem(position)

        holder.apply {
            holder.itemView.name.text = rwe.routine.name
            holder.itemView.description.text = rwe.routine.description

            if (rwe.routine.description.trim().isEmpty()) itemView.description.visibility = GONE
        }
    }

    interface OnRoutineClickListener {
        fun onRoutineClick(rwe: RwE)
        fun onRoutineLongClick(rwe: RwE)
    }
}

private val diffUtil = object : DiffUtil.ItemCallback<RwE>() {
    override fun areItemsTheSame(
        oldItem: RwE,
        newItem: RwE
    ): Boolean = oldItem.routine.routineId == newItem.routine.routineId

    override fun areContentsTheSame(
        oldItem: RwE,
        newItem: RwE
    ): Boolean = oldItem == newItem
}
