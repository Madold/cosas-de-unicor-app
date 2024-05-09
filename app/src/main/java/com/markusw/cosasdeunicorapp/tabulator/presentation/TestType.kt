package com.markusw.cosasdeunicorapp.tabulator.presentation

sealed class TestType(
    val label: String
) {

    companion object {
        val entries by lazy { listOf(Before2005, After2005, After2014) }
    }

    data object Before2005 : TestType("2000-1/2005-II")

    data object After2005 : TestType("2006-I/2014-I")

    data object After2014 : TestType("2014-II en adelante")

}