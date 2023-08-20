package com.markusw.cosasdeunicorapp.core.utils

import java.util.Calendar

object TimeUtils {

    fun getDeviceHourInTimestamp(): Long {
        val currentTime = Calendar.getInstance().time

        return currentTime.time
    }

}