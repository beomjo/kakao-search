package io.github.beomjo.search.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.usecase.GetSearchDocumentVisit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn

class SearchDocumentViewModel @AssistedInject constructor(
    @Assisted val searchDocument: SearchDocument,
    getSearchDocumentItemVisit: GetSearchDocumentVisit
) {
    val isVisit: LiveData<Boolean> = getSearchDocumentItemVisit(searchDocument.url)
        .distinctUntilChanged()
        .flowOn(Dispatchers.Main)
        .asLiveData()
        .map { it?.url != null }
}

@AssistedFactory
interface SearchDocumentViewModelFactory {
    fun create(searchDocument: SearchDocument): SearchDocumentViewModel
}

