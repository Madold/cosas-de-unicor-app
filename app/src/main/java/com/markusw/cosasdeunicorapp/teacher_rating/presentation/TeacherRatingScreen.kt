@file:OptIn(ExperimentalMaterial3Api::class)

package com.markusw.cosasdeunicorapp.teacher_rating.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.ext.pop
import com.markusw.cosasdeunicorapp.core.presentation.AppTopBar
import com.markusw.cosasdeunicorapp.core.presentation.Screens
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables.AZ
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables.TeacherFiltersDialog
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables.TeachersList
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.composables.ZA
import com.markusw.cosasdeunicorapp.ui.theme.CosasDeUnicorAppTheme
import com.markusw.cosasdeunicorapp.ui.theme.home_bottom_bar_background

@Composable
fun TeacherRatingScreen(
    state: TeacherRatingState,
    onEvent: (TeacherRatingEvent) -> Unit,
    mainNavController: NavController
) {
    Scaffold(
        topBar = {
            Box {
                AppTopBar(
                    title = {
                        Text(
                            text = "Calificar docentes", color = Color.White,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { mainNavController.pop() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_left),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { onEvent(TeacherRatingEvent.ShowSearchBar) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        IconButton(onClick = { onEvent(TeacherRatingEvent.ShowTeacherFiltersDialog) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_filter),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                )

                SearchBar(
                    modifier = Modifier.run {
                        if (state.isSearchBarActive) {
                            this.fillMaxSize()
                        } else {
                            this.width(0.dp)
                        }
                    },
                    colors = SearchBarDefaults.colors(
                        containerColor = home_bottom_bar_background,
                        inputFieldColors = SearchBarDefaults.inputFieldColors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    ), query = state.teacherNameQuery,
                    onQueryChange = {
                        onEvent(TeacherRatingEvent.ChangeTeacherNameQuery(it))
                    },
                    onSearch = {
                        onEvent(TeacherRatingEvent.ChangeTeacherNameQuery(it))
                        onEvent(TeacherRatingEvent.SearchTeachers)
                    },
                    active = state.isSearchBarActive,
                    onActiveChange = { isActive ->
                        onEvent(
                            if (isActive) TeacherRatingEvent.ShowSearchBar
                            else TeacherRatingEvent.HideSearchBar
                        )
                    },
                    leadingIcon = {
                        IconButton(onClick = {
                            onEvent(TeacherRatingEvent.SearchTeachers)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            onEvent(TeacherRatingEvent.HideSearchBar)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    content = {
                        LazyColumn(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            items(state.filteredTeachers) { teacher ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            onEvent(TeacherRatingEvent.ChangeSelectedTeacher(teacher))
                                            mainNavController.navigate(Screens.TeacherRatingDetail.route)
                                        }
                                        .padding(16.dp),
                                ) {
                                    Text(
                                        text = teacher.teacherName,
                                        color = Color.White,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = state.isLoadingReviews,
                label = "loading_transition",
                transitionSpec = { fadeIn() togetherWith fadeOut() }
            ) { isLoadingReviews ->
                if (isLoadingReviews) {
                    LoadingAnimation()
                } else {
                    TeachersList(
                        state = state,
                        onEvent = onEvent,
                        modifier = Modifier.fillMaxSize(),
                        mainNavController = mainNavController
                    )
                    if (state.isTeacherFiltersDialogVisible) {
                        TeacherFiltersDialog(
                            onDismiss = {
                                onEvent(TeacherRatingEvent.HideTeacherFiltersDialog)
                            },
                            onConfirm = {
                                onEvent(TeacherRatingEvent.HideTeacherFiltersDialog)
                            },
                            onChipClick = {
                                onEvent(TeacherRatingEvent.ChangeFilterType(FilterType.ByRating(it)))
                            },
                            selectedFilterType = state.filterType,
                            isDropDownMenuExpanded = state.isNameOrderDropDownMenuExpanded,
                            onExpandedChange = {
                                onEvent(TeacherRatingEvent.ChangeNameOrderDropDownMenuExpanded(it))
                            },
                            onDropDownMenuOptionChange = { option ->
                                onEvent(TeacherRatingEvent.ChangeNameOrderDropDownMenuOption(option))
                                when (option) {
                                    AZ -> {
                                        onEvent(TeacherRatingEvent.ChangeFilterType(FilterType.ByNameAscending))
                                    }

                                    ZA -> {
                                        onEvent(TeacherRatingEvent.ChangeFilterType(FilterType.ByNameDescending))
                                    }
                                }
                            },
                            selectedOption = state.selectedNameOrderOption
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TeacherRatingScreenPreview() {

    CosasDeUnicorAppTheme {
        TeacherRatingScreen(
            state = TeacherRatingState(),
            onEvent = {},
            mainNavController = rememberNavController()
        )
    }
}

@Composable
private fun LoadingAnimation() {

    val lottieComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading_animation))
    val progress by animateLottieCompositionAsState(
        composition = lottieComposition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = lottieComposition,
        progress = { progress }
    )

}