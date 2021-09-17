package io.github.beomjo.search.ui.viewmodels

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.beomjo.search.base.BaseViewModel
import io.github.beomjo.search.entity.SearchDocument
import io.github.beomjo.search.usecase.GetBookmark
import io.github.beomjo.search.usecase.RemoveBookmark
import io.github.beomjo.search.usecase.SetBookmark
import io.github.beomjo.search.usecase.SetSearchDocumentVisit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val setSearchDocumentVisit: SetSearchDocumentVisit,
    private val getBookmark: GetBookmark,
    private val removeBookmark: RemoveBookmark,
    private val setBookmark: SetBookmark
) : BaseViewModel() {

    private val _searchDocument = MutableLiveData<SearchDocument>()
    val searchDocument: LiveData<SearchDocument> = _searchDocument

    private val _isBookmark = MutableLiveData<SearchDocument>()
    val isBookmark = _isBookmark.switchMap { searchDocument ->
        getBookmark(searchDocument).asLiveData()
    }

    fun init(searchDocument: SearchDocument?) {
        searchDocument?.let {
            _searchDocument.value = it
            _isBookmark.value = it
        } ?: kotlin.run {
            finish()
        }
    }

    fun setVisit() {
        viewModelScope.launch {
            setSearchDocumentVisit(searchDocument.value?.url ?: return@launch)
        }
    }

    fun onClickBookmark() {
        viewModelScope.launch {
            if (isBookmark.value == false) {
                setBookmark.invoke(searchDocument.value ?: return@launch)
            } else {
                removeBookmark.invoke(searchDocument.value ?: return@launch)
            }
        }
    }
}
