package com.rahees.cleanfiles.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahees.cleanfiles.data.FileRepository
import com.rahees.cleanfiles.data.model.FileCategory
import com.rahees.cleanfiles.data.model.FileItem
import com.rahees.cleanfiles.data.model.FileOperation
import com.rahees.cleanfiles.data.model.SortField
import com.rahees.cleanfiles.data.model.SortOption
import com.rahees.cleanfiles.data.model.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val fileRepository: FileRepository
) : ViewModel() {

    private val _files = MutableStateFlow<List<FileItem>>(emptyList())
    val files: StateFlow<List<FileItem>> = _files.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedPaths = MutableStateFlow<Set<String>>(emptySet())
    val selectedPaths: StateFlow<Set<String>> = _selectedPaths.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption(SortField.DATE, SortOrder.DESCENDING))
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    private val _isGridView = MutableStateFlow(true)
    val isGridView: StateFlow<Boolean> = _isGridView.asStateFlow()

    fun loadCategory(category: FileCategory) {
        viewModelScope.launch {
            _isLoading.value = true
            val fileList = fileRepository.getFilesByCategory(category)
            _files.value = sortFiles(fileList, _sortOption.value)
            _isLoading.value = false
        }
    }

    fun toggleSelection(path: String) {
        val current = _selectedPaths.value.toMutableSet()
        if (current.contains(path)) current.remove(path) else current.add(path)
        _selectedPaths.value = current
    }

    fun selectAll() {
        _selectedPaths.value = _files.value.map { it.path }.toSet()
    }

    fun clearSelection() {
        _selectedPaths.value = emptySet()
    }

    fun toggleViewMode() {
        _isGridView.value = !_isGridView.value
    }

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
        _files.value = sortFiles(_files.value, option)
    }

    fun deleteSelected(category: FileCategory) {
        viewModelScope.launch {
            val paths = _selectedPaths.value.toList()
            fileRepository.performOperation(FileOperation.Delete(paths))
            clearSelection()
            loadCategory(category)
        }
    }

    private fun sortFiles(files: List<FileItem>, option: SortOption): List<FileItem> {
        val comparator: Comparator<FileItem> = when (option.field) {
            SortField.NAME -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
            SortField.SIZE -> compareBy { it.size }
            SortField.DATE -> compareBy { it.lastModified }
            SortField.TYPE -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.extension }
        }
        return if (option.order == SortOrder.DESCENDING) {
            files.sortedWith(comparator.reversed())
        } else {
            files.sortedWith(comparator)
        }
    }
}
