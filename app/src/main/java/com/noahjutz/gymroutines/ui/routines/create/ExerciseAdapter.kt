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
import com.noahjutz.gymroutines.data.domain.ExerciseImpl
import kotlinx.android.synthetic.main.listitem_exercise.view.description
import kotlinx.android.synthetic.main.listitem_exercise.view.name
import kotlinx.android.synthetic.main.listitem_exercise_wrapper.view.*

@Suppress("unused")
private const val TAG = "ExerciseAdapter"

class ExerciseAdapter(
    private val onExerciseClickListener: OnExerciseClickListener
) : ListAdapter<ExerciseImpl, ExerciseAdapter.ExerciseHolder>(diffUtil) {
    fun getExercise(pos: Int): ExerciseImpl = getItem(pos)

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
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_exercise_wrapper, parent, false)
        return ExerciseHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        val exercise = getItem(position)

        holder.itemView.apply {
            name.text = exercise.exercise.name
            description.text = exercise.exercise.description

            sets.text = exercise.sets.toString()

            if (exercise.exercise.description.trim().isEmpty())
                description.visibility = GONE

            button_add_set.setOnClickListener {
                Log.d(TAG, "add set button clicked.")
            }
        }
    }

    interface OnExerciseClickListener {
        fun onExerciseClick(exercise: ExerciseImpl)
        fun onExerciseLongClick(exercise: ExerciseImpl)
    }
}

private val diffUtil = object : DiffUtil.ItemCallback<ExerciseImpl>() {
    override fun areItemsTheSame(oldItem: ExerciseImpl, newItem: ExerciseImpl): Boolean {
        return oldItem.exerciseHolder.exerciseHolderId == newItem.exerciseHolder.exerciseHolderId
    }

    override fun areContentsTheSame(oldItem: ExerciseImpl, newItem: ExerciseImpl): Boolean {
        return oldItem == newItem
    }
}
