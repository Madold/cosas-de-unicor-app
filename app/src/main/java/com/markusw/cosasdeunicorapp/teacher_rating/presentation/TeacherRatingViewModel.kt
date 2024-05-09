package com.markusw.cosasdeunicorapp.teacher_rating.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.cosasdeunicorapp.core.DispatcherProvider
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.domain.repository.AuthRepository
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.Review
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherRating
import com.markusw.cosasdeunicorapp.teacher_rating.domain.model.TeacherReview
import com.markusw.cosasdeunicorapp.teacher_rating.domain.repository.TeacherRatingRepository
import com.markusw.cosasdeunicorapp.teacher_rating.domain.use_cases.GetTeacherAverageRating
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherRatingViewModel @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val teacherRatingRepository: TeacherRatingRepository,
    private val authRepository: AuthRepository,
    private val getTeacherAverageRating: GetTeacherAverageRating
) : ViewModel() {

    private val _uiState = MutableStateFlow(TeacherRatingState())
    private val _teachers = teacherRatingRepository.getTeachers()
    val uiState = combine(_uiState, _teachers) { state, teachers ->
        val updatedSelectedTeacher =
            teachers.find { it.id == state.selectedTeacher.id } ?: state.selectedTeacher

        val filteredTeachers = if (state.teacherNameQuery.isBlank()) {
            emptyList()
        } else {
            teachers.filter { it.teacherName.contains(state.teacherNameQuery, ignoreCase = true) }
        }

        val teachersListWithFilter: List<TeacherReview> = when (state.filterType) {
            is FilterType.ByNameAscending ->  teachers.sortedBy { it.teacherName }
            is FilterType.ByNameDescending -> teachers.sortedByDescending { it.teacherName }
            is FilterType.ByRating -> teachers.filter { getTeacherAverageRating(it) == state.filterType.rating }
            is FilterType.Default -> teachers
        }

        state.copy(
            teachers = teachersListWithFilter,
            selectedTeacher = updatedSelectedTeacher,
            filteredTeachers = filteredTeachers
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        TeacherRatingState()
    )
    private val eventsChannel = Channel<TeacherRatingViewModelEvent>()
    val events = eventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val loggedUser = when (val result = authRepository.getLoggedUser()) {
                is Result.Error -> User()
                is Result.Success -> result.data ?: User()
            }

            updateUiState {
                copy(
                    loggedUser = loggedUser
                )
            }
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
                    updateUiState { copy(isSavingReview = true) }

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

                    when (val result =
                        teacherRatingRepository.saveReview(review, selectedTeacher.id)) {
                        is Result.Error -> {

                        }

                        is Result.Success -> {

                        }
                    }

                    updateUiState { copy(isSavingReview = false) }
                }

            }

            is TeacherRatingEvent.ChangeSelectedTeacher -> {
                updateUiState { copy(selectedTeacher = event.teacher) }
            }

            is TeacherRatingEvent.DeleteReview -> {
                updateUiState { copy(isDeletingReview = true) }

                viewModelScope.launch(dispatchers.io) {
                    val teacherId = _uiState.value.selectedTeacher.id

                    when (val result =
                        teacherRatingRepository.deleteReview(event.review, teacherId)) {
                        is Result.Error -> {
                            eventsChannel.send(
                                TeacherRatingViewModelEvent.ReviewSaveError(
                                    result.message ?: UiText.DynamicString("Unknown error")
                                )
                            )
                        }

                        is Result.Success -> {
                            eventsChannel.send(TeacherRatingViewModelEvent.ReviewDeletedSuccessfully)

                            val teacher = _uiState.value.selectedTeacher
                            val reviews =
                                teacher.reviews.filter { it.author.uid != event.review.author.uid }

                            updateUiState {
                                copy(
                                    selectedTeacher = teacher.copy(reviews = reviews)
                                )
                            }
                        }
                    }

                    updateUiState { copy(isDeletingReview = false) }
                }


            }

            is TeacherRatingEvent.ChangeTeacherNameQuery -> {
                updateUiState { copy(teacherNameQuery = event.query)}
            }

            is TeacherRatingEvent.HideSearchBar -> {
                updateUiState { copy(isSearchBarActive = false) }
            }
            is TeacherRatingEvent.SearchTeachers -> {

            }
            is TeacherRatingEvent.ShowSearchBar -> {
                updateUiState { copy(isSearchBarActive = true) }
            }

            is TeacherRatingEvent.ToggleReviewLike -> {
                viewModelScope.launch(dispatchers.io) {
                    teacherRatingRepository.toggleReviewLike(event.teacherId, event.authorId)
                }
            }

            is TeacherRatingEvent.ToggleReviewDislike -> {
                viewModelScope.launch(dispatchers.io) {
                    teacherRatingRepository.toggleReviewDislike(event.teacherId, event.authorId)
                }
            }

            TeacherRatingEvent.HideTeacherFiltersDialog -> {
                updateUiState {
                    copy(isTeacherFiltersDialogVisible = false)
                }
            }
            TeacherRatingEvent.ShowTeacherFiltersDialog -> {
                updateUiState {
                    copy(isTeacherFiltersDialogVisible = true)
                }
            }

            is TeacherRatingEvent.ChangeFilterType -> {
                if (event.filterType == _uiState.value.filterType) {
                    updateUiState { copy(
                        filterType = FilterType.Default
                    )}
                    return
                }

                updateUiState {
                    copy(filterType = event.filterType)
                }
            }

            is TeacherRatingEvent.ChangeNameOrderDropDownMenuExpanded -> {
                updateUiState {
                    copy(isNameOrderDropDownMenuExpanded = event.isExpanded)
                }
            }
            is TeacherRatingEvent.ChangeNameOrderDropDownMenuOption -> {
                updateUiState {
                    copy(selectedNameOrderOption = event.option)
                }
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

    override fun onCleared() {
        super.onCleared()
        eventsChannel.close()
    }

}