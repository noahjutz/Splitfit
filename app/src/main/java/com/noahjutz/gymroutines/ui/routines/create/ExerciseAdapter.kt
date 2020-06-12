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
import com.noahjutz.gymroutines.data.domain.ExerciseWrapper
import kotlinx.android.synthetic.main.listitem_exercise.view.description
import kotlinx.android.synthetic.main.listitem_exercise.view.name
import kotlinx.android.synthetic.main.listitem_exercise_wrapper.view.*
import java.lang.NullPointerException

@Suppress("unused")
private const val TAG = "ExerciseAdapter"

class ExerciseAdapter(
    private val onExerciseClickListener: OnExerciseClickListener,
    private val viewModel: CreateRoutineViewModel
) : ListAdapter<ExerciseWrapper, ExerciseAdapter.ExerciseHolder>(diffUtil) {
    fun getExerciseWrapperAt(pos: Int): ExerciseWrapper = getItem(pos)

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
        val exerciseId = getItem(position).exerciseId
        val ewId = getItem(position).exerciseWrapperId
        val exercise = viewModel.getExerciseById(exerciseId)
            ?: throw NullPointerException("ExerciseWrapper linked to Exercise that doesn't exist")

        val setList = viewModel.getSetsById(ewId)
        Log.d(TAG, "ewId: $ewId, sets: $setList")

        holder.itemView.apply {
            name.text = exercise.name
            description.text = exercise.description

            if (exercise.description.trim().isEmpty())
                description.visibility = GONE

            val exerciseWrapperId = getItem(position).exerciseWrapperId
            button_add_set.setOnClickListener { viewModel.addSet(exerciseWrapperId) }
        }
    }

    interface OnExerciseClickListener {
        fun onExerciseClick(exerciseWrapper: ExerciseWrapper)
        fun onExerciseLongClick(exerciseWrapper: ExerciseWrapper)
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
