package io.github.beomjo.search.ui.adapter.viewholders

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.beomjo.search.R
import io.github.beomjo.search.databinding.SearchControlMenuItemBinding
import io.github.beomjo.search.entity.DocumentType
import io.github.beomjo.search.entity.SortType

class SearchControlMenuViewHolder(
    val binding: SearchControlMenuItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private var selectedSort = SortType.TITLE

    fun bind(
        onFilterSelected: (DocumentType) -> Unit,
        onSortSelected: (SortType) -> Unit
    ) {
        binding.apply {
            with(filter) {
                adapter = ArrayAdapter(
                    binding.root.context,
                    android.R.layout.simple_spinner_item,
                    DocumentType.values().map { it.toString() }
                ).also {
                    it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
                setSelection(0)
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        onFilterSelected(DocumentType.values()[position])
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) = Unit
                }
            }
            with(sort) {
                text = selectedSort.toString()
                setOnClickListener { button ->
                    AlertDialog.Builder(button.context)
                        .setSingleChoiceItems(
                            SortType.values().map { it.toString() }.toTypedArray(),
                            SortType.values().toList().indexOf(selectedSort)
                        ) { _, which ->
                            selectedSort = SortType.values()[which]
                        }
                        .setPositiveButton(R.string.ok) { _, _ ->
                            sort.text = selectedSort.toString()
                            onSortSelected(selectedSort)
                        }
                        .setNegativeButton(R.string.cancel) { _, _ -> }
                        .show()
                }
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): SearchControlMenuViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.search_control_menu_item, parent, false)
            val binding = SearchControlMenuItemBinding.bind(view)
            return SearchControlMenuViewHolder(binding)
        }
    }
}
