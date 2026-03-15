package com.rahees.cleanfiles.ui.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahees.cleanfiles.data.StorageRepository
import com.rahees.cleanfiles.data.model.StorageInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val storageRepository: StorageRepository
) : ViewModel() {

    private val _storageInfo = MutableStateFlow(StorageInfo(0, 0, 0))
    val storageInfo: StateFlow<StorageInfo> = _storageInfo.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadAnalysis()
    }

    private fun loadAnalysis() {
        viewModelScope.launch {
            _isLoading.value = true
            storageRepository.getStorageInfo().collect { info ->
                _storageInfo.value = info
                _isLoading.value = false
            }
        }
    }
}
