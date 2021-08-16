
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
