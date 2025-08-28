package com.grohon.pdfreader

import com.grohon.pdfreader.models.RecentFile
import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for RecentFile model class.
 */
class RecentFileTest {

    @Test
    fun recentFile_creation_isCorrect() {
        // Given
        val uri = "content://com.android.providers.downloads.documents/document/123"
        val fileName = "test.pdf"
        val filePath = "/storage/emulated/0/Documents/test.pdf"
        val fileSize = 1024000L
        val lastAccessed = System.currentTimeMillis()

        // When
        val recentFile = RecentFile(uri, fileName, filePath, fileSize, lastAccessed)

        // Then
        assertEquals(uri, recentFile.uri)
        assertEquals(fileName, recentFile.fileName)
        assertEquals(filePath, recentFile.filePath)
        assertEquals(fileSize, recentFile.fileSize)
        assertEquals(lastAccessed, recentFile.lastAccessed)
    }

    @Test
    fun recentFile_equality_isCorrect() {
        // Given
        val uri = "content://com.android.providers.downloads.documents/document/123"
        val fileName = "test.pdf"
        val filePath = "/storage/emulated/0/Documents/test.pdf"
        val fileSize = 1024000L
        val lastAccessed = System.currentTimeMillis()

        val recentFile1 = RecentFile(uri, fileName, filePath, fileSize, lastAccessed)
        val recentFile2 = RecentFile(uri, fileName, filePath, fileSize, lastAccessed)

        // Then
        assertEquals(recentFile1, recentFile2)
    }

    @Test
    fun recentFile_hashCode_isConsistent() {
        // Given
        val uri = "content://com.android.providers.downloads.documents/document/123"
        val fileName = "test.pdf"
        val filePath = "/storage/emulated/0/Documents/test.pdf"
        val fileSize = 1024000L
        val lastAccessed = System.currentTimeMillis()

        val recentFile1 = RecentFile(uri, fileName, filePath, fileSize, lastAccessed)
        val recentFile2 = RecentFile(uri, fileName, filePath, fileSize, lastAccessed)

        // Then
        assertEquals(recentFile1.hashCode(), recentFile2.hashCode())
    }

    @Test
    fun recentFile_formattedFileSize_isCorrect() {
        // Given
        val uri = "content://com.android.providers.downloads.documents/document/123"
        val fileName = "test.pdf"
        val filePath = "/storage/emulated/0/Documents/test.pdf"

        // Test different file sizes
        val smallFile = RecentFile(uri, fileName, filePath, 512L, System.currentTimeMillis())
        val kbFile = RecentFile(uri, fileName, filePath, 2048L, System.currentTimeMillis())
        val mbFile = RecentFile(uri, fileName, filePath, 2097152L, System.currentTimeMillis())
        val gbFile = RecentFile(uri, fileName, filePath, 2147483648L, System.currentTimeMillis())

        // Then
        assertEquals("512 B", smallFile.getFormattedFileSize())
        assertEquals("2 KB", kbFile.getFormattedFileSize())
        assertEquals("2.0 MB", mbFile.getFormattedFileSize())
        assertEquals("2.0 GB", gbFile.getFormattedFileSize())
    }
}