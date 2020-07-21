package com.noahjutz.gymroutines.ui.routines.create.pick

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.Exercise
import com.noahjutz.gymroutines.util.DiffUtilCallback
import com.noahjutz.gymroutines.util.setTextOrHide
import com.noahjutz.gymroutines.util.setTextOrUnnamed
import kotlinx.android.synthetic.main.listitem_exercise.view.*

@Suppress("unused")
private const val TAG = "ExercisesAdapter"

class ExercisesAdapter(
    private val exerciseListener: ExerciseListener
) : ListAdapter<Exercise, ExercisesAdapter.ExerciseHolder>(diffUtil) {
    inner class ExerciseHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ExerciseHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_exercise, parent, false)
    )

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        val exercise = getItem(position)
        val (exerciseName, exerciseDesc) = exercise

        holder.itemView.apply {
            name.setTextOrUnnamed(exerciseName)
            description.setTextOrHide(exerciseDesc)

            setOnClickListener {
                exerciseListener.onExerciseClick(exercise, this as MaterialCardView)
            }
        }
    }

    interface ExerciseListener {
        fun onExerciseClick(exercise: Exercise, card: MaterialCardView)
    }
}

private val diffUtil = DiffUtilCallback<Exercise>({ old, new -> old.exerciseId == new.exerciseId })
