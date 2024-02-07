package com.markusw.cosasdeunicorapp.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TextUtils {

    const val PDF = "pdf"
    const val DOCX = "docx"
    const val XLSX = "xlsx"

    fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp)
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(date)
    }

    fun formatDate(timestamp: Long): String {
        val date = Date(timestamp)
        val dateFormat = SimpleDateFormat("MMMM d h:mm a", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun getFileExtensionFromName(fileName: String): String {
        val lastDotIndex = fileName.lastIndexOf(".")
        return if (lastDotIndex == -1 || lastDotIndex == fileName.length - 1) {
            ""
        } else {
            fileName.substring(lastDotIndex + 1)
        }
    }

}