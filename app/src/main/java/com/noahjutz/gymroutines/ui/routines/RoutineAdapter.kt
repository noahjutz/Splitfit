package com.noahjutz.gymroutines.ui.routines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.Routine
import kotlinx.android.synthetic.main.listitem_routine.view.*

private const val TAG = "RoutinesRoutineAdapter"

// TODO: Use ListAdapter for animations
class RoutineAdapter(private val onRoutineClickListener: OnRoutineClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<Routine> = ArrayList()

    fun submitList(list: java.util.ArrayList<Routine>) {
        items = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RoutineViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.listitem_routine, parent, false),
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

    class RoutineViewHolder(
        itemView: View,
        private val onRoutineClickListener: OnRoutineClickListener
    ) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        fun bind(routine: Routine) {
            itemView.setOnClickListener(this)
            itemView.title.text = routine.title
            itemView.content.text = routine.note
        }

        override fun onClick(v: View?) {
            onRoutineClickListener.onRoutineClick(adapterPosition)
        }
    }

    interface OnRoutineClickListener {
        fun onRoutineClick(pos: Int)
    }
}