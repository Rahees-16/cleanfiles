package com.rahees.cleanfiles.ui.search

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahees.cleanfiles.data.FileRepository
import com.rahees.cleanfiles.data.model.FileCategory
import com.rahees.cleanfiles.data.model.FileItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SizeFilter(val displayName: String, val minBytes: Long?, val maxBytes: Long?) {
    ANY("Any", null, null),
    LESS_1MB("< 1 MB", null, 1_048_576L),
    FROM_1_TO_10MB("1-10 MB", 1_048_576L, 10_485_760L),
    FROM_10_TO_100MB("10-100 MB", 10_485_760L, 104_857_600L),
    MORE_100MB("> 100 MB", 104_857_600L, null)
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val fileRepository: FileRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _results = MutableStateFlow<List<FileItem>>(emptyList())
    val results: StateFlow<List<FileItem>> = _results.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _categoryFilter = MutableStateFlow<FileCategory?>(null)
    val categoryFilter: StateFlow<FileCategory?> = _categoryFilter.asStateFlow()

    private val _sizeFilter = MutableStateFlow(SizeFilter.ANY)
    val sizeFilter: StateFlow<SizeFilter> = _sizeFilter.asStateFlow()

    private var searchJob: Job? = null

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
        debounceSearch()
    }

    fun setCategoryFilter(category: FileCategory?) {
        _categoryFilter.value = category
        performSearch()
    }

    fun setSizeFilter(filter: SizeFilter) {
        _sizeFilter.value = filter
        performSearch()
    }

    private fun debounceSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            performSearch()
        }
    }

    private fun performSearch() {
        val currentQuery = _query.value
        if (currentQuery.isBlank()) {
            _results.value = emptyList()
            return
        }

        viewModelScope.launch {
            _isSearching.value = true
            val rootPath = Environment.getExternalStorageDirectory().absolutePath
            val searchResults = fileRepository.searchFiles(
                rootPath = rootPath,
                query = currentQuery,
                categoryFilter = _categoryFilter.value,
                minSize = _sizeFilter.value.minBytes,
                maxSize = _sizeFilter.value.maxBytes
            )
            _results.value = searchResults
            _isSearching.value = false
        }
    }
}
