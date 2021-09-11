package io.github.beomjo.search.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.entity.Visit
import io.github.beomjo.search.usecase.GetSearchItemVisit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SearchDocumentViewModel @AssistedInject constructor(
    @Assisted val searchDocument: SearchDocument,
    getSearchItemVisit: GetSearchItemVisit
) {
    val isVisit: LiveData<Boolean> = getSearchItemVisit(searchDocument.url)
        .distinctUntilChanged()
        .flowOn(Dispatchers.Main)
        .asLiveData()
        .map { it?.url != null }
}

@AssistedFactory
interface SearchDocumentViewModelFactory {
    fun create(searchDocument: SearchDocument): SearchDocumentViewModel
}

