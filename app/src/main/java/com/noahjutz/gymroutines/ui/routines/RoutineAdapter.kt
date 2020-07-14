package com.noahjutz.gymroutines.ui.routines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.ExerciseImpl
import com.noahjutz.gymroutines.data.domain.FullRoutine
import com.noahjutz.gymroutines.util.DiffUtilCallback
import com.noahjutz.gymroutines.util.setTextOrHide
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

    inner class RoutineHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RoutineHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_routine, parent, false)
    )

    override fun onBindViewHolder(holder: RoutineHolder, position: Int) {
        val fullRoutine = getItem(position)

        holder.itemView.apply {
            name.text = fullRoutine.routine.name
            description.setTextOrHide(fullRoutine.routine.description)
            exercises.setTextOrHide(getExercises(fullRoutine.exercises))

            button_edit.setOnClickListener { onRoutineClickListener.onEditClick(fullRoutine) }
        }
    }

    interface OnRoutineClickListener {
        fun onEditClick(fullRoutine: FullRoutine)
    }

    private fun getExercises(exercises: List<ExerciseImpl>) = exercises
        .sortedBy { it.exerciseHolder.position }
        .joinToString("\n") { "${it.sets.size} x ${it.exercise.name}" }
}

private val diffUtil =
    DiffUtilCallback<FullRoutine>({ old, new -> old.routine.routineId == new.routine.routineId })