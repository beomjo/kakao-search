package io.github.beomjo.search.binding

import android.text.Html
import android.widget.TextView
import androidx.databinding.BindingAdapter
import io.github.beomjo.search.R
import java.util.*

@BindingAdapter("html")
fun TextView.setHtml(html: String?) {
    text = if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
        @Suppress("DEPRECATION") Html.fromHtml(html ?: "")
    } else {
        Html.fromHtml(html ?: "", Html.FROM_HTML_MODE_LEGACY)
    }
}

private const val ONE_DAY: Long = 1000 * 60 * 60 * 24

@BindingAdapter("date")
fun TextView.dateString(date: Date?) {
    date ?: kotlin.run {
        text = ""
        return
    }

    val diff = Date().time - date.time
    text = when (diff / ONE_DAY) {
        0L -> context.getString(R.string.today)
        1L -> context.getString(R.string.yesterday)
        else -> context.getString(R.string.date_format, date)
    }
}

