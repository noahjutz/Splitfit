package com.noahjutz.gymroutines.ui.routines.create

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.ExerciseWrapper
import kotlinx.android.synthetic.main.listitem_exercise.view.*
import java.lang.NullPointerException

@Suppress("unused")
private const val TAG = "ExercisesAdapter"

private val diffUtil = object : DiffUtil.ItemCallback<ExerciseWrapper>() {
    override fun areItemsTheSame(oldItem: ExerciseWrapper, newItem: ExerciseWrapper): Boolean {
        return oldItem.exerciseId == newItem.exerciseId
    }

    override fun areContentsTheSame(oldItem: ExerciseWrapper, newItem: ExerciseWrapper): Boolean {
        return oldItem == newItem
    }
}

class ExercisesAdapter(
    private val onExerciseClickListener: OnExerciseClickListener,
    private val viewModel: CreateRoutineViewModel
) : ListAdapter<ExerciseWrapper, ExercisesAdapter.ExerciseHolder>(diffUtil) {
    fun getExerciseWrapperAt(pos: Int): ExerciseWrapper = getItem(pos)

    inner class ExerciseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                this@ExercisesAdapter.onExerciseClickListener
                    .onExerciseClick(getItem(adapterPosition))
            }
            itemView.setOnLongClickListener {
                this@ExercisesAdapter.onExerciseClickListener
                    .onExerciseLongClick(getItem(adapterPosition))
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseHolder {
        val viewModel = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_exercise, parent, false)
        return ExerciseHolder(viewModel)
    }

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        val id = getItem(position).exerciseId
        val exercise = viewModel.getExerciseById(id)
            ?: throw NullPointerException("ExerciseWrapper linked to Exercise that doesn't exist")

        holder.apply {
            holder.itemView.name.text = exercise.name
            holder.itemView.description.text = exercise.description

            if (exercise.description.trim().isEmpty())
                holder.itemView.description.visibility = GONE
        }
    }

    interface OnExerciseClickListener {
        fun onExerciseClick(exerciseWrapper: ExerciseWrapper)
        fun onExerciseLongClick(exerciseWrapper: ExerciseWrapper)
    }
}
