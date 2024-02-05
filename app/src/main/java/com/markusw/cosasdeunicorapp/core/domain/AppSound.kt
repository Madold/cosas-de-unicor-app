package com.markusw.cosasdeunicorapp.core.domain

import android.support.annotation.RawRes
import com.markusw.cosasdeunicorapp.R

sealed class AppSound(
    @RawRes val resource: Int
) {

    data object MessageSent: AppSound(R.raw.multi_pop)
    data object MessageReceived: AppSound(R.raw.happy_pop)
    data object Like: AppSound(R.raw.like)

}