package com.rahees.cleanfiles.ui.whatsapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class WhatsAppCategory(
    val name: String,
    val path: String,
    val fileCount: Int = 0,
    val totalSize: Long = 0L,
    val isSelected: Boolean = false
)

data class WhatsAppCleanerUiState(
    val categories: List<WhatsAppCategory> = emptyList(),
    val totalSize: Long = 0L,
    val isScanning: Boolean = false,
    val isDeleting: Boolean = false,
    val bytesFreed: Long = -1L
)

@HiltViewModel
class WhatsAppCleanerViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(WhatsAppCleanerUiState())
    val uiState: StateFlow<WhatsAppCleanerUiState> = _uiState.asStateFlow()

    private val mediaBasePaths = listOf(
        "/storage/emulated/0/Android/media/com.whatsapp/WhatsApp/Media",
        "/storage/emulated/0/WhatsApp/Media"
    )

    private val categoryDefs = listOf(
        "Images" to "WhatsApp Images",
        "Videos" to "WhatsApp Video",
        "Voice Notes" to "WhatsApp Voice Notes",
        "Documents" to "WhatsApp Documents",
        "Stickers" to "WhatsApp Stickers",
        "GIFs" to "WhatsApp Animated Gifs",
        "Status" to ".Statuses"
    )

    init {
        scan()
    }

    fun scan() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isScanning = true, bytesFreed = -1L) }

            val categories = mutableListOf<WhatsAppCategory>()

            for ((name, folder) in categoryDefs) {
                var totalFiles = 0
                var totalSize = 0L
                var resolvedPath = ""

                for (base in mediaBasePaths) {
                    val dir = File(base, folder)
                    if (dir.exists() && dir.isDirectory) {
                        resolvedPath = dir.absolutePath
                        val files = dir.walkTopDown().filter { it.isFile }.toList()
                        totalFiles += files.size
                        totalSize += files.sumOf { it.length() }
                    }
                }

                if (resolvedPath.isEmpty()) {
                    resolvedPath = "${mediaBasePaths[0]}/$folder"
                }

                categories.add(
                    WhatsAppCategory(
                        name = name,
                        path = resolvedPath,
                        fileCount = totalFiles,
                        totalSize = totalSize,
                        isSelected = false
                    )
                )
            }

            _uiState.update {
                it.copy(
                    categories = categories,
                    totalSize = categories.sumOf { cat -> cat.totalSize },
                    isScanning = false
                )
            }
        }
    }

    fun toggleCategory(index: Int) {
        _uiState.update { state ->
            val updated = state.categories.toMutableList()
            if (index in updated.indices) {
                updated[index] = updated[index].copy(isSelected = !updated[index].isSelected)
            }
            state.copy(categories = updated)
        }
    }

    fun toggleAll(selectAll: Boolean) {
        _uiState.update { state ->
            state.copy(
                categories = state.categories.map { it.copy(isSelected = selectAll) }
            )
        }
    }

    fun deleteSelected() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isDeleting = true) }

            var bytesFreed = 0L
            val selected = _uiState.value.categories.filter { it.isSelected }

            for (category in selected) {
                for (base in mediaBasePaths) {
                    val folderName = categoryDefs.find { it.first == category.name }?.second ?: continue
                    val dir = File(base, folderName)
                    if (dir.exists() && dir.isDirectory) {
                        dir.walkTopDown().filter { it.isFile }.forEach { file ->
                            val size = file.length()
                            if (file.delete()) {
                                bytesFreed += size
                            }
                        }
                    }
                }
            }

            _uiState.update { it.copy(isDeleting = false, bytesFreed = bytesFreed) }

            // Re-scan after deletion
            scan()
        }
    }
}
