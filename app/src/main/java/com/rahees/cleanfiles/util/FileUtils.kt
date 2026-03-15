package com.rahees.cleanfiles.util

import android.content.Context
import android.content.Intent
import android.webkit.MimeTypeMap
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.content.FileProvider
import com.rahees.cleanfiles.data.model.FileCategory
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

object FileUtils {

    fun getFileIcon(extension: String): ImageVector {
        return when (extension.lowercase(Locale.ROOT)) {
            "jpg", "jpeg", "png", "gif", "bmp", "webp", "svg", "ico" -> Icons.Filled.Image
            "mp4", "avi", "mkv", "mov", "wmv", "flv", "webm", "3gp" -> Icons.Filled.VideoFile
            "mp3", "wav", "flac", "aac", "ogg", "wma", "m4a", "opus" -> Icons.Filled.AudioFile
            "pdf" -> Icons.Filled.PictureAsPdf
            "doc", "docx", "odt", "rtf", "txt" -> Icons.Filled.Description
            "xls", "xlsx", "ods", "csv" -> Icons.Filled.TableChart
            "ppt", "pptx", "odp" -> Icons.Filled.Article
            "apk" -> Icons.Filled.Android
            "zip", "rar", "7z", "tar", "gz", "bz2" -> Icons.Filled.FolderZip
            "html", "css", "js", "json", "xml", "kt", "java", "py", "c", "cpp", "h" -> Icons.Filled.Code
            else -> Icons.Filled.InsertDriveFile
        }
    }

    fun formatFileSize(bytes: Long): String {
        if (bytes <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(bytes.toDouble()) / Math.log10(1024.0)).toInt()
            .coerceAtMost(units.size - 1)
        val size = bytes / Math.pow(1024.0, digitGroups.toDouble())
        return if (digitGroups == 0) {
            "${bytes.toInt()} B"
        } else {
            String.format(Locale.US, "%.1f %s", size, units[digitGroups])
        }
    }

    fun getMimeType(file: File): String? {
        val extension = file.extension.lowercase(Locale.ROOT)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }

    fun copyFile(source: File, dest: File): Boolean {
        return try {
            if (source.isDirectory) {
                dest.mkdirs()
                source.listFiles()?.forEach { child ->
                    copyFile(child, File(dest, child.name))
                }
                true
            } else {
                dest.parentFile?.mkdirs()
                source.inputStream().use { input ->
                    dest.outputStream().use { output ->
                        input.copyTo(output, bufferSize = 8192)
                    }
                }
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun moveFile(source: File, dest: File): Boolean {
        return try {
            if (source.renameTo(dest)) {
                true
            } else {
                if (copyFile(source, dest)) {
                    deleteFile(source)
                } else {
                    false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun deleteFile(file: File): Boolean {
        return try {
            if (file.isDirectory) {
                file.listFiles()?.forEach { child ->
                    deleteFile(child)
                }
            }
            file.delete()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun zipFiles(files: List<File>, outputPath: String): Boolean {
        return try {
            ZipOutputStream(BufferedOutputStream(FileOutputStream(outputPath))).use { zipOut ->
                files.forEach { file ->
                    addToZip(zipOut, file, "")
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun addToZip(zipOut: ZipOutputStream, file: File, parentPath: String) {
        val entryName = if (parentPath.isEmpty()) file.name else "$parentPath/${file.name}"
        if (file.isDirectory) {
            zipOut.putNextEntry(ZipEntry("$entryName/"))
            zipOut.closeEntry()
            file.listFiles()?.forEach { child ->
                addToZip(zipOut, child, entryName)
            }
        } else {
            zipOut.putNextEntry(ZipEntry(entryName))
            BufferedInputStream(FileInputStream(file)).use { input ->
                input.copyTo(zipOut, bufferSize = 8192)
            }
            zipOut.closeEntry()
        }
    }

    fun unzipFile(zipFile: File, destDir: String): Boolean {
        return try {
            val destDirectory = File(destDir)
            destDirectory.mkdirs()
            ZipInputStream(BufferedInputStream(FileInputStream(zipFile))).use { zipIn ->
                var entry: ZipEntry? = zipIn.nextEntry
                while (entry != null) {
                    val filePath = File(destDirectory, entry.name)
                    if (!filePath.canonicalPath.startsWith(destDirectory.canonicalPath)) {
                        throw SecurityException("Zip path traversal detected")
                    }
                    if (entry.isDirectory) {
                        filePath.mkdirs()
                    } else {
                        filePath.parentFile?.mkdirs()
                        BufferedOutputStream(FileOutputStream(filePath)).use { output ->
                            zipIn.copyTo(output, bufferSize = 8192)
                        }
                    }
                    zipIn.closeEntry()
                    entry = zipIn.nextEntry
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun shareFile(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val mimeType = getMimeType(file) ?: "*/*"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share ${file.name}"))
    }

    fun getFileCategory(file: File): FileCategory {
        val ext = file.extension.lowercase(Locale.ROOT)
        return when (ext) {
            "jpg", "jpeg", "png", "gif", "bmp", "webp", "svg", "ico", "heif", "heic" -> FileCategory.IMAGES
            "mp4", "avi", "mkv", "mov", "wmv", "flv", "webm", "3gp", "m4v" -> FileCategory.VIDEOS
            "mp3", "wav", "flac", "aac", "ogg", "wma", "m4a", "opus", "amr" -> FileCategory.AUDIO
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "rtf",
            "odt", "ods", "odp", "csv", "html", "xml", "json" -> FileCategory.DOCUMENTS
            "apk", "xapk" -> FileCategory.APKS
            else -> FileCategory.OTHER
        }
    }

    fun getDirectorySize(directory: File): Long {
        var size = 0L
        directory.listFiles()?.forEach { file ->
            size += if (file.isDirectory) {
                getDirectorySize(file)
            } else {
                file.length()
            }
        }
        return size
    }
}
