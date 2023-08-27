package com.markusw.cosasdeunicorapp.core.ext

fun <T> List<T>.prepend(elements: List<T>): List<T> {
    return elements + this
}