package com.noahjutz.gymroutines.ui.routines.create_routine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.models.Exercise
import com.noahjutz.gymroutines.models.ExerciseReference
import kotlinx.android.synthetic.main.listitem_exercise.view.*
import kotlinx.android.synthetic.main.listitem_routine.view.title

private const val TAG = "CR_ExerciseAdapter"

// TODO: Use ListAdapter for animations
class ExerciseAdapter(private val onExerciseClickListener: OnExerciseClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var realItems: ArrayList<Exercise> = ArrayList()
    private var items: ArrayList<ExerciseReference> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ExerciseRefViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.listitem_exercise, parent, false),
            onExerciseClickListener
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
        private val onExerciseClickListener: OnExerciseClickListener
    ) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        fun bind(exerciseRef: ExerciseReference) {
            itemView.setOnClickListener(this)

            itemView.title.text = exerciseRef.exercise.name
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