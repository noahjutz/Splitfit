package com.noahjutz.gymroutines.ui.routines.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.Set
import kotlinx.android.synthetic.main.listitem_set.view.*

private val diffUtil = object : DiffUtil.ItemCallback<Set>() {
    override fun areItemsTheSame(oldItem: Set, newItem: Set): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Set, newItem: Set): Boolean {
        return oldItem.distance == newItem.distance
                && oldItem.reps == newItem.reps
                && oldItem.time == newItem.time
                && oldItem.weight == newItem.weight
    }

}

class SetAdapter : ListAdapter<Set, SetAdapter.SetHolder>(diffUtil) {
    inner class SetHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetAdapter.SetHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_set, parent, false)
        return SetHolder(itemView)
    }

    override fun onBindViewHolder(holder: SetAdapter.SetHolder, position: Int) {
        val set = getItem(position)

        // TODO: Hide text fields according to: exercise.logReps, exercise.logWeight, etc.

        holder.itemView.apply {
            edit_reps.setText(set.reps.toString())
            edit_weight.setText(set.weight.toString())
            edit_time.setText(set.time.toString())
            edit_distance.setText(set.distance.toString())
        }
    }
}