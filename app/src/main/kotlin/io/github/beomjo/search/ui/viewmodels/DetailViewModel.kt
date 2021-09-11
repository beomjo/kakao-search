package io.github.beomjo.search.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.beomjo.search.base.BaseViewModel
import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.usecase.SetSearchDocumentVisit
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val setSearchDocumentVisit: SetSearchDocumentVisit
) : BaseViewModel() {

    private val _searchDocument = MutableLiveData<SearchDocument>()
    val searchDocument: LiveData<SearchDocument> = _searchDocument

    fun init(searchDocument: SearchDocument?) {
        searchDocument?.let {
            _searchDocument.value = it
        } ?: kotlin.run {
            // TODO DO Back Activity
        }
    }

    fun setVisit() {
        viewModelScope.launch {
            setSearchDocumentVisit(searchDocument.value?.url ?: "")
        }
    }
}
