package com.rahees.cleanfiles.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahees.cleanfiles.data.FileRepository
import com.rahees.cleanfiles.data.StorageRepository
import com.rahees.cleanfiles.data.local.RecentDao
import com.rahees.cleanfiles.data.local.RecentFileEntity
import com.rahees.cleanfiles.data.model.FileCategory
import com.rahees.cleanfiles.data.model.StorageInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryInfo(
    val category: FileCategory,
    val fileCount: Int,
    val totalSize: Long
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val storageRepository: StorageRepository,
    private val fileRepository: FileRepository,
    private val recentDao: RecentDao
) : ViewModel() {

    private val _storageInfo = MutableStateFlow(StorageInfo(0, 0, 0))
    val storageInfo: StateFlow<StorageInfo> = _storageInfo.asStateFlow()

    private val _categories = MutableStateFlow<List<CategoryInfo>>(emptyList())
    val categories: StateFlow<List<CategoryInfo>> = _categories.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val recentFiles: StateFlow<List<RecentFileEntity>> = recentDao.getRecentFiles(10)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true

            storageRepository.getStorageInfo().collect { info ->
                _storageInfo.value = info
            }
        }

        viewModelScope.launch {
            val categoryList = FileCategory.entries.filter { it != FileCategory.OTHER }.map { category ->
                val files = fileRepository.getFilesByCategory(category)
                CategoryInfo(
                    category = category,
                    fileCount = files.size,
                    totalSize = files.sumOf { it.size }
                )
            }
            _categories.value = categoryList
            _isLoading.value = false
        }
    }
}
