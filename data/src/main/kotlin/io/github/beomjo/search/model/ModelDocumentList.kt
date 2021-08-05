package io.github.beomjo.search.model

import com.google.gson.annotations.SerializedName

internal data class ModelDocumentList<T>(
    @SerializedName("meta")
    val meta: ModelMeta,
    @SerializedName("documents")
    val documents: List<T>
)