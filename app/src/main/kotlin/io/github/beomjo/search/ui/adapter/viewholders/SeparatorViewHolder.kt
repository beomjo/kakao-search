package io.github.beomjo.search.ui.adapter.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.beomjo.search.databinding.SeparatorListItemBinding

class SeparatorViewHolder(
    private val binding: SeparatorListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(separatorText: String) {
        binding.separatorDescription.text = separatorText
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): SeparatorViewHolder {
            return SeparatorViewHolder(
                SeparatorListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}