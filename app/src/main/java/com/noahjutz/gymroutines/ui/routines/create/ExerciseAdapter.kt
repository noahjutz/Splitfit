package com.noahjutz.gymroutines.ui.routines.create

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.Exercise
import kotlinx.android.synthetic.main.listitem_exercise.view.description
import kotlinx.android.synthetic.main.listitem_exercise.view.name
import kotlinx.android.synthetic.main.listitem_exercise_wrapper.view.*

@Suppress("unused")
private const val TAG = "ExerciseAdapter"

class ExerciseAdapter(
    private val onExerciseClickListener: OnExerciseClickListener
) : ListAdapter<Exercise, ExerciseAdapter.ExerciseHolder>(diffUtil) {
    fun getExercise(pos: Int): Exercise = getItem(pos)

    inner class ExerciseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                this@ExerciseAdapter.onExerciseClickListener
                    .onExerciseClick(getItem(adapterPosition))
            }
            itemView.setOnLongClickListener {
                this@ExerciseAdapter.onExerciseClickListener
                    .onExerciseLongClick(getItem(adapterPosition))
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseHolder {
        val viewModel = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_exercise_wrapper, parent, false)
        return ExerciseHolder(viewModel)
    }

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        val exercise = getItem(position)

        holder.itemView.apply {
            name.text = exercise.name
            description.text = exercise.description

            if (exercise.description.trim().isEmpty())
                description.visibility = GONE

            button_add_set.setOnClickListener {
                Log.d(TAG, "add set button clicked.")
            }
        }
    }

    interface OnExerciseClickListener {
        fun onExerciseClick(exercise: Exercise)
        fun onExerciseLongClick(exercise: Exercise)
    }
}

private val diffUtil = object : DiffUtil.ItemCallback<Exercise>() {
    override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
        return oldItem.exerciseId == newItem.exerciseId
    }

    override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
        return oldItem == newItem
    }
}
