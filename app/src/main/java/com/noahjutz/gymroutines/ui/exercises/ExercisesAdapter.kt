package com.noahjutz.gymroutines.ui.exercises

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.Exercise
import com.noahjutz.gymroutines.util.DiffUtilCallback
import com.noahjutz.gymroutines.util.setTextOrHide
import kotlinx.android.synthetic.main.listitem_exercise.view.*

// TODO: Constructor injection
class ExercisesAdapter(
    private val onExerciseClickListener: OnExerciseClickListener
) : ListAdapter<Exercise, ExercisesAdapter.ExerciseHolder>(diffUtil) {
    fun getExerciseAt(pos: Int): Exercise = getItem(pos)

    inner class ExerciseHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ExerciseHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_exercise, parent, false)
    )

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        val exercise = getItem(position)
        val (exerciseName, exerciseDesc) = exercise

        holder.itemView.apply {
            name.text = exerciseName
            description.setTextOrHide(exerciseDesc)

            setOnClickListener { onExerciseClickListener.onExerciseClick(exercise) }
        }
    }

    interface OnExerciseClickListener {
        fun onExerciseClick(exercise: Exercise)
    }
}

private val diffUtil =
    DiffUtilCallback<Exercise>({ old, new -> old.exerciseId == new.exerciseId })
