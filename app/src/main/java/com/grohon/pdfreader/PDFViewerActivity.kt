package com.grohon.pdfreader

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.grohon.pdfreader.adapters.PDFPagesAdapter
import com.grohon.pdfreader.databinding.ActivityPdfViewerBinding
import com.grohon.pdfreader.models.PDFPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class PDFViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfViewerBinding
    private lateinit var pdfPagesAdapter: PDFPagesAdapter
    private var pdfRenderer: PdfRenderer? = null
    private var parcelFileDescriptor: ParcelFileDescriptor? = null
    private var currentPage = 0
    private var totalPages = 0
    private var zoomLevel = 1.0f
    private val maxZoomLevel = 3.0f
    private val minZoomLevel = 0.5f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupViews()
        setupRecyclerView()

        intent.data?.let { uri ->
            loadPDF(uri)
        } ?: run {
            Toast.makeText(this, R.string.error_file_not_found, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        closePdfRenderer()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.pdf_viewer_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_share -> {
                sharePDF()
                true
            }
            R.id.action_file_info -> {
                showFileInfo()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setupViews() {
        binding.apply {
            previousPageButton.setOnClickListener {
                goToPreviousPage()
            }

            nextPageButton.setOnClickListener {
                goToNextPage()
            }

            zoomFab.setOnClickListener {
                toggleZoom()
            }
        }
    }

    private fun setupRecyclerView() {
        pdfPagesAdapter = PDFPagesAdapter { pageNumber ->
            currentPage = pageNumber
            updatePageInfo()
        }

        // Set lifecycle scope for the adapter
        pdfPagesAdapter.setLifecycleScope(lifecycleScope)

        binding.pdfPagesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@PDFViewerActivity)
            adapter = pdfPagesAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    updateCurrentPageFromScroll()
                }
            })
        }
    }

    private fun loadPDF(uri: Uri) {
        binding.loadingProgressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    // Open the PDF file
                    parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r")
                    parcelFileDescriptor?.let { pfd ->
                        pdfRenderer = PdfRenderer(pfd)
                        totalPages = pdfRenderer!!.pageCount
                    }
                }

                // Update UI on main thread
                withContext(Dispatchers.Main) {
                    binding.loadingProgressBar.visibility = View.GONE
                    if (pdfRenderer != null) {
                        loadPages()
                        updatePageInfo()
                    } else {
                        Toast.makeText(
                            this@PDFViewerActivity,
                            R.string.error_invalid_pdf,
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }

            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    binding.loadingProgressBar.visibility = View.GONE
                    Toast.makeText(
                        this@PDFViewerActivity,
                        R.string.error_opening_file,
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            } catch (e: SecurityException) {
                withContext(Dispatchers.Main) {
                    binding.loadingProgressBar.visibility = View.GONE
                    Toast.makeText(
                        this@PDFViewerActivity,
                        R.string.error_permission_denied,
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }

    private fun loadPages() {
        pdfRenderer?.let { renderer ->
            val pages = mutableListOf<PDFPage>()

            for (i in 0 until renderer.pageCount) {
                pages.add(PDFPage(i))
            }

            pdfPagesAdapter.updatePages(pages, renderer, zoomLevel)
        }
    }

    private fun goToPreviousPage() {
        if (currentPage > 0) {
            currentPage--
            scrollToPage(currentPage)
            updatePageInfo()
        }
    }

    private fun goToNextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++
            scrollToPage(currentPage)
            updatePageInfo()
        }
    }

    private fun scrollToPage(pageNumber: Int) {
        val layoutManager = binding.pdfPagesRecyclerView.layoutManager as LinearLayoutManager
        val smoothScroller = object : LinearSmoothScroller(this) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        smoothScroller.targetPosition = pageNumber
        layoutManager.startSmoothScroll(smoothScroller)
    }

    private fun updateCurrentPageFromScroll() {
        val layoutManager = binding.pdfPagesRecyclerView.layoutManager as LinearLayoutManager
        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
        if (firstVisibleItem != RecyclerView.NO_POSITION) {
            currentPage = firstVisibleItem
            updatePageInfo()
        }
    }

    private fun updatePageInfo() {
        val pageText = getString(R.string.page_info_format, currentPage + 1, totalPages)
        binding.pageInfoTextView.text = pageText

        // Update navigation buttons
        binding.previousPageButton.isEnabled = currentPage > 0
        binding.nextPageButton.isEnabled = currentPage < totalPages - 1
    }

    private fun toggleZoom() {
        zoomLevel = when {
            zoomLevel < 1.0f -> 1.0f
            zoomLevel < 1.5f -> 1.5f
            zoomLevel < 2.0f -> 2.0f
            zoomLevel < maxZoomLevel -> maxZoomLevel
            else -> minZoomLevel
        }

        // Update zoom icon
        val zoomIcon = when {
            zoomLevel <= 1.0f -> R.drawable.ic_zoom_in
            else -> R.drawable.ic_zoom_out
        }
        binding.zoomFab.setImageResource(zoomIcon)

        // Reload pages with new zoom level
        loadPages()
    }

    private fun sharePDF() {
        intent.data?.let { uri ->
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(shareIntent, "Share PDF"))
        }
    }

    private fun showFileInfo() {
        intent.data?.let { uri ->
            // You can implement a dialog showing file information
            Toast.makeText(this, "File: ${uri.lastPathSegment}", Toast.LENGTH_LONG).show()
        }
    }

    private fun closePdfRenderer() {
        try {
            pdfRenderer?.close()
            parcelFileDescriptor?.close()
        } catch (e: IOException) {
            // Handle silently
        } finally {
            pdfRenderer = null
            parcelFileDescriptor = null
        }
    }
}