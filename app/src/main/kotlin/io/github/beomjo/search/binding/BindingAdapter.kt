package io.github.beomjo.search.binding

import android.text.Html
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("html")
fun TextView.setHtml(html: String?) {
    text = if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
        @Suppress("DEPRECATION") Html.fromHtml(html ?: "")
    } else {
        Html.fromHtml(html ?: "", Html.FROM_HTML_MODE_LEGACY)
    }
}