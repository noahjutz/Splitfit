package com.noahjutz.gymExercises

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.models.Exercise
import com.noahjutz.gymroutines.models.ExerciseReference
import kotlinx.android.synthetic.main.exercise_listitem.view.*
import kotlinx.android.synthetic.main.routine_listitem.view.*
import kotlinx.android.synthetic.main.routine_listitem.view.title

class ViewRoutineExerciseRecyclerAdapter(private val onExerciseClickListener: OnExerciseClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var realItems: ArrayList<Exercise> = ArrayList()
    private var items: ArrayList<ExerciseReference> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ExerciseRefViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.exercise_listitem, parent, false),
            onExerciseClickListener,
            realItems
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ExerciseRefViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    fun submitList(list: ArrayList<ExerciseReference>, listE: ArrayList<Exercise>) {
        items = list
        realItems = listE
        notifyDataSetChanged()
    }

    class ExerciseRefViewHolder(
        itemView: View,
        private val onExerciseClickListener: OnExerciseClickListener,
        private val realItems: ArrayList<Exercise>
    ) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val exerciseTitle: TextView = itemView.title

        fun bind(exerciseRef: ExerciseReference) {
            itemView.setOnClickListener(this)
            for (e: Exercise in realItems) {
                if (e.id == exerciseRef.idToRef) {
                    exerciseTitle.text = e.name
                }
            }
            itemView.sets.text = exerciseRef.setsJson
        }

        override fun onClick(v: View?) {
            onExerciseClickListener.onExerciseClick(adapterPosition)
        }
    }

    interface OnExerciseClickListener {
        fun onExerciseClick(pos: Int)
    }
}