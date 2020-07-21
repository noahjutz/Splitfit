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
import com.noahjutz.gymroutines.util.setTextOrUnnamed
import kotlinx.android.synthetic.main.listitem_routine.view.*

/**
 * [ListAdapter] for [FullRoutine]
 */
// TODO: Constructor injection
class RoutineAdapter(
    private val routineListener: RoutineListener
) : ListAdapter<FullRoutine, RoutineAdapter.RoutineHolder>(diffUtil) {
    fun getRoutine(pos: Int): FullRoutine = getItem(pos)

    inner class RoutineHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RoutineHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_routine, parent, false)
    )

    override fun onBindViewHolder(holder: RoutineHolder, position: Int) {
        val fullRoutine = getItem(position)
        val (rName, rDesc) = fullRoutine.routine
        val rExercises = fullRoutine.exercises

        holder.itemView.apply {
            name.setTextOrUnnamed(rName)
            description.setTextOrHide(rDesc)
            exercises.setTextOrHide(formatExercises(rExercises))

            button_edit.setOnClickListener { routineListener.onEditClick(fullRoutine) }
        }
    }

    interface RoutineListener {
        fun onEditClick(fullRoutine: FullRoutine)
    }

    private fun formatExercises(exercises: List<ExerciseImpl>) = exercises
        .sortedBy { it.exerciseHolder.position }
        .joinToString("\n") { "${it.sets.size} x ${it.exercise.name}" }
}

private val diffUtil =
    DiffUtilCallback<FullRoutine>({ old, new -> old.routine.routineId == new.routine.routineId })
