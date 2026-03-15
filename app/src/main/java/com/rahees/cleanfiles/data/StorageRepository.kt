package com.rahees.cleanfiles.data

import android.content.Context
import android.os.Environment
import com.rahees.cleanfiles.data.model.FileItem
import com.rahees.cleanfiles.data.model.JunkItem
import com.rahees.cleanfiles.data.model.StorageInfo
import com.rahees.cleanfiles.util.FileUtils
import com.rahees.cleanfiles.util.HashUtils
import com.rahees.cleanfiles.util.StorageUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getStorageInfo(): Flow<StorageInfo> = flow {
        val info = StorageUtils.getStorageInfoWithCategories(context)
        emit(info)
    }.flowOn(Dispatchers.IO)

    fun scanForJunk(): Flow<List<JunkItem>> = flow {
        val rootDirs = mutableListOf<File>()
        val externalStorage = Environment.getExternalStorageDirectory()
        if (externalStorage.exists()) {
            rootDirs.add(externalStorage)
        }
        val cacheDirs = listOfNotNull(
            context.cacheDir,
            context.externalCacheDir
        )
        rootDirs.addAll(cacheDirs)

        val junkItems = StorageUtils.scanJunkFiles(rootDirs)
        emit(junkItems)
    }.flowOn(Dispatchers.IO)

    suspend fun cleanJunk(items: List<JunkItem>): Long {
        var bytesFreed = 0L
        items.forEach { item ->
            val file = File(item.path)
            if (file.exists()) {
                val size = if (file.isDirectory) FileUtils.getDirectorySize(file) else file.length()
                if (FileUtils.deleteFile(file)) {
                    bytesFreed += size
                }
            }
        }
        return bytesFreed
    }

    fun findDuplicates(): Flow<Map<String, List<FileItem>>> = flow {
        val root = Environment.getExternalStorageDirectory()
        val duplicateFiles = HashUtils.findDuplicates(root)

        val result = duplicateFiles.mapValues { (_, files) ->
            files.map { file ->
                FileItem(
                    name = file.name,
                    path = file.absolutePath,
                    size = file.length(),
                    lastModified = file.lastModified(),
                    isDirectory = false,
                    mimeType = FileUtils.getMimeType(file),
                    extension = file.extension.lowercase(Locale.ROOT)
                )
            }
        }

        emit(result)
    }.flowOn(Dispatchers.IO)
}
