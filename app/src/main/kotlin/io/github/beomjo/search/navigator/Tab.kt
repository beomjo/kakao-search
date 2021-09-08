package io.github.beomjo.search.navigator

import io.github.beomjo.search.R
import io.github.beomjo.search.ui.fragment.FavoriteFragment
import io.github.beomjo.search.ui.fragment.SearchFragment

enum class Tab(
    val itemId: Int,
    val tag: String
) {
    SEARCH(R.id.search_dest, SearchFragment.TAG),
    FAVORITE(R.id.favorite, FavoriteFragment.TAG);

    companion object
}

fun Tab.Companion.otherTab(exceptTag: String): Sequence<Tab> = Tab.values()
    .asSequence()
    .filter { it.tag != exceptTag }

fun Tab.Companion.isContainsTag(tag: String): Boolean = Tab.values()
    .map { it.tag }
    .contains(tag)
