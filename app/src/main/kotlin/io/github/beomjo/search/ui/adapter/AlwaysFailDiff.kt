package io.github.beomjo.search.ui.adapter

import androidx.recyclerview.widget.DiffUtil

class AlwaysFailDiff<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = false
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = false
}
