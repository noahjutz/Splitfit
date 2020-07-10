package com.noahjutz.gymroutines.ui.routines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.ExerciseImpl
import com.noahjutz.gymroutines.data.domain.FullRoutine
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_routine, parent, false)
        return RoutineHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineHolder, position: Int) {
        val fullRoutine = getItem(position)

        holder.itemView.apply {
            name.text = fullRoutine.routine.name
            description.text = fullRoutine.routine.description

            exercises.text = getExercises(fullRoutine.exercises)

            button_edit.setOnClickListener { onRoutineClickListener.onEditClick(fullRoutine) }
            button_launch.setOnClickListener { onRoutineClickListener.onLaunchClick(fullRoutine) }
            setOnClickListener { onRoutineClickListener.onRoutineClick(this as MaterialCardView) }
        }
    }

    interface OnRoutineClickListener {
        fun onRoutineClick(card: MaterialCardView)
        fun onEditClick(fullRoutine: FullRoutine)
        fun onLaunchClick(fullRoutine: FullRoutine)
    }

    private fun getExercises(exercises: List<ExerciseImpl>): String = exercises
        .sortedBy { it.exerciseHolder.position }
        .joinToString("\n") { "${it.sets.size} x ${it.exercise.name}" }
}

private val diffUtil = object : DiffUtil.ItemCallback<FullRoutine>() {
    override fun areItemsTheSame(old: FullRoutine, new: FullRoutine) =
        old.routine.routineId == new.routine.routineId

    override fun areContentsTheSame(old: FullRoutine, new: FullRoutine) = old == new
}
