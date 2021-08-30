package io.github.beomjo.search.binding

import android.text.Html
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import io.github.beomjo.search.util.DateHelper
import java.util.Date

@BindingAdapter("html")
fun TextView.setHtml(html: String?) {
    text = if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
        @Suppress("DEPRECATION") Html.fromHtml(html ?: "")
    } else {
        Html.fromHtml(html ?: "", Html.FROM_HTML_MODE_LEGACY)
    }
}

@BindingAdapter("date")
fun TextView.dateString(date: Date?) {
    date ?: kotlin.run {
        text = ""
        return
    }

    text = DateHelper(context).convert(date)
}


@BindingAdapter("hasFocus")
fun EditText.setFocusChanged(hasFocus: Boolean) {
    if (hasFocus) requestFocus()
    else clearFocus()
}

@InverseBindingAdapter(attribute = "hasFocus", event = "focusChanged")
fun EditText.getFocusChanged(): Boolean = hasFocus()

@BindingAdapter("focusChanged")
fun EditText.setFocusChangedListener(listener: InverseBindingListener) {
    setOnFocusChangeListener { _, _ ->
        listener.onChange()
    }
}