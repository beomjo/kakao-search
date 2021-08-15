package io.github.beomjo.search.datasource.remote.api.converter

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateConverter : JsonDeserializer<Date> {

    private val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Date? {
        return try {
            dateFormat.parse(json?.asString ?: "")
        } catch (e: ParseException) {
            null
        }
    }

    companion object {
        private const val DATE_FORMAT = ("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    }
}
