package com.noahjutz.gymroutines.util

import androidx.recyclerview.widget.DiffUtil

/**
 * Implementation of [DiffUtil.ItemCallback] for data classes.
 */
class DiffUtilCallback<T : Equatable>(
    private val itemsEqual: (old: T, new: T) -> Boolean = { old, new -> old == new },
    private val contentsEqual: (old: T, new: T) -> Boolean = { old, new -> old == new }
) : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(old: T, new: T) = itemsEqual(old, new)
    override fun areContentsTheSame(old: T, new: T) = contentsEqual(old, new)
}

interface Equatable {
    override fun equals(other: Any?): Boolean
}
