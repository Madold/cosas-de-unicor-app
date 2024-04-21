package com.markusw.cosasdeunicorapp.teacher_rating.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.cosasdeunicorapp.core.DispatcherProvider
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.domain.repository.AuthRepository
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.Review
import com.markusw.cosasdeunicorapp.teacher_rating.domain.repository.TeacherRatingRepository
import com.markusw.cosasdeunicorapp.teacher_rating.domain.use_cases.ValidateTeacherRating
import com.markusw.cosasdeunicorapp.teacher_rating.domain.use_cases.ValidateUserOpinion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class TeacherRatingViewModel @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val teacherRatingRepository: TeacherRatingRepository,
    private val validateTeacherRating: ValidateTeacherRating,
    private val validateUserOpinion: ValidateUserOpinion,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TeacherRatingState())
    private val eventsChannel = Channel<TeacherRatingViewModelEvent>()
    val events = eventsChannel.receiveAsFlow()
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {

            updateUiState { copy(isLoadingReviews = true)}

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
                val teacherRatingValidationResult = validateTeacherRating(selectedTeacherRating)
                val userOpinionValidationResult = validateUserOpinion(userOpinion)
                val isAnyError = listOf(
                    teacherRatingValidationResult,
                    userOpinionValidationResult
                ).any { !it.successful }


                if (isAnyError) {
                    _uiState.update { state ->
                        state.copy(
                            ratingError = teacherRatingValidationResult.errorMessage,
                            opinionError = userOpinionValidationResult.errorMessage
                        )
                    }
                    return
                }
                clearErrors()

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

                    teacherRatingRepository.saveReview(review, selectedTeacher.id)

                    updateUiState { copy(isSavingReview = false)}
                }

            }

            is TeacherRatingEvent.ChangeSelectedTeacher -> {
                Timber.d("Selected teacher: ${event.teacher}")
                updateUiState { copy(selectedTeacher = event.teacher) }
            }
        }
    }

    private fun clearErrors() {
        _uiState.update { it.copy(ratingError = null, opinionError = null) }
    }

    private fun updateUiState(update: TeacherRatingState.() -> TeacherRatingState) {
        _uiState.update(update)
    }

    override fun onCleared() {
        super.onCleared()
        eventsChannel.close()
    }

}