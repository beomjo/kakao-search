package io.github.beomjo.search.navigator

import io.github.beomjo.search.R
import io.github.beomjo.search.ui.fragment.BookmarkFragment
import io.github.beomjo.search.ui.fragment.SearchFragment

enum class Tab(
    val itemId: Int,
    val tag: String
) {
    SEARCH(R.id.search_dest, SearchFragment.TAG),
    BOOKMARK(R.id.bookmark_dest, BookmarkFragment.TAG);

    companion object {
        fun from(itemId: Int): Tab? = values().firstOrNull { it.itemId == itemId }
    }
}

fun Tab.Companion.otherTab(exceptTag: String): Sequence<Tab> =
    Tab.values()
        .asSequence()
        .filter { it.tag != exceptTag }
