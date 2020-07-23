package com.noahjutz.gymroutines.ui.routines.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.ExerciseImpl
import com.noahjutz.gymroutines.data.domain.Set
import com.noahjutz.gymroutines.util.DiffUtilCallback
import com.noahjutz.gymroutines.util.ItemTouchHelperBuilder
import com.noahjutz.gymroutines.util.setTextOrHide
import com.noahjutz.gymroutines.util.setTextOrUnnamed
import kotlinx.android.synthetic.main.listitem_exercise.view.description
import kotlinx.android.synthetic.main.listitem_exercise.view.name
import kotlinx.android.synthetic.main.listitem_exercise_holder.view.*

class ExerciseAdapter(
    private val exerciseHolderListener: ExerciseHolderListener
) : ListAdapter<ExerciseImpl, ExerciseAdapter.ExerciseHolder>(diffUtil) {
    val mAdapters = HashMap<Int, SetAdapter>()

    fun getExercise(pos: Int): ExerciseImpl = getItem(pos)

    inner class ExerciseHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ExerciseHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_exercise_holder, parent, false)
    )

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        val exerciseImpl = getItem(position)
        val (exerciseName, exerciseDesc) = exerciseImpl.exercise

        holder.itemView.apply {
            name.setTextOrUnnamed(exerciseName)
            description.setTextOrHide(exerciseDesc)

            button_add_set.setOnClickListener { exerciseHolderListener.onAddSetClick(exerciseImpl) }

            // TODO: Delegate the responsibility of setting up the recycler views to [CreateRoutineFragment]
            val setAdapter = SetAdapter()
            mAdapters[exerciseImpl.exerciseHolder.exerciseHolderId] = setAdapter

            val itemTouchHelper = ItemTouchHelperBuilder(
                swipeDirs = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                onSwipedCall = { viewHolder, _ ->
                    deleteSet(
                        viewHolder.adapterPosition,
                        exerciseImpl.exerciseHolder.exerciseHolderId,
                        holder.adapterPosition
                    )
                }
            ).build()

            set_container.apply {
                adapter = setAdapter
                layoutManager = LinearLayoutManager(context)
                itemTouchHelper.attachToRecyclerView(this)
            }
        }
    }

    private fun deleteSet(position: Int, id: Int, exercisePosition: Int) {
        val set = mAdapters[id]!!.getSet(position)
        exerciseHolderListener.onDeleteSet(set, exercisePosition)
    }

    interface ExerciseHolderListener {
        fun onAddSetClick(exercise: ExerciseImpl)
        fun onDeleteSet(set: Set, position: Int)
    }
}

private val diffUtil =
    DiffUtilCallback<ExerciseImpl>({ old, new -> old.exerciseHolder.exerciseHolderId == new.exerciseHolder.exerciseHolderId })
