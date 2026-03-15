package com.rahees.cleanfiles.ui.trash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahees.cleanfiles.data.local.TrashDao
import com.rahees.cleanfiles.data.local.TrashEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class TrashViewModel @Inject constructor(
    private val trashDao: TrashDao
) : ViewModel() {

    val trashedFiles: StateFlow<List<TrashEntity>> = trashDao.getAllTrashed()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            trashDao.deleteExpired()
        }
    }

    fun restoreFile(trashEntity: TrashEntity) {
        viewModelScope.launch {
            try {
                val trashFile = File(trashEntity.trashPath)
                val originalFile = File(trashEntity.originalPath)
                if (trashFile.exists()) {
                    originalFile.parentFile?.mkdirs()
                    trashFile.renameTo(originalFile)
                }
                trashDao.delete(trashEntity)
            } catch (_: Exception) {
                // Handle silently
            }
        }
    }

    fun deletePermanently(trashEntity: TrashEntity) {
        viewModelScope.launch {
            try {
                val trashFile = File(trashEntity.trashPath)
                if (trashFile.exists()) {
                    trashFile.delete()
                }
                trashDao.delete(trashEntity)
            } catch (_: Exception) {
                // Handle silently
            }
        }
    }

    fun emptyTrash() {
        viewModelScope.launch {
            val allTrashed = trashedFiles.value
            allTrashed.forEach { entity ->
                try {
                    val file = File(entity.trashPath)
                    if (file.exists()) file.delete()
                } catch (_: Exception) {
                    // Handle silently
                }
            }
            trashDao.deleteAll()
        }
    }
}
