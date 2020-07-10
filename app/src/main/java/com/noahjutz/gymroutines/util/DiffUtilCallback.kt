package com.noahjutz.gymroutines.util

import androidx.recyclerview.widget.DiffUtil

/**
 * Implementation of [DiffUtil.ItemCallback] for data classes.
 */
class DiffUtilCallback<T : Equatable>(
    private val itemsEqual: (oldItem: T, newItem: T) -> Boolean = { oldItem, newItem -> oldItem == newItem },
    private val contentsEqual: (oldItem: T, newItem: T) -> Boolean = { oldItem, newItem -> oldItem == newItem }
) : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) = itemsEqual(oldItem, newItem)
    override fun areContentsTheSame(oldItem: T, newItem: T) = contentsEqual(oldItem, newItem)
}

interface Equatable {
    override fun equals(other: Any?): Boolean
}
