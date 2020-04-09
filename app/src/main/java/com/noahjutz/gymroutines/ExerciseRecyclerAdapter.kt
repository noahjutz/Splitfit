package com.noahjutz.gymExercises

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.models.Exercise
import kotlinx.android.synthetic.main.routine_listitem.view.*

class ExerciseRecyclerAdapter(private val onExerciseClickListener: OnExerciseClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<Exercise> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ExerciseViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.exercise_listitem, parent, false),
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

    fun submitList(list: ArrayList<Exercise>) {
        items = list
        notifyDataSetChanged()
    }

    class ExerciseViewHolder(
        itemView: View,
        private val onExerciseClickListener: OnExerciseClickListener
    ) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnCreateContextMenuListener {
        private val exerciseTitle: TextView = itemView.title

        fun bind(Exercise: Exercise) {
            itemView.setOnClickListener(this)
            exerciseTitle.text = Exercise.name
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
            menu?.setHeaderTitle(exerciseTitle.text.toString())
            menu?.add(this.adapterPosition, 420, 0, "Delete")
            menu?.add(this.adapterPosition, 421, 0, "Edit")
        }
    }

    interface OnExerciseClickListener {
        fun onExerciseClick(pos: Int)
    }
}