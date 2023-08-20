package com.markusw.cosasdeunicorapp.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TextUtils {

    fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp)
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(date)
    }

}