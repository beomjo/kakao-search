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

package io.github.beomjo.search.util

import android.content.Context
import androidx.annotation.StringRes
import io.github.beomjo.search.R.string
import java.util.Date
import javax.inject.Inject

class DateHelper @Inject constructor(
    private val context: Context
) {

    fun convert(date: Date?, @StringRes formatRes: Int = string.date): String {
        if (date == null) return ""
        val diff = Date().time - date.time
        return when (diff / ONE_DAY) {
            0L -> context.getString(string.today)
            1L -> context.getString(string.yesterday)
            else -> context.getString(formatRes, date.time)
        }
    }

    companion object {
        const val ONE_DAY: Long = 1000 * 60 * 60 * 24
    }
}
