package com.noahjutz.gymroutines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.noahjutz.gymroutines.models.Routine
import kotlinx.android.synthetic.main.routine_listitem.view.*

class RoutineRecyclerAdapter(private val onRoutineClickListener: OnRoutineClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<Routine> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RoutineViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.routine_listitem, parent, false),
            onRoutineClickListener
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RoutineViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    fun submitList(list: ArrayList<Routine>) {
        items = list
        notifyDataSetChanged()
    }

    class RoutineViewHolder(
        itemView: View,
        private val onRoutineClickListener: OnRoutineClickListener
    ) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val routineTitle: TextView = itemView.title
        private val routineContent: TextView = itemView.content

        fun bind(routine: Routine) {
            itemView.setOnClickListener(this)
            routineTitle.text = routine.title
            routineContent.text = routine.content
        }

        override fun onClick(v: View?) {
            onRoutineClickListener.onRoutineClick(adapterPosition)
        }
    }

    interface OnRoutineClickListener {
        fun onRoutineClick(pos: Int)
    }
}