package com.noahjutz.gymroutines.ui.routines.create

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

            val setAdapter = SetAdapter(exercise.exerciseHolder.exerciseHolderId)
            mAdapters.add(setAdapter)
            val itemTouchHelper = ItemTouchHelperBuilder(
                // TODO: See [ItemTouchHelper]
                itemTouchHelperId = setAdapter.exerciseHolderId,
                swipeDirs = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                onSwiped = { viewHolder, _, id -> deleteSet(viewHolder.adapterPosition, id) }
            ).build()
            set_container.apply {
                adapter = setAdapter
                layoutManager = LinearLayoutManager(context)
                itemTouchHelper.attachToRecyclerView(this)
            }
        }
    }

    private fun deleteSet(position: Int, id: Int) {
        val set = mAdapters.filter { it.exerciseHolderId == id }[0].getItemPublic(position)
        onExerciseClickListener.onDeleteSet(set)
    }

    fun submitSetList(list: List<Set>) {
        // TODO: Find a better way to do this.
        //  Instead of waiting a hardcoded amount of time, wait until all viewHolders have been bound.
        CoroutineScope(Default).launch {
            delay(25)
            withContext(Main) {
                for (adapter in mAdapters) {
                    adapter.submitList(list.filter { it.exerciseHolderId == adapter.exerciseHolderId })
                }
            }
        }
    }

    interface OnExerciseClickListener {
        fun onExerciseClick(card: MaterialCardView)
        fun onAddSetClick(exercise: ExerciseImpl, card: MaterialCardView)
        fun onDeleteClick(position: Int)
        fun onDeleteSet(set: Set)
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
