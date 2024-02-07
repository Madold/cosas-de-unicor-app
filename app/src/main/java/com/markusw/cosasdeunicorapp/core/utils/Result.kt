package com.markusw.cosasdeunicorapp.core.utils

import com.markusw.cosasdeunicorapp.core.presentation.UiText

sealed class Result<T>(val data: T? = null, var message: UiText? = null) {
    class Success<T>(data: T) : Result<T>(data)
    class Error<T>(message: UiText) : Result<T>(null, message)
}
