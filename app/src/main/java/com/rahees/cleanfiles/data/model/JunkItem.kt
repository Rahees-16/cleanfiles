package com.rahees.cleanfiles.data.model

data class JunkItem(
    val path: String,
    val name: String,
    val size: Long,
    val type: JunkType,
    val isSelected: Boolean = true
)

enum class JunkType(val displayName: String) {
    CACHE("App Cache"),
    TEMP("Temp Files"),
    APK("Old APKs"),
    EMPTY_FOLDER("Empty Folders"),
    THUMBNAIL("Thumbnails")
}
