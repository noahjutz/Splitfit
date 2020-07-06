package com.noahjutz.gymroutines.ui.routines.create

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
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

        val setTextUnlessNull: TextInputEditText.(Any?) -> Unit = { value ->
            apply {
                if (value == null) {
                    (parent.parent as View).visibility = GONE
                } else {
                    (parent.parent as View).visibility = VISIBLE
                    setText(value.toString())
                }
            }
        }

        holder.itemView.apply {
            edit_reps.setTextUnlessNull(set.reps)
            edit_weight.setTextUnlessNull(set.weight)
            edit_time.setTextUnlessNull(set.time)
            edit_distance.setTextUnlessNull(set.distance)
        }
    }
}