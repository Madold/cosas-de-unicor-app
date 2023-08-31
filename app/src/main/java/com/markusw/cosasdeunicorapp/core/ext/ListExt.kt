package com.markusw.cosasdeunicorapp.core.ext

fun <T> List<T>.prepend(elements: List<T>): List<T> {
    return elements + this
}

fun <T> List<T>.prepend(element: T): List<T> {
    return listOf(element) + this
}