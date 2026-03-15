package com.rahees.cleanfiles.data.model

data class StorageInfo(
    val totalSpace: Long,
    val usedSpace: Long,
    val freeSpace: Long,
    val categories: Map<FileCategory, Long> = emptyMap()
)

enum class FileCategory(val displayName: String) {
    IMAGES("Images"),
    VIDEOS("Videos"),
    AUDIO("Audio"),
    DOCUMENTS("Documents"),
    APKS("APKs"),
    DOWNLOADS("Downloads"),
    OTHER("Other")
}
