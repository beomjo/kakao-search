/*
 * Designed and developed by 2021 beomjo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
