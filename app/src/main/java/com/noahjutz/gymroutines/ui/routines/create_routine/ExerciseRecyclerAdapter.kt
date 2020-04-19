package com.noahjutz.gymroutines.ui.routines.create_routine

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.models.Exercise
import com.noahjutz.gymroutines.models.ExerciseReference
import com.noahjutz.gymroutines.models.Set
import kotlinx.android.synthetic.main.exercise_listitem.view.*
import kotlinx.android.synthetic.main.routine_listitem.view.title

private const val TAG = "CR_ExerciseRecyclerAdapter"

class CreateRoutineExerciseRecyclerAdapter(private val onExerciseClickListener: OnExerciseClickListener) :
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
        RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnCreateContextMenuListener {
        private val exerciseTitle: TextView = itemView.title

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            if (adapterPosition > 0) {
                menu?.add(adapterPosition, 2423, 0, "Move Up")
            }
            menu?.add(adapterPosition, 2420, 0, "Remove")
            menu?.add(adapterPosition, 2421, 0, "Edit")
            menu?.add(adapterPosition, 2422, 0, "Add Set")
        }

        fun bind(exerciseRef: ExerciseReference) {
            itemView.setOnClickListener(this)
            itemView.setOnCreateContextMenuListener(this)
            for (e: Exercise in realItems) {
                if (e == exerciseRef.exercise) {
                    exerciseTitle.text = e.name
                }
            }
            itemView.sets.text = exerciseRef.sets.toString()
        }

        override fun onClick(v: View?) {
            onExerciseClickListener.onExerciseClick(adapterPosition)
        }
    }

    interface OnExerciseClickListener {
        fun onExerciseClick(pos: Int)
    }
}