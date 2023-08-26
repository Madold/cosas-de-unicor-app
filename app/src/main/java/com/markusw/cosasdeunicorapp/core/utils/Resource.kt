package com.markusw.cosasdeunicorapp.core.utils

import com.markusw.cosasdeunicorapp.core.presentation.UiText

sealed class Resource<T>(val data: T? = null, var message: UiText? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: UiText) : Resource<T>(null, message)
}
