package com.noahjutz.gymroutines.ui.routines.create

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.google.android.material.card.MaterialCardView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.ExerciseImpl
import com.noahjutz.gymroutines.data.domain.Set
import com.noahjutz.gymroutines.util.ItemTouchHelperBuilder
import kotlinx.android.synthetic.main.listitem_exercise.view.description
import kotlinx.android.synthetic.main.listitem_exercise.view.name
import kotlinx.android.synthetic.main.listitem_exercise_holder.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

@Suppress("unused")
private const val TAG = "ExerciseAdapter"

class ExerciseAdapter(
    private val onExerciseClickListener: OnExerciseClickListener
) : ListAdapter<ExerciseImpl, ExerciseAdapter.ExerciseHolder>(diffUtil) {
    private val mAdapters: ArrayList<SetAdapter> by lazy { ArrayList<SetAdapter>() }

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

            val itemTouchHelper = ItemTouchHelperBuilder(
                swipeDirs = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                onSwiped = {viewHolder, _ -> deleteSet(viewHolder.adapterPosition) }
            ).build()

            val setAdapter = SetAdapter(exercise.exerciseHolder.exerciseHolderId)
            mAdapters.add(setAdapter)
            set_container.apply {
                adapter = setAdapter
                layoutManager = LinearLayoutManager(context)
                itemTouchHelper.attachToRecyclerView(this)
            }
        }
    }

    private fun deleteSet(position: Int) {
        // TODO: call listeners deleteSet function (TODO: Create a listener interface and pass it as
        //  a dependency)
    }

    fun submitSetList(list: List<Set>) {
        // TODO: Find a better way to do this.
        //  Instead of waiting a hardcoded amount of time, wait until all viewHolders have been bound.
        CoroutineScope(Default).launch {
            delay(1000)
            withContext(Main) {
                for (adapter in mAdapters) {
                    adapter.submitList(list.filter { it.exerciseHolderId == adapter.exerciseHolderId })
                    Log.d(TAG, "submitSetList: ${adapter.exerciseHolderId}: ${list.filter { it.exerciseHolderId == adapter.exerciseHolderId }.map { it.setId }}")
                }
            }
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
