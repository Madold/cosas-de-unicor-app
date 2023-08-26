package com.markusw.cosasdeunicorapp.core.utils

sealed class Resource<T>(val data: T? = null, var message: UiText? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: UiText) : Resource<T>(null, message)
}

//TODO: Change resource error string to UiText