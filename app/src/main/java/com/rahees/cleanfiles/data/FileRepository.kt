package com.rahees.cleanfiles.data

import android.content.ContentResolver
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import com.rahees.cleanfiles.data.local.RecentDao
import com.rahees.cleanfiles.data.local.RecentFileEntity
import com.rahees.cleanfiles.data.model.FileCategory
import com.rahees.cleanfiles.data.model.FileItem
import com.rahees.cleanfiles.data.model.FileOperation
import com.rahees.cleanfiles.util.FileUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val recentDao: RecentDao
) {

    suspend fun listFiles(path: String, showHidden: Boolean = false): List<FileItem> =
        withContext(Dispatchers.IO) {
            val directory = File(path)
            if (!directory.exists() || !directory.isDirectory) return@withContext emptyList()

            directory.listFiles()
                ?.filter { showHidden || !it.name.startsWith(".") }
                ?.map { file ->
                    FileItem(
                        name = file.name,
                        path = file.absolutePath,
                        size = if (file.isDirectory) FileUtils.getDirectorySize(file) else file.length(),
                        lastModified = file.lastModified(),
                        isDirectory = file.isDirectory,
                        mimeType = if (file.isFile) FileUtils.getMimeType(file) else null,
                        extension = if (file.isFile) file.extension.lowercase(Locale.ROOT) else "",
                        childCount = if (file.isDirectory) (file.listFiles()?.size ?: 0) else 0
                    )
                }
                ?: emptyList()
        }

    suspend fun searchFiles(
        rootPath: String,
        query: String,
        categoryFilter: FileCategory? = null,
        minSize: Long? = null,
        maxSize: Long? = null
    ): List<FileItem> = withContext(Dispatchers.IO) {
        val results = mutableListOf<FileItem>()
        searchRecursive(File(rootPath), query.lowercase(Locale.ROOT), categoryFilter, minSize, maxSize, results, 0)
        results
    }

    private fun searchRecursive(
        dir: File,
        query: String,
        categoryFilter: FileCategory?,
        minSize: Long?,
        maxSize: Long?,
        results: MutableList<FileItem>,
        depth: Int
    ) {
        if (depth > 10 || results.size > 500) return

        dir.listFiles()?.forEach { file ->
            if (file.name.startsWith(".")) return@forEach

            val matchesName = file.name.lowercase(Locale.ROOT).contains(query)
            val matchesCategory = categoryFilter == null ||
                    (file.isFile && FileUtils.getFileCategory(file) == categoryFilter)
            val matchesSize = (minSize == null || file.length() >= minSize) &&
                    (maxSize == null || file.length() <= maxSize)

            if (matchesName && matchesCategory && matchesSize) {
                results.add(
                    FileItem(
                        name = file.name,
                        path = file.absolutePath,
                        size = if (file.isDirectory) 0 else file.length(),
                        lastModified = file.lastModified(),
                        isDirectory = file.isDirectory,
                        mimeType = if (file.isFile) FileUtils.getMimeType(file) else null,
                        extension = if (file.isFile) file.extension.lowercase(Locale.ROOT) else "",
                        childCount = if (file.isDirectory) (file.listFiles()?.size ?: 0) else 0
                    )
                )
            }

            if (file.isDirectory) {
                searchRecursive(file, query, categoryFilter, minSize, maxSize, results, depth + 1)
            }
        }
    }

    suspend fun getFilesByCategory(category: FileCategory): List<FileItem> =
        withContext(Dispatchers.IO) {
            val contentResolver = context.contentResolver
            when (category) {
                FileCategory.IMAGES -> queryMediaStore(contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                FileCategory.VIDEOS -> queryMediaStore(contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                FileCategory.AUDIO -> queryMediaStore(contentResolver, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
                FileCategory.DOCUMENTS -> getDocumentFiles()
                FileCategory.APKS -> getFilesByExtension("apk")
                FileCategory.DOWNLOADS -> getDownloadFiles()
                FileCategory.OTHER -> emptyList()
            }
        }

    private fun queryMediaStore(contentResolver: ContentResolver, uri: android.net.Uri): List<FileItem> {
        val items = mutableListOf<FileItem>()
        val projection = arrayOf(
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.SIZE,
            MediaStore.MediaColumns.DATE_MODIFIED,
            MediaStore.MediaColumns.MIME_TYPE
        )

        try {
            contentResolver.query(uri, projection, null, null,
                "${MediaStore.MediaColumns.DATE_MODIFIED} DESC"
            )?.use { cursor ->
                val nameCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                val dataCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)
                val dateCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED)
                val mimeCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)

                while (cursor.moveToNext()) {
                    val name = cursor.getString(nameCol) ?: continue
                    val path = cursor.getString(dataCol) ?: continue
                    val size = cursor.getLong(sizeCol)
                    val date = cursor.getLong(dateCol) * 1000
                    val mime = cursor.getString(mimeCol)
                    val ext = name.substringAfterLast('.', "").lowercase(Locale.ROOT)

                    items.add(
                        FileItem(
                            name = name,
                            path = path,
                            size = size,
                            lastModified = date,
                            isDirectory = false,
                            mimeType = mime,
                            extension = ext
                        )
                    )
                }
            }
        } catch (_: Exception) {
            // Handle query errors
        }

        return items
    }

    private fun getDocumentFiles(): List<FileItem> {
        val docExtensions = setOf("pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",
            "txt", "rtf", "odt", "ods", "odp", "csv")
        val results = mutableListOf<FileItem>()
        val root = Environment.getExternalStorageDirectory()
        collectFilesByExtension(root, docExtensions, results, 0)
        return results
    }

    private fun getDownloadFiles(): List<FileItem> {
        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadDir.exists()) return emptyList()
        return downloadDir.listFiles()
            ?.filter { it.isFile }
            ?.map { file ->
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
            ?: emptyList()
    }

    private fun getFilesByExtension(ext: String): List<FileItem> {
        val results = mutableListOf<FileItem>()
        val root = Environment.getExternalStorageDirectory()
        collectFilesByExtension(root, setOf(ext), results, 0)
        return results
    }

    private fun collectFilesByExtension(
        dir: File,
        extensions: Set<String>,
        results: MutableList<FileItem>,
        depth: Int
    ) {
        if (depth > 8 || results.size > 1000) return
        dir.listFiles()?.forEach { file ->
            if (file.name.startsWith(".")) return@forEach
            if (file.isDirectory) {
                collectFilesByExtension(file, extensions, results, depth + 1)
            } else if (file.extension.lowercase(Locale.ROOT) in extensions) {
                results.add(
                    FileItem(
                        name = file.name,
                        path = file.absolutePath,
                        size = file.length(),
                        lastModified = file.lastModified(),
                        isDirectory = false,
                        mimeType = FileUtils.getMimeType(file),
                        extension = file.extension.lowercase(Locale.ROOT)
                    )
                )
            }
        }
    }

    suspend fun performOperation(operation: FileOperation): Boolean = withContext(Dispatchers.IO) {
        when (operation) {
            is FileOperation.Copy -> {
                operation.sourcePaths.all { sourcePath ->
                    val source = File(sourcePath)
                    val dest = File(operation.destPath, source.name)
                    FileUtils.copyFile(source, dest)
                }
            }
            is FileOperation.Move -> {
                operation.sourcePaths.all { sourcePath ->
                    val source = File(sourcePath)
                    val dest = File(operation.destPath, source.name)
                    FileUtils.moveFile(source, dest)
                }
            }
            is FileOperation.Delete -> {
                operation.paths.all { path ->
                    FileUtils.deleteFile(File(path))
                }
            }
            is FileOperation.Rename -> {
                val source = File(operation.path)
                val dest = File(source.parentFile, operation.newName)
                source.renameTo(dest)
            }
            is FileOperation.CreateFolder -> {
                val newFolder = File(operation.parentPath, operation.folderName)
                newFolder.mkdirs()
            }
            is FileOperation.Zip -> {
                val files = operation.paths.map { File(it) }
                FileUtils.zipFiles(files, operation.outputPath)
            }
        }
    }

    suspend fun addToRecent(path: String, name: String) {
        recentDao.deleteByPath(path)
        recentDao.insertRecent(RecentFileEntity(path = path, name = name))
        recentDao.trimToSize(50)
    }
}
