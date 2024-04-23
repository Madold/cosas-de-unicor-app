package com.markusw.cosasdeunicorapp.teacher_rating.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.cosasdeunicorapp.core.DispatcherProvider
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.domain.repository.AuthRepository
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.Review
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating
import com.markusw.cosasdeunicorapp.teacher_rating.domain.repository.TeacherRatingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherRatingViewModel @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val teacherRatingRepository: TeacherRatingRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TeacherRatingState())
    private val eventsChannel = Channel<TeacherRatingViewModelEvent>()
    val events = eventsChannel.receiveAsFlow()
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {

            val loggedUser = when (val result = authRepository.getLoggedUser()) {
                is Result.Error -> User()
                is Result.Success -> result.data ?: User()
            }

            updateUiState { copy(
                isLoadingReviews = true,
                loggedUser = loggedUser
            )}
            delay(2400)
            val teachers = teacherRatingRepository.getTeachers()

            updateUiState { copy(
                teachers = teachers,
                isLoadingReviews = false
            ) }
        }
    }

    fun onEvent(event: TeacherRatingEvent) {
        when (event) {
            is TeacherRatingEvent.ChangeTeacherRating -> {
               updateUiState { copy(selectedTeacherRating = event.teacherRating) }
            }

            is TeacherRatingEvent.ChangeUserOpinion -> {
                updateUiState { copy(userOpinion = event.opinion) }
            }

            is TeacherRatingEvent.SubmitRating -> {
                val selectedTeacherRating = _uiState.value.selectedTeacherRating
                val userOpinion = _uiState.value.userOpinion
                val selectedTeacher = _uiState.value.selectedTeacher

                viewModelScope.launch(dispatchers.io) {
                   updateUiState { copy(isSavingReview = true)}

                    val loggedUser = when (val result = authRepository.getLoggedUser()) {
                        is Result.Error -> User()
                        is Result.Success -> result.data
                    }

                    val review = Review(
                        content = userOpinion,
                        vote = selectedTeacherRating,
                        author = loggedUser!!,
                        timestamp = System.currentTimeMillis()
                    )

                    when (val result = teacherRatingRepository.saveReview(review, selectedTeacher.id)) {
                        is Result.Error -> {

                        }
                        is Result.Success -> {
                            updateUiState {
                                copy(
                                    selectedTeacher = selectedTeacher.copy(
                                        reviews = selectedTeacher.reviews + review
                                    )
                                )
                            }
                        }
                    }

                    updateUiState { copy(isSavingReview = false) }
                    refreshTeachersList()
                }

            }

            is TeacherRatingEvent.ChangeSelectedTeacher -> {
                updateUiState { copy(selectedTeacher = event.teacher) }
            }
        }
    }

    private fun updateUiState(update: TeacherRatingState.() -> TeacherRatingState) {
        _uiState.update(update)
    }

    private fun resetFields() {
        updateUiState {
            copy(
                selectedTeacherRating = TeacherRating.Supportive,
                userOpinion = ""
            )
        }
    }

    private fun refreshTeachersList() {
        viewModelScope.launch {
            updateUiState { copy(isLoadingReviews = true) }
            val teachers = teacherRatingRepository.getTeachers()
            updateUiState { copy(teachers = teachers, isLoadingReviews = false) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        eventsChannel.close()
    }

}