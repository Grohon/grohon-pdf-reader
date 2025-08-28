package com.grohon.pdfreader.models

data class RecentFile(
    val uri: String,
    val fileName: String,
    val filePath: String,
    val fileSize: Long,
    val lastAccessed: Long
) {
    fun getFormattedFileSize(): String {
        return when {
            fileSize < 1024 -> "${fileSize} B"
            fileSize < 1024 * 1024 -> "${fileSize / 1024} KB"
            fileSize < 1024 * 1024 * 1024 -> "${String.format("%.1f", fileSize / (1024.0 * 1024.0))} MB"
            else -> "${String.format("%.1f", fileSize / (1024.0 * 1024.0 * 1024.0))} GB"
        }
    }
}