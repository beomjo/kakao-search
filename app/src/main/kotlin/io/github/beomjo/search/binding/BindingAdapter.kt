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
