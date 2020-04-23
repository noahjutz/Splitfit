package com.noahjutz.gymroutines.ui.exercises

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.Exercise
import kotlinx.android.synthetic.main.listitem_exercise.view.*
import kotlinx.android.synthetic.main.listitem_routine.view.title

private const val TAG = "VE_ExerciseRecyclerAdapter"

// TODO: Use ListAdapter for animations
class ExerciseAdapter(private val onExerciseClickListener: OnExerciseClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<Exercise> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ExerciseViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.listitem_exercise, parent, false),
            onExerciseClickListener
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ExerciseViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    class ExerciseViewHolder(
        itemView: View,
        private val onExerciseClickListener: OnExerciseClickListener
    ) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnCreateContextMenuListener {

        fun bind(exercise: Exercise) {
            itemView.setOnClickListener(this)
            itemView.title.text = exercise.name
            itemView.exercise_id.text = exercise.id.toString()
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onClick(v: View?) {
            onExerciseClickListener.onExerciseClick(adapterPosition)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu?.add(this.adapterPosition, 420, 0, "Delete")
            menu?.add(this.adapterPosition, 421, 0, "Edit")
        }
    }

    interface OnExerciseClickListener {
        fun onExerciseClick(pos: Int)
    }
}