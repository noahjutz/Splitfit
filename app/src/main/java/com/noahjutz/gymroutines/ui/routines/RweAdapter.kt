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
import com.noahjutz.gymroutines.data.RoutineWithExercises
import kotlinx.android.synthetic.main.listitem_routine.view.*

@Suppress("unused")
private const val TAG = "RoutinesAdapter"

private val diffUtil = object : DiffUtil.ItemCallback<RoutineWithExercises>() {
    override fun areItemsTheSame(
        oldItem: RoutineWithExercises,
        newItem: RoutineWithExercises
    ): Boolean {
        return oldItem.routine.routineId == newItem.routine.routineId
    }

    override fun areContentsTheSame(
        oldItem: RoutineWithExercises,
        newItem: RoutineWithExercises
    ): Boolean {
        return oldItem == newItem
    }
}

class RweAdapter(
    private val onItemClickListener: OnItemClickListener
) : ListAdapter<RoutineWithExercises, RweAdapter.RweHolder>(
    diffUtil
) {
    fun getRweAt(pos: Int): RoutineWithExercises = getItem(pos)

    inner class RweHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                this@RweAdapter.onItemClickListener.onRoutineClick(getItem(adapterPosition))
            }
            itemView.setOnLongClickListener {
                this@RweAdapter.onItemClickListener.onRoutineLongClick(getItem(adapterPosition))
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

        holder.itemView.name.text = rwe.routine.name
        holder.itemView.description.text = rwe.routine.description

        val exercisesSb = StringBuilder()
        for (i in rwe.exercises.indices) {
            exercisesSb.append(rwe.exercises[i].name)
            if (i != rwe.exercises.size - 1)
                exercisesSb.append("\n")
        }

        holder.itemView.exercises.text = exercisesSb.toString()

        if (rwe.routine.description.trim().isEmpty()) holder.itemView.description.visibility = GONE
        if (rwe.exercises.isEmpty()) holder.itemView.exercises.visibility = GONE
    }

    interface OnItemClickListener {
        fun onRoutineClick(rwe: RoutineWithExercises)
        fun onRoutineLongClick(rwe: RoutineWithExercises)
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