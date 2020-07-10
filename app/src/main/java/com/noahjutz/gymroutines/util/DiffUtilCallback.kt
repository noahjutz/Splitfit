package com.noahjutz.gymroutines.util

import androidx.recyclerview.widget.DiffUtil

/**
 * Implementation of [DiffUtil.ItemCallback] for data classes.
 */
class DiffUtilCallback<T : Equatable> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem == newItem
    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
}

interface Equatable {
    override fun equals(other: Any?): Boolean
}
