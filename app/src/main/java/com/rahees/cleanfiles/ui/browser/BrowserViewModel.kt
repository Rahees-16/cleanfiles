package com.rahees.cleanfiles.ui.browser

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahees.cleanfiles.data.FileRepository
import com.rahees.cleanfiles.data.PreferencesManager
import com.rahees.cleanfiles.data.model.FileItem
import com.rahees.cleanfiles.data.model.FileOperation
import com.rahees.cleanfiles.data.model.SortField
import com.rahees.cleanfiles.data.model.SortOption
import com.rahees.cleanfiles.data.model.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClipboardData(
    val paths: List<String>,
    val isCut: Boolean
)

@HiltViewModel
class BrowserViewModel @Inject constructor(
    private val fileRepository: FileRepository,
    preferencesManager: PreferencesManager
) : ViewModel() {

    private val _currentPath = MutableStateFlow(
        Environment.getExternalStorageDirectory().absolutePath
    )
    val currentPath: StateFlow<String> = _currentPath.asStateFlow()

    private val _files = MutableStateFlow<List<FileItem>>(emptyList())
    val files: StateFlow<List<FileItem>> = _files.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedPaths = MutableStateFlow<Set<String>>(emptySet())
    val selectedPaths: StateFlow<Set<String>> = _selectedPaths.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption())
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    private val _clipboard = MutableStateFlow<ClipboardData?>(null)
    val clipboard: StateFlow<ClipboardData?> = _clipboard.asStateFlow()

    val isGridView: StateFlow<Boolean> = preferencesManager.isGridView
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val showHiddenFiles: StateFlow<Boolean> = preferencesManager.showHiddenFiles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun navigateTo(path: String) {
        _currentPath.value = path
        _selectedPaths.value = emptySet()
        loadFiles()
    }

    fun loadFiles() {
        viewModelScope.launch {
            _isLoading.value = true
            val fileList = fileRepository.listFiles(_currentPath.value, showHiddenFiles.value)
            _files.value = sortFiles(fileList, _sortOption.value)
            _isLoading.value = false
        }
    }

    fun navigateUp(): Boolean {
        val rootPath = Environment.getExternalStorageDirectory().absolutePath
        val currentDir = java.io.File(_currentPath.value)
        val parent = currentDir.parentFile
        return if (parent != null && _currentPath.value != rootPath && _currentPath.value != "/") {
            navigateTo(parent.absolutePath)
            true
        } else {
            false
        }
    }

    fun toggleSelection(path: String) {
        val current = _selectedPaths.value.toMutableSet()
        if (current.contains(path)) {
            current.remove(path)
        } else {
            current.add(path)
        }
        _selectedPaths.value = current
    }

    fun selectAll() {
        _selectedPaths.value = _files.value.map { it.path }.toSet()
    }

    fun clearSelection() {
        _selectedPaths.value = emptySet()
    }

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
        _files.value = sortFiles(_files.value, option)
    }

    fun copyToClipboard(paths: List<String>) {
        _clipboard.value = ClipboardData(paths, isCut = false)
        clearSelection()
    }

    fun cutToClipboard(paths: List<String>) {
        _clipboard.value = ClipboardData(paths, isCut = true)
        clearSelection()
    }

    fun paste() {
        val clipData = _clipboard.value ?: return
        viewModelScope.launch {
            val operation = if (clipData.isCut) {
                FileOperation.Move(clipData.paths, _currentPath.value)
            } else {
                FileOperation.Copy(clipData.paths, _currentPath.value)
            }
            fileRepository.performOperation(operation)
            if (clipData.isCut) {
                _clipboard.value = null
            }
            loadFiles()
        }
    }

    fun deleteSelected() {
        viewModelScope.launch {
            val paths = _selectedPaths.value.toList()
            fileRepository.performOperation(FileOperation.Delete(paths))
            clearSelection()
            loadFiles()
        }
    }

    fun createFolder(name: String) {
        viewModelScope.launch {
            fileRepository.performOperation(
                FileOperation.CreateFolder(_currentPath.value, name)
            )
            loadFiles()
        }
    }

    fun renameFile(path: String, newName: String) {
        viewModelScope.launch {
            fileRepository.performOperation(FileOperation.Rename(path, newName))
            loadFiles()
        }
    }

    fun zipSelected() {
        viewModelScope.launch {
            val paths = _selectedPaths.value.toList()
            val outputPath = "${_currentPath.value}/archive_${System.currentTimeMillis()}.zip"
            fileRepository.performOperation(FileOperation.Zip(paths, outputPath))
            clearSelection()
            loadFiles()
        }
    }

    fun onFileOpened(path: String, name: String) {
        viewModelScope.launch {
            fileRepository.addToRecent(path, name)
        }
    }

    private fun sortFiles(files: List<FileItem>, option: SortOption): List<FileItem> {
        val directories = files.filter { it.isDirectory }
        val regularFiles = files.filter { !it.isDirectory }

        val sortedDirs = sortList(directories, option)
        val sortedFiles = sortList(regularFiles, option)

        return sortedDirs + sortedFiles
    }

    private fun sortList(files: List<FileItem>, option: SortOption): List<FileItem> {
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
