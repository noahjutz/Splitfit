package com.noahjutz.gymroutines.ui.routines

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.FullRoutine
import kotlinx.android.synthetic.main.listitem_routine.view.*

@Suppress("unused")
private const val TAG = "RoutineAdapter"

/**
 * [ListAdapter] for [FullRoutine]
 */
class RoutineAdapter(
    private val onRoutineClickListener: OnRoutineClickListener
) : ListAdapter<FullRoutine, RoutineAdapter.RoutineHolder>(diffUtil) {
    fun getRoutine(pos: Int): FullRoutine = getItem(pos)

    inner class RoutineHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                this@RoutineAdapter.onRoutineClickListener
                    .onRoutineClick(getItem(adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_routine, parent, false)
        return RoutineHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineHolder, position: Int) {
        val fullRoutine = getItem(position)

        holder.itemView.apply {
            name.text = fullRoutine.routine.name
            description.text = fullRoutine.routine.description

            if (fullRoutine.routine.description.trim().isEmpty()) description.visibility =
                GONE

            button_edit.setOnClickListener { onRoutineClickListener.onEditClick(fullRoutine) }
            button_launch.setOnClickListener { onRoutineClickListener.onLaunchClick(fullRoutine) }
            button_expand.addOnCheckedChangeListener { button, isChecked ->
                onRoutineClickListener.onExpandClick(button, isChecked, this as MaterialCardView)
            }
        }
    }

    interface OnRoutineClickListener {
        fun onRoutineClick(fullRoutine: FullRoutine)
        fun onEditClick(fullRoutine: FullRoutine)
        fun onLaunchClick(fullRoutine: FullRoutine)
        fun onExpandClick(button: Button, isChecked: Boolean, cardView: MaterialCardView)
    }
}

private val diffUtil = object : DiffUtil.ItemCallback<FullRoutine>() {
    override fun areItemsTheSame(
        oldItem: FullRoutine,
        newItem: FullRoutine
    ): Boolean = oldItem.routine.routineId == newItem.routine.routineId

    override fun areContentsTheSame(
        oldItem: FullRoutine,
        newItem: FullRoutine
    ): Boolean = oldItem == newItem
}
