package com.grohon.pdfreader.adapters

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.grohon.pdfreader.databinding.ItemPdfPageBinding
import com.grohon.pdfreader.models.PDFPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PDFPagesAdapter(
    private val onPageVisible: (Int) -> Unit
) : RecyclerView.Adapter<PDFPagesAdapter.PDFPageViewHolder>() {

    private val pages = mutableListOf<PDFPage>()
    private var pdfRenderer: PdfRenderer? = null
    private var zoomLevel: Float = 1.0f
    private var lifecycleScope: LifecycleCoroutineScope? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PDFPageViewHolder {
        val binding = ItemPdfPageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PDFPageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PDFPageViewHolder, position: Int) {
        holder.bind(pages[position])
    }

    override fun getItemCount(): Int = pages.size

    fun updatePages(newPages: List<PDFPage>, renderer: PdfRenderer, zoom: Float) {
        pages.clear()
        pages.addAll(newPages)
        pdfRenderer = renderer
        zoomLevel = zoom
        notifyDataSetChanged()
    }

    fun setLifecycleScope(scope: LifecycleCoroutineScope) {
        lifecycleScope = scope
    }

    inner class PDFPageViewHolder(
        private val binding: ItemPdfPageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(page: PDFPage) {
            binding.apply {
                pageNumberTextView.text = (page.pageNumber + 1).toString()

                if (page.isLoaded && page.bitmap != null) {
                    pageImageView.setImageBitmap(page.bitmap)
                    pageLoadingProgressBar.visibility = View.GONE
                } else {
                    pageImageView.setImageBitmap(null)
                    pageLoadingProgressBar.visibility = View.VISIBLE
                    loadPageBitmap(page)
                }

                // Notify when page becomes visible
                onPageVisible(page.pageNumber)
            }
        }

        private fun loadPageBitmap(page: PDFPage) {
            pdfRenderer?.let { renderer ->
                lifecycleScope?.launch {
                    try {
                        val bitmap = withContext(Dispatchers.IO) {
                            generatePageBitmap(renderer, page.pageNumber)
                        }

                        withContext(Dispatchers.Main) {
                            page.bitmap = bitmap
                            page.isLoaded = true

                            binding.pageImageView.setImageBitmap(bitmap)
                            binding.pageLoadingProgressBar.visibility = View.GONE
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            binding.pageLoadingProgressBar.visibility = View.GONE
                            // Could show error state here
                        }
                    }
                }
            }
        }

        private fun generatePageBitmap(renderer: PdfRenderer, pageIndex: Int): Bitmap {
            val page = renderer.openPage(pageIndex)

            // Calculate bitmap dimensions based on zoom level
            val baseWidth = (page.width * zoomLevel).toInt()
            val baseHeight = (page.height * zoomLevel).toInt()

            // Ensure minimum dimensions
            val width = maxOf(baseWidth, 400)
            val height = maxOf(baseHeight, 600)

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.eraseColor(android.graphics.Color.WHITE)

            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()

            return bitmap
        }
    }
}