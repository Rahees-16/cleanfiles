package com.rahees.cleanfiles.util

import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

object HashUtils {

    fun calculateMD5(file: File): String {
        val digest = MessageDigest.getInstance("MD5")
        FileInputStream(file).use { fis ->
            val buffer = ByteArray(8192)
            var bytesRead: Int
            while (fis.read(buffer).also { bytesRead = it } != -1) {
                digest.update(buffer, 0, bytesRead)
            }
        }
        val hashBytes = digest.digest()
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    fun findDuplicates(directory: File): Map<String, List<File>> {
        val sizeGroups = mutableMapOf<Long, MutableList<File>>()

        collectFiles(directory, sizeGroups)

        val duplicates = mutableMapOf<String, MutableList<File>>()

        sizeGroups.values
            .filter { it.size > 1 }
            .forEach { filesWithSameSize ->
                val hashGroups = mutableMapOf<String, MutableList<File>>()
                filesWithSameSize.forEach { file ->
                    try {
                        val hash = calculateMD5(file)
                        hashGroups.getOrPut(hash) { mutableListOf() }.add(file)
                    } catch (_: Exception) {
                        // Skip files that can't be read
                    }
                }
                hashGroups.values
                    .filter { it.size > 1 }
                    .forEach { group ->
                        val key = "${group[0].name} (${FileUtils.formatFileSize(group[0].length())})"
                        duplicates[key] = group
                    }
            }

        return duplicates
    }

    private fun collectFiles(directory: File, sizeGroups: MutableMap<Long, MutableList<File>>) {
        directory.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                if (!file.name.startsWith(".")) {
                    collectFiles(file, sizeGroups)
                }
            } else {
                if (file.length() > 0) {
                    sizeGroups.getOrPut(file.length()) { mutableListOf() }.add(file)
                }
            }
        }
    }
}
