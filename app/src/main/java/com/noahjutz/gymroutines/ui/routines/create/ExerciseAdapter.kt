package com.noahjutz.gymroutines.ui.routines.create

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.*
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

            button_delete.setOnClickListener { onExerciseClickListener.onDeleteClick(holder.adapterPosition) }

            setOnClickListener { onExerciseClickListener.onExerciseClick(this as MaterialCardView) }

            val myAdapter = SetAdapter()
            val itemTouchHelper = object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ) = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    // TODO
                }

            }
            set_container.apply {
                adapter = myAdapter
                layoutManager = LinearLayoutManager(context)
                ItemTouchHelper(itemTouchHelper).attachToRecyclerView(this)
            }
            //// (sample data)
            // val sampleData = listOf(
            //     com.noahjutz.gymroutines.data.domain.Set(-1, 12),
            //     com.noahjutz.gymroutines.data.domain.Set(-1, null, 32.2, 2),
            //     com.noahjutz.gymroutines.data.domain.Set(-1, 1, 2.3, 23, 22.11),
            //     com.noahjutz.gymroutines.data.domain.Set(-1, null, null, null, 42.22)
            // )
            // myAdapter.submitList(sampleData)
            // TODO: Observe sets in order to push UI changes
            myAdapter.submitList(exercise.sets)
        }
    }

    interface OnExerciseClickListener {
        fun onExerciseClick(card: MaterialCardView)
        fun onAddSetClick(exercise: ExerciseImpl, card: MaterialCardView)
        fun onDeleteClick(position: Int)
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
