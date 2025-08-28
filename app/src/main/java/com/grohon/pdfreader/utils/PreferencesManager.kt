package com.grohon.pdfreader.utils

import android.content.Context
import android.content.SharedPreferences
import com.grohon.pdfreader.models.RecentFile
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class PreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )

    companion object {
        private const val PREFS_NAME = "grohon_pdf_reader_prefs"
        private const val KEY_RECENT_FILES = "recent_files"
        private const val MAX_RECENT_FILES = 10
    }

    fun addRecentFile(recentFile: RecentFile) {
        val recentFiles = getRecentFiles().toMutableList()

        // Remove if already exists (to update position)
        recentFiles.removeAll { it.uri == recentFile.uri }

        // Add to beginning
        recentFiles.add(0, recentFile)

        // Keep only max number of files
        if (recentFiles.size > MAX_RECENT_FILES) {
            recentFiles.subList(MAX_RECENT_FILES, recentFiles.size).clear()
        }

        saveRecentFiles(recentFiles)
    }

    fun getRecentFiles(): List<RecentFile> {
        val jsonString = sharedPreferences.getString(KEY_RECENT_FILES, null)
            ?: return emptyList()

        return try {
            val jsonArray = JSONArray(jsonString)
            val recentFiles = mutableListOf<RecentFile>()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val recentFile = RecentFile(
                    uri = jsonObject.getString("uri"),
                    fileName = jsonObject.getString("fileName"),
                    filePath = jsonObject.getString("filePath"),
                    fileSize = jsonObject.getLong("fileSize"),
                    lastAccessed = jsonObject.getLong("lastAccessed")
                )
                recentFiles.add(recentFile)
            }

            recentFiles
        } catch (e: JSONException) {
            emptyList()
        }
    }

    fun removeRecentFile(uri: String) {
        val recentFiles = getRecentFiles().toMutableList()
        recentFiles.removeAll { it.uri == uri }
        saveRecentFiles(recentFiles)
    }

    fun clearRecentFiles() {
        sharedPreferences.edit().remove(KEY_RECENT_FILES).apply()
    }

    private fun saveRecentFiles(recentFiles: List<RecentFile>) {
        try {
            val jsonArray = JSONArray()

            for (recentFile in recentFiles) {
                val jsonObject = JSONObject().apply {
                    put("uri", recentFile.uri)
                    put("fileName", recentFile.fileName)
                    put("filePath", recentFile.filePath)
                    put("fileSize", recentFile.fileSize)
                    put("lastAccessed", recentFile.lastAccessed)
                }
                jsonArray.put(jsonObject)
            }

            sharedPreferences.edit()
                .putString(KEY_RECENT_FILES, jsonArray.toString())
                .apply()

        } catch (e: JSONException) {
            // Silently fail
        }
    }
}