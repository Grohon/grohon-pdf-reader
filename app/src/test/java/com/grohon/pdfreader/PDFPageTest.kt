package com.grohon.pdfreader

import com.grohon.pdfreader.models.PDFPage
import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for PDFPage model class.
 */
class PDFPageTest {

    @Test
    fun pdfPage_creation_isCorrect() {
        // Given
        val pageNumber = 1
        val bitmap = null // Bitmap would be null in unit tests
        val isLoaded = false

        // When
        val pdfPage = PDFPage(pageNumber, bitmap, isLoaded)

        // Then
        assertEquals(pageNumber, pdfPage.pageNumber)
        assertEquals(bitmap, pdfPage.bitmap)
        assertEquals(isLoaded, pdfPage.isLoaded)
    }

    @Test
    fun pdfPage_equality_isCorrect() {
        // Given
        val pageNumber = 1
        val bitmap = null
        val isLoaded = false

        val pdfPage1 = PDFPage(pageNumber, bitmap, isLoaded)
        val pdfPage2 = PDFPage(pageNumber, bitmap, isLoaded)

        // Then
        assertEquals(pdfPage1, pdfPage2)
    }

    @Test
    fun pdfPage_hashCode_isConsistent() {
        // Given
        val pageNumber = 1
        val bitmap = null
        val isLoaded = false

        val pdfPage1 = PDFPage(pageNumber, bitmap, isLoaded)
        val pdfPage2 = PDFPage(pageNumber, bitmap, isLoaded)

        // Then
        assertEquals(pdfPage1.hashCode(), pdfPage2.hashCode())
    }

    @Test
    fun pdfPage_defaultValues_areCorrect() {
        // Given
        val pageNumber = 5

        // When
        val pdfPage = PDFPage(pageNumber)

        // Then
        assertEquals(pageNumber, pdfPage.pageNumber)
        assertEquals(null, pdfPage.bitmap)
        assertEquals(false, pdfPage.isLoaded)
    }
}