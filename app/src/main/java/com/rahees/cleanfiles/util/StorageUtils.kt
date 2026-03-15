package com.rahees.cleanfiles.util

import android.content.Context
import android.os.Environment
import android.os.StatFs
import android.provider.MediaStore
import com.rahees.cleanfiles.data.model.FileCategory
import com.rahees.cleanfiles.data.model.JunkItem
import com.rahees.cleanfiles.data.model.JunkType
import com.rahees.cleanfiles.data.model.StorageInfo
import java.io.File

object StorageUtils {

    fun getStorageInfo(): StorageInfo {
        val path = Environment.getExternalStorageDirectory()
        val statFs = StatFs(path.absolutePath)
        val totalSpace = statFs.totalBytes
        val freeSpace = statFs.availableBytes
        val usedSpace = totalSpace - freeSpace
        return StorageInfo(
            totalSpace = totalSpace,
            usedSpace = usedSpace,
            freeSpace = freeSpace
        )
    }

    fun getStorageInfoWithCategories(context: Context): StorageInfo {
        val baseInfo = getStorageInfo()
        val categories = mutableMapOf<FileCategory, Long>()

        categories[FileCategory.IMAGES] = getCategorySize(context, FileCategory.IMAGES)
        categories[FileCategory.VIDEOS] = getCategorySize(context, FileCategory.VIDEOS)
        categories[FileCategory.AUDIO] = getCategorySize(context, FileCategory.AUDIO)
        categories[FileCategory.DOCUMENTS] = getDocumentsSize()
        categories[FileCategory.APKS] = getApksSize()
        categories[FileCategory.DOWNLOADS] = getDownloadsSize()

        val accountedSize = categories.values.sum()
        categories[FileCategory.OTHER] = (baseInfo.usedSpace - accountedSize).coerceAtLeast(0)

        return baseInfo.copy(categories = categories)
    }

    fun getCategorySize(context: Context, category: FileCategory): Long {
        val uri = when (category) {
            FileCategory.IMAGES -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            FileCategory.VIDEOS -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            FileCategory.AUDIO -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            else -> return 0L
        }

        var totalSize = 0L
        val projection = arrayOf(MediaStore.MediaColumns.SIZE)

        try {
            context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)
                while (cursor.moveToNext()) {
                    totalSize += cursor.getLong(sizeColumn)
                }
            }
        } catch (_: Exception) {
            // Handle permission or query errors
        }

        return totalSize
    }

    private fun getDocumentsSize(): Long {
        val docsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        return if (docsDir.exists()) FileUtils.getDirectorySize(docsDir) else 0L
    }

    private fun getApksSize(): Long {
        val root = Environment.getExternalStorageDirectory()
        var size = 0L
        scanForExtension(root, "apk") { file ->
            size += file.length()
        }
        return size
    }

    private fun getDownloadsSize(): Long {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        return if (downloadsDir.exists()) FileUtils.getDirectorySize(downloadsDir) else 0L
    }

    private fun scanForExtension(dir: File, extension: String, action: (File) -> Unit) {
        dir.listFiles()?.forEach { file ->
            if (file.isDirectory && !file.name.startsWith(".")) {
                scanForExtension(file, extension, action)
            } else if (file.extension.equals(extension, ignoreCase = true)) {
                action(file)
            }
        }
    }

    fun scanJunkFiles(rootDirs: List<File>): List<JunkItem> {
        val junkItems = mutableListOf<JunkItem>()

        rootDirs.forEach { rootDir ->
            scanCacheFiles(rootDir, junkItems)
            scanTempFiles(rootDir, junkItems)
            scanOldApks(rootDir, junkItems)
            scanEmptyFolders(rootDir, junkItems)
            scanThumbnails(rootDir, junkItems)
        }

        return junkItems
    }

    private fun scanCacheFiles(dir: File, items: MutableList<JunkItem>) {
        dir.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                if (file.name.equals("cache", ignoreCase = true) ||
                    file.name.equals(".cache", ignoreCase = true)
                ) {
                    val size = FileUtils.getDirectorySize(file)
                    if (size > 0) {
                        items.add(
                            JunkItem(
                                path = file.absolutePath,
                                name = "${file.parentFile?.name ?: ""}/cache",
                                size = size,
                                type = JunkType.CACHE
                            )
                        )
                    }
                } else if (!file.name.startsWith(".")) {
                    scanCacheFiles(file, items)
                }
            }
        }
    }

    private fun scanTempFiles(dir: File, items: MutableList<JunkItem>) {
        dir.listFiles()?.forEach { file ->
            if (file.isDirectory && !file.name.startsWith(".")) {
                scanTempFiles(file, items)
            } else if (file.isFile) {
                val ext = file.extension.lowercase()
                if (ext == "tmp" || ext == "temp" || file.name.endsWith("~") ||
                    file.name.startsWith(".~") || ext == "bak" || ext == "log"
                ) {
                    items.add(
                        JunkItem(
                            path = file.absolutePath,
                            name = file.name,
                            size = file.length(),
                            type = JunkType.TEMP
                        )
                    )
                }
            }
        }
    }

    private fun scanOldApks(dir: File, items: MutableList<JunkItem>) {
        dir.listFiles()?.forEach { file ->
            if (file.isDirectory && !file.name.startsWith(".")) {
                scanOldApks(file, items)
            } else if (file.isFile && file.extension.equals("apk", ignoreCase = true)) {
                items.add(
                    JunkItem(
                        path = file.absolutePath,
                        name = file.name,
                        size = file.length(),
                        type = JunkType.APK
                    )
                )
            }
        }
    }

    private fun scanEmptyFolders(dir: File, items: MutableList<JunkItem>) {
        dir.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                val children = file.listFiles()
                if (children != null && children.isEmpty()) {
                    items.add(
                        JunkItem(
                            path = file.absolutePath,
                            name = file.name,
                            size = 0,
                            type = JunkType.EMPTY_FOLDER
                        )
                    )
                } else if (!file.name.startsWith(".")) {
                    scanEmptyFolders(file, items)
                }
            }
        }
    }

    private fun scanThumbnails(dir: File, items: MutableList<JunkItem>) {
        dir.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                if (file.name.equals(".thumbnails", ignoreCase = true) ||
                    file.name.equals("thumbnails", ignoreCase = true)
                ) {
                    val size = FileUtils.getDirectorySize(file)
                    if (size > 0) {
                        items.add(
                            JunkItem(
                                path = file.absolutePath,
                                name = file.name,
                                size = size,
                                type = JunkType.THUMBNAIL
                            )
                        )
                    }
                } else if (!file.name.startsWith(".")) {
                    scanThumbnails(file, items)
                }
            }
        }
    }
}
