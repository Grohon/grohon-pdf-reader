package com.grohon.pdfreader.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grohon.pdfreader.databinding.ItemRecentFileBinding
import com.grohon.pdfreader.models.RecentFile
import java.text.SimpleDateFormat
import java.util.*

class RecentFilesAdapter(
    private val onFileClick: (RecentFile) -> Unit
) : RecyclerView.Adapter<RecentFilesAdapter.RecentFileViewHolder>() {

    private val recentFiles = mutableListOf<RecentFile>()
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentFileViewHolder {
        val binding = ItemRecentFileBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecentFileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentFileViewHolder, position: Int) {
        holder.bind(recentFiles[position])
    }

    override fun getItemCount(): Int = recentFiles.size

    fun updateFiles(newFiles: List<RecentFile>) {
        recentFiles.clear()
        recentFiles.addAll(newFiles)
        notifyDataSetChanged()
    }

    inner class RecentFileViewHolder(
        private val binding: ItemRecentFileBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recentFile: RecentFile) {
            binding.apply {
                fileNameTextView.text = recentFile.fileName
                filePathTextView.text = getShortPath(recentFile.filePath)
                fileSizeTextView.text = recentFile.getFormattedFileSize()

                root.setOnClickListener {
                    onFileClick(recentFile)
                }
            }
        }

        private fun getShortPath(fullPath: String): String {
            return if (fullPath.length > 50) {
                "..." + fullPath.takeLast(47)
            } else {
                fullPath
            }
        }
    }
}