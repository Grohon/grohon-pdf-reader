package com.grohon.pdfreader

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.grohon.pdfreader.adapters.RecentFilesAdapter
import com.grohon.pdfreader.databinding.ActivityMainBinding
import com.grohon.pdfreader.models.RecentFile
import com.grohon.pdfreader.utils.PreferencesManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recentFilesAdapter: RecentFilesAdapter
    private lateinit var preferencesManager: PreferencesManager

    private val storagePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openFilePicker()
        } else {
            Toast.makeText(this, R.string.error_permission_denied, Toast.LENGTH_SHORT).show()
        }
    }

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            openPdfFile(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesManager = PreferencesManager(this)

        setupViews()
        setupRecyclerView()
        loadRecentFiles()

        // Handle intent if app was opened with a PDF file
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleIntent(it) }
    }

    private fun setupViews() {
        binding.selectPdfButton.setOnClickListener {
            checkPermissionAndOpenPicker()
        }
    }

    private fun setupRecyclerView() {
        recentFilesAdapter = RecentFilesAdapter { recentFile ->
            // Handle recent file click
            try {
                val uri = Uri.parse(recentFile.uri)
                openPdfFile(uri)
            } catch (e: Exception) {
                Toast.makeText(this, R.string.error_opening_file, Toast.LENGTH_SHORT).show()
            }
        }

        binding.recentFilesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = recentFilesAdapter
        }
    }

    private fun checkPermissionAndOpenPicker() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                openFilePicker()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                // Show rationale and request permission
                Toast.makeText(
                    this,
                    "Storage permission is needed to access PDF files",
                    Toast.LENGTH_LONG
                ).show()
                storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            else -> {
                storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun openFilePicker() {
        try {
            filePickerLauncher.launch(arrayOf("application/pdf"))
        } catch (e: Exception) {
            Toast.makeText(this, R.string.error_opening_file, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openPdfFile(uri: Uri) {
        try {
            // Take persistable URI permission for future access
            contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            // Add to recent files
            addToRecentFiles(uri)

            // Start PDF viewer
            val intent = Intent(this, PDFViewerActivity::class.java).apply {
                data = uri
            }
            startActivity(intent)

        } catch (e: Exception) {
            Toast.makeText(this, R.string.error_opening_file, Toast.LENGTH_SHORT).show()
        }
    }

    private fun addToRecentFiles(uri: Uri) {
        try {
            val fileName = getFileNameFromUri(uri)
            val fileSize = getFileSizeFromUri(uri)
            val recentFile = RecentFile(
                uri = uri.toString(),
                fileName = fileName,
                filePath = uri.path ?: "",
                fileSize = fileSize,
                lastAccessed = System.currentTimeMillis()
            )

            preferencesManager.addRecentFile(recentFile)
            loadRecentFiles()
        } catch (e: Exception) {
            // Silently fail for recent files functionality
        }
    }

    private fun getFileNameFromUri(uri: Uri): String {
        return try {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME)
                    if (nameIndex >= 0) {
                        it.getString(nameIndex) ?: "Unknown"
                    } else {
                        "Unknown"
                    }
                } else {
                    "Unknown"
                }
            } ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
    }

    private fun getFileSizeFromUri(uri: Uri): Long {
        return try {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val sizeIndex = it.getColumnIndex(DocumentsContract.Document.COLUMN_SIZE)
                    if (sizeIndex >= 0) {
                        it.getLong(sizeIndex)
                    } else {
                        0L
                    }
                } else {
                    0L
                }
            } ?: 0L
        } catch (e: Exception) {
            0L
        }
    }

    private fun loadRecentFiles() {
        val recentFiles = preferencesManager.getRecentFiles()
        recentFilesAdapter.updateFiles(recentFiles)

        if (recentFiles.isEmpty()) {
            binding.recentFilesRecyclerView.visibility = android.view.View.GONE
            binding.noRecentFilesTextView.visibility = android.view.View.VISIBLE
        } else {
            binding.recentFilesRecyclerView.visibility = android.view.View.VISIBLE
            binding.noRecentFilesTextView.visibility = android.view.View.GONE
        }
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW && intent.data != null) {
            val uri = intent.data!!
            if (uri.toString().endsWith(".pdf", ignoreCase = true)) {
                openPdfFile(uri)
            }
        }
    }
}