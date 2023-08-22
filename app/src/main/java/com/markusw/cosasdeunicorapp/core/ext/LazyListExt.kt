package com.markusw.cosasdeunicorapp.core.ext

import androidx.compose.foundation.lazy.LazyListState

fun LazyListState.isScrolledToTheEnd(): Boolean {
    return this.layoutInfo.visibleItemsInfo.lastOrNull()?.index == this.layoutInfo.totalItemsCount - 1
}

fun LazyListState.isSecondLastItemVisible(): Boolean {
    return this.layoutInfo.visibleItemsInfo.lastOrNull()?.index == this.layoutInfo.totalItemsCount - 2
}