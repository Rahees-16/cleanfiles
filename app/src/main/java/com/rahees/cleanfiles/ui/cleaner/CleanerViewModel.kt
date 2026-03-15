package com.rahees.cleanfiles.ui.cleaner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahees.cleanfiles.data.StorageRepository
import com.rahees.cleanfiles.data.model.JunkItem
import com.rahees.cleanfiles.data.model.JunkType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class CleanerState {
    IDLE, SCANNING, RESULTS, CLEANING, DONE
}

@HiltViewModel
class CleanerViewModel @Inject constructor(
    private val storageRepository: StorageRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CleanerState.IDLE)
    val state: StateFlow<CleanerState> = _state.asStateFlow()

    private val _junkItems = MutableStateFlow<List<JunkItem>>(emptyList())
    val junkItems: StateFlow<List<JunkItem>> = _junkItems.asStateFlow()

    private val _bytesFreed = MutableStateFlow(0L)
    val bytesFreed: StateFlow<Long> = _bytesFreed.asStateFlow()

    fun startScan() {
        viewModelScope.launch {
            _state.value = CleanerState.SCANNING
            storageRepository.scanForJunk().collect { items ->
                _junkItems.value = items
                _state.value = CleanerState.RESULTS
            }
        }
    }

    fun toggleItem(path: String) {
        _junkItems.value = _junkItems.value.map { item ->
            if (item.path == path) item.copy(isSelected = !item.isSelected) else item
        }
    }

    fun toggleGroup(type: JunkType) {
        val groupItems = _junkItems.value.filter { it.type == type }
        val allSelected = groupItems.all { it.isSelected }
        _junkItems.value = _junkItems.value.map { item ->
            if (item.type == type) item.copy(isSelected = !allSelected) else item
        }
    }

    fun getSelectedSize(): Long {
        return _junkItems.value.filter { it.isSelected }.sumOf { it.size }
    }

    fun clean() {
        viewModelScope.launch {
            _state.value = CleanerState.CLEANING
            val selectedItems = _junkItems.value.filter { it.isSelected }
            val freed = storageRepository.cleanJunk(selectedItems)
            _bytesFreed.value = freed
            _state.value = CleanerState.DONE
        }
    }

    fun reset() {
        _state.value = CleanerState.IDLE
        _junkItems.value = emptyList()
        _bytesFreed.value = 0L
    }
}
