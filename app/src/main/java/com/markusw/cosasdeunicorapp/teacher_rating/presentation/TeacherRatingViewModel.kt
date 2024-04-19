package com.markusw.cosasdeunicorapp.teacher_rating.presentation

import androidx.lifecycle.ViewModel
import com.markusw.cosasdeunicorapp.core.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TeacherRatingViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(TeacherRatingState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: TeacherRatingEvent) {
        when (event) {
            else -> {}
        }
    }

}