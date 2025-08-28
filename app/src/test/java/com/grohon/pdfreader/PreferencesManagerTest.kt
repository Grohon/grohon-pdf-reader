package com.grohon.pdfreader

import android.content.Context
import android.content.SharedPreferences
import com.grohon.pdfreader.models.RecentFile
import com.grohon.pdfreader.utils.PreferencesManager
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Unit tests for PreferencesManager class.
 * Note: This test uses mocking since it involves Android Context and SharedPreferences.
 */
class PreferencesManagerTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var preferencesManager: PreferencesManager

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        // Mock the SharedPreferences chain
        `when`(context.getSharedPreferences("grohon_pdf_reader_prefs", Context.MODE_PRIVATE))
            .thenReturn(sharedPreferences)
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(any(), any())).thenReturn(editor)
        `when`(editor.remove(any())).thenReturn(editor)

        preferencesManager = PreferencesManager(context)
    }

    @Test
    fun getRecentFiles_whenEmpty_returnsEmptyList() {
        // Given
        `when`(sharedPreferences.getString("recent_files", null)).thenReturn(null)

        // When
        val result = preferencesManager.getRecentFiles()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun getRecentFiles_withValidJson_returnsCorrectList() {
        // Given
        val jsonString = """[
            {
                "uri": "content://test1",
                "fileName": "test1.pdf",
                "filePath": "/path/test1.pdf",
                "fileSize": 1024,
                "lastAccessed": 1234567890
            },
            {
                "uri": "content://test2",
                "fileName": "test2.pdf",
                "filePath": "/path/test2.pdf",
                "fileSize": 2048,
                "lastAccessed": 1234567891
            }
        ]"""

        `when`(sharedPreferences.getString("recent_files", null)).thenReturn(jsonString)

        // When
        val result = preferencesManager.getRecentFiles()

        // Then
        assertEquals(2, result.size)
        assertEquals("content://test1", result[0].uri)
        assertEquals("test1.pdf", result[0].fileName)
        assertEquals("/path/test1.pdf", result[0].filePath)
        assertEquals(1024L, result[0].fileSize)
        assertEquals(1234567890L, result[0].lastAccessed)
    }

    @Test
    fun getRecentFiles_withInvalidJson_returnsEmptyList() {
        // Given
        `when`(sharedPreferences.getString("recent_files", null)).thenReturn("invalid json")

        // When
        val result = preferencesManager.getRecentFiles()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun clearRecentFiles_callsCorrectPreferencesMethod() {
        // When
        preferencesManager.clearRecentFiles()

        // Then
        verify(sharedPreferences).edit()
        verify(editor).remove("recent_files")
        verify(editor).apply()
    }

    // Helper method to create test RecentFile
    private fun createTestRecentFile(
        uri: String = "content://test",
        fileName: String = "test.pdf",
        filePath: String = "/path/test.pdf",
        fileSize: Long = 1024L,
        lastAccessed: Long = System.currentTimeMillis()
    ): RecentFile {
        return RecentFile(uri, fileName, filePath, fileSize, lastAccessed)
    }
}