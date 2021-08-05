package io.github.beomjo.search.model

import com.google.gson.annotations.SerializedName

internal data class ModelMeta(
    @SerializedName("is_end")
    val isEnd: Boolean,
    @SerializedName("pageable_count")
    val pageableCount: Int,
    @SerializedName("total_count")
    val totalCount: Int
)