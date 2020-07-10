package com.noahjutz.gymroutines.ui.routines.create.pick

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.Exercise
import kotlinx.android.synthetic.main.listitem_exercise.view.*

@Suppress("unused")
private const val TAG = "ExercisesAdapter"

class ExercisesAdapter(
    private val onExerciseClickListener: OnExerciseClickListener
) : ListAdapter<Exercise, ExercisesAdapter.ExerciseHolder>(diffUtil) {
    inner class ExerciseHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ExerciseHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_exercise, parent, false)
    )

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        val exercise = getItem(position)

        holder.itemView.apply {
            name.text = exercise.name
            description.text = exercise.description

            if (exercise.description.trim().isEmpty())
                description.visibility = GONE

            setOnClickListener {
                onExerciseClickListener.onExerciseClick(
                    exercise,
                    this as MaterialCardView
                )
            }
        }
    }

    interface OnExerciseClickListener {
        fun onExerciseClick(exercise: Exercise, card: MaterialCardView)
    }
}


private val diffUtil = object : DiffUtil.ItemCallback<Exercise>() {
    override fun areItemsTheSame(old: Exercise, new: Exercise) = old.exerciseId == new.exerciseId
    override fun areContentsTheSame(old: Exercise, new: Exercise) = old == new
}
