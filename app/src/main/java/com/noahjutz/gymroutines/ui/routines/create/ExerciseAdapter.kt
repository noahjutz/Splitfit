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
import kotlinx.android.synthetic.main.listitem_exercise.view.description
import kotlinx.android.synthetic.main.listitem_exercise.view.name
import kotlinx.android.synthetic.main.listitem_exercise_holder.view.*

@Suppress("unused")
private const val TAG = "ExerciseAdapter"

class ExerciseAdapter(
    private val onExerciseClickListener: OnExerciseClickListener
) : ListAdapter<ExerciseImpl, ExerciseAdapter.ExerciseHolder>(diffUtil) {
    val mAdapters: ArrayList<SetAdapter> by lazy { ArrayList<SetAdapter>() }

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
            name.text = exerciseName
            description.setTextOrHide(exerciseDesc)

            button_add_set.setOnClickListener { onExerciseClickListener.onAddSetClick(exerciseImpl) }

            // TODO: Delegate the responsibility of setting up the recycler views to [CreateRoutineFragment]
            val setAdapter = SetAdapter(exerciseImpl.exerciseHolder.exerciseHolderId)
            mAdapters.add(setAdapter)

            val itemTouchHelper = ItemTouchHelperBuilder(
                // TODO: See [ItemTouchHelper]
                itemTouchHelperId = setAdapter.exerciseHolderId,
                swipeDirs = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                onSwipedCall = { viewHolder, _, id ->
                    deleteSet(viewHolder.adapterPosition, id, holder.adapterPosition)
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
        val set = mAdapters.filter { it.exerciseHolderId == id }[0].getItemPublic(position)
        onExerciseClickListener.onDeleteSet(set, exercisePosition)
    }

    interface OnExerciseClickListener {
        fun onAddSetClick(exercise: ExerciseImpl)
        fun onDeleteSet(set: Set, position: Int)
    }
}

private val diffUtil =
    DiffUtilCallback<ExerciseImpl>({ old, new -> old.exerciseHolder.exerciseHolderId == new.exerciseHolder.exerciseHolderId })