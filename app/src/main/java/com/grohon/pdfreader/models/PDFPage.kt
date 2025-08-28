package com.grohon.pdfreader.models

import android.graphics.Bitmap

data class PDFPage(
    val pageNumber: Int,
    var bitmap: Bitmap? = null,
    var isLoaded: Boolean = false
)