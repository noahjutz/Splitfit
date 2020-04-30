package com.noahjutz.gymroutines.ui.routines.create

import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.Exercise
import kotlinx.android.synthetic.main.listitem_exercise.view.*

@Suppress("unused")
private const val TAG = "ExercisesAdapter"

private val diffUtil = object : DiffUtil.ItemCallback<Exercise>() {
    override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
        return oldItem.exerciseId == newItem.exerciseId
    }

    override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
        return oldItem == newItem
    }
}

class ExercisesAdapter(
    private val onItemClickListener: OnItemClickListener
) : ListAdapter<Exercise, ExercisesAdapter.ExerciseHolder>(
    diffUtil
) {
    fun getExerciseAt(pos: Int): Exercise = getItem(pos)
    fun getPosOf(exercise: Exercise) = currentList.indexOf(exercise)

    inner class ExerciseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                this@ExercisesAdapter.onItemClickListener.onExerciseClick(getItem(adapterPosition))
            }
            itemView.setOnLongClickListener {
                this@ExercisesAdapter.onItemClickListener.onExerciseLongClick(getItem(adapterPosition))
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseHolder {
        return ExerciseHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.listitem_exercise, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        val exercise = getItem(position)

        holder.itemView.name.text = exercise.name
        holder.itemView.description.text = exercise.description

        if (exercise.description.trim().isEmpty()) {
            holder.itemView.description.visibility = GONE
        }
    }

    interface OnItemClickListener {
        fun onExerciseClick(exercise: Exercise)
        fun onExerciseLongClick(exercise: Exercise)
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