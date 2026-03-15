package com.rahees.cleanfiles.data.model

data class FileItem(
    val name: String,
    val path: String,
    val size: Long,
    val lastModified: Long,
    val isDirectory: Boolean,
    val mimeType: String? = null,
    val extension: String = "",
    val childCount: Int = 0
)
