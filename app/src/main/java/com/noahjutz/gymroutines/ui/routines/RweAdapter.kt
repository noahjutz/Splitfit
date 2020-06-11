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

@Suppress("unused")
private const val TAG = "RoutinesAdapter"

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

class RweAdapter(
    private val onRoutineClickListener: OnRoutineClickListener
) : ListAdapter<RwE, RweAdapter.RweHolder>(diffUtil) {
    fun getRweAt(pos: Int): RwE = getItem(pos)

    inner class RweHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                this@RweAdapter.onRoutineClickListener.onRoutineClick(getItem(adapterPosition))
            }
            itemView.setOnLongClickListener {
                this@RweAdapter.onRoutineClickListener.onRoutineLongClick(getItem(adapterPosition))
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RweHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_routine, parent, false)
        return RweHolder(view)
    }

    override fun onBindViewHolder(holder: RweHolder, position: Int) {
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
