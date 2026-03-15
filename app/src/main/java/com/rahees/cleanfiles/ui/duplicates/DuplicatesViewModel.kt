package com.rahees.cleanfiles.ui.duplicates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahees.cleanfiles.data.StorageRepository
import com.rahees.cleanfiles.data.model.FileItem
import com.rahees.cleanfiles.data.model.FileOperation
import com.rahees.cleanfiles.data.FileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class DuplicateState {
    IDLE, SCANNING, RESULTS
}

@HiltViewModel
class DuplicatesViewModel @Inject constructor(
    private val storageRepository: StorageRepository,
    private val fileRepository: FileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DuplicateState.IDLE)
    val state: StateFlow<DuplicateState> = _state.asStateFlow()

    private val _duplicates = MutableStateFlow<Map<String, List<FileItem>>>(emptyMap())
    val duplicates: StateFlow<Map<String, List<FileItem>>> = _duplicates.asStateFlow()

    private val _selectedPaths = MutableStateFlow<Set<String>>(emptySet())
    val selectedPaths: StateFlow<Set<String>> = _selectedPaths.asStateFlow()

    fun startScan() {
        viewModelScope.launch {
            _state.value = DuplicateState.SCANNING
            _selectedPaths.value = emptySet()
            storageRepository.findDuplicates().collect { result ->
                _duplicates.value = result
                _state.value = DuplicateState.RESULTS
            }
        }
    }

    fun toggleSelection(path: String) {
        val current = _selectedPaths.value.toMutableSet()
        if (current.contains(path)) current.remove(path) else current.add(path)
        _selectedPaths.value = current
    }

    fun keepNewest(groupKey: String) {
        val group = _duplicates.value[groupKey] ?: return
        val newest = group.maxByOrNull { it.lastModified } ?: return
        _selectedPaths.value = _selectedPaths.value.toMutableSet().apply {
            group.forEach { item ->
                if (item.path != newest.path) add(item.path)
                else remove(item.path)
            }
        }
    }

    fun keepOldest(groupKey: String) {
        val group = _duplicates.value[groupKey] ?: return
        val oldest = group.minByOrNull { it.lastModified } ?: return
        _selectedPaths.value = _selectedPaths.value.toMutableSet().apply {
            group.forEach { item ->
                if (item.path != oldest.path) add(item.path)
                else remove(item.path)
            }
        }
    }

    fun getRecoverableSpace(): Long {
        return _selectedPaths.value.sumOf { path ->
            _duplicates.value.values.flatten().find { it.path == path }?.size ?: 0L
        }
    }

    fun deleteSelected() {
        viewModelScope.launch {
            val paths = _selectedPaths.value.toList()
            fileRepository.performOperation(FileOperation.Delete(paths))
            _selectedPaths.value = emptySet()
            startScan()
        }
    }
}
