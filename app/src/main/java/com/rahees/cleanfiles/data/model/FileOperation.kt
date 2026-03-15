package com.rahees.cleanfiles.data.model

sealed class FileOperation {
    data class Copy(val sourcePaths: List<String>, val destPath: String) : FileOperation()
    data class Move(val sourcePaths: List<String>, val destPath: String) : FileOperation()
    data class Delete(val paths: List<String>) : FileOperation()
    data class Rename(val path: String, val newName: String) : FileOperation()
    data class CreateFolder(val parentPath: String, val folderName: String) : FileOperation()
    data class Zip(val paths: List<String>, val outputPath: String) : FileOperation()
}
