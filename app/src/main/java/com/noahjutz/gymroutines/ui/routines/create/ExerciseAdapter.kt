package com.noahjutz.gymroutines.ui.routines.create

import android.os.Build
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.ExerciseImpl
import kotlinx.android.synthetic.main.listitem_exercise.view.description
import kotlinx.android.synthetic.main.listitem_exercise.view.name
import kotlinx.android.synthetic.main.listitem_exercise_holder.view.*

@Suppress("unused")
private const val TAG = "ExerciseAdapter"

class ExerciseAdapter(
    private val onExerciseClickListener: OnExerciseClickListener
) : ListAdapter<ExerciseImpl, ExerciseAdapter.ExerciseHolder>(diffUtil) {
    fun getExercise(pos: Int): ExerciseImpl = getItem(pos)

    inner class ExerciseHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_exercise_holder, parent, false)
        return ExerciseHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        val exercise = getItem(position)

        holder.itemView.apply {
            name.text = exercise.exercise.name
            description.text = exercise.exercise.description

            if (exercise.exercise.description.trim().isEmpty())
                description.visibility = GONE

            button_add_set.setOnClickListener {
                onExerciseClickListener.onAddSetClick(
                    exercise,
                    this as MaterialCardView
                )
            }

            setOnClickListener { onExerciseClickListener.onExerciseClick(this as MaterialCardView) }
        }
    }

    interface OnExerciseClickListener {
        fun onExerciseClick(card: MaterialCardView)
        fun onAddSetClick(exercise: ExerciseImpl, card: MaterialCardView)
    }

    fun addViewToCard(cardView: MaterialCardView, view: View) {
        TransitionManager.beginDelayedTransition(cardView.exercise_holder_root, AutoTransition())
        cardView.parent_layout.addView(view)
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
