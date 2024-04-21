package com.markusw.cosasdeunicorapp.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.findNavController
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.ext.pop
import com.markusw.cosasdeunicorapp.core.ext.sharedViewModel
import com.markusw.cosasdeunicorapp.core.presentation.GoogleAuthClient
import com.markusw.cosasdeunicorapp.core.presentation.Screens
import com.markusw.cosasdeunicorapp.profile.presentation.ChangePasswordScreen
import com.markusw.cosasdeunicorapp.profile.presentation.EditProfileScreen
import com.markusw.cosasdeunicorapp.profile.presentation.ProfileScreen
import com.markusw.cosasdeunicorapp.profile.presentation.ProfileViewModel
import com.markusw.cosasdeunicorapp.profile.presentation.ProfileViewModelEvent
import com.markusw.cosasdeunicorapp.tabulator.presentation.TabulatorScreen
import com.markusw.cosasdeunicorapp.tabulator.presentation.TabulatorViewModel
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.TeacherRatingScreen
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.TeacherRatingViewModel
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.TeacherDetailsScreen
import com.markusw.cosasdeunicorapp.teacher_rating.presentation.TeacherRatingViewModelEvent
import com.markusw.cosasdeunicorapp.ui.theme.CosasDeUnicorAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var composeView: ComposeView
    private val navController by lazy { findNavController() }
    private val googleAuthClient by lazy { GoogleAuthClient(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

                val mainNavController = rememberNavController()
                val homeState by viewModel.uiState.collectAsStateWithLifecycle()

                CosasDeUnicorAppTheme(
                    darkTheme = homeState.localSettings.isDarkModeEnabled,
                    dynamicColor = false
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {

                        NavHost(
                            navController = mainNavController,
                            startDestination = Screens.Home.route
                        ) {

                            composable(
                                route = Screens.Home.route,
                                popEnterTransition = {
                                    slideInHorizontally(
                                        initialOffsetX = { fullWidth -> -fullWidth }
                                    )
                                },
                                popExitTransition = {
                                    slideOutHorizontally(
                                        targetOffsetX = { fullWidth -> -fullWidth }
                                    )
                                }
                            ) {
                                HomeScreen(
                                    mainNavController = mainNavController,
                                    viewModel = viewModel
                                )
                            }

                            composable(
                                route = Screens.Profile.route,
                                enterTransition = {
                                    slideInHorizontally(
                                        initialOffsetX = { fullWidth -> fullWidth }
                                    )
                                },
                                popExitTransition = {
                                    slideOutHorizontally { fullWidth -> fullWidth }
                                }
                            ) {
                                val viewModel = hiltViewModel<ProfileViewModel>()
                                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                                ProfileScreen(
                                    mainNavController = mainNavController,
                                    state = uiState,
                                    onEvent = viewModel::onEvent
                                )
                            }

                            composable(
                                route = Screens.EditProfile.route,
                                enterTransition = {
                                    slideInHorizontally(
                                        initialOffsetX = { fullWidth -> fullWidth }
                                    )
                                },
                                popExitTransition = {
                                    slideOutHorizontally { fullWidth -> fullWidth }
                                }

                            ) {

                                val viewModel = hiltViewModel<ProfileViewModel>()
                                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                                LaunchedEffect(key1 = viewModel.events) {
                                    viewModel.events.collectLatest { event ->
                                        when (event) {
                                            ProfileViewModelEvent.ProfileUpdatedError -> TODO()
                                            ProfileViewModelEvent.ProfileUpdatedSuccess -> {
                                                mainNavController.pop()
                                            }

                                            else -> return@collectLatest
                                        }
                                    }
                                }

                                EditProfileScreen(
                                    mainNavController = mainNavController,
                                    state = uiState,
                                    onEvent = viewModel::onEvent
                                )
                            }

                            composable(
                                route = Screens.ResetPassword.route,
                                enterTransition = {
                                    slideInHorizontally(
                                        initialOffsetX = { fullWidth -> fullWidth }
                                    )
                                },
                                popExitTransition = {
                                    slideOutHorizontally { fullWidth -> fullWidth }
                                }
                            ) {

                                val viewModel = hiltViewModel<ProfileViewModel>()
                                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                                LaunchedEffect(key1 = viewModel.events) {
                                    viewModel.events.collectLatest { event ->
                                        when (event) {
                                            ProfileViewModelEvent.PasswordResetSentSuccess -> {
                                                mainNavController.pop()
                                            }

                                            else -> return@collectLatest
                                        }
                                    }
                                }

                                ChangePasswordScreen(
                                    mainNavController = mainNavController,
                                    state = uiState,
                                    onEvent = viewModel::onEvent
                                )
                            }

                            composable(Screens.Tabulator.route) {
                                val viewModel = hiltViewModel<TabulatorViewModel>()
                                val state by viewModel.uiState.collectAsStateWithLifecycle()

                                TabulatorScreen(
                                    mainNavController = mainNavController,
                                    state = state,
                                    onEvent = viewModel::onEvent
                                )
                            }

                            navigation(
                                route = Screens.TeacherRating.route,
                                startDestination = "${Screens.TeacherRating.route}/teachers"
                            ) {
                                composable("${Screens.TeacherRating.route}/teachers") { backStackEntry ->

                                    val viewModel = backStackEntry.sharedViewModel<TeacherRatingViewModel>(navController = mainNavController)
                                    val state by viewModel.uiState.collectAsStateWithLifecycle()

                                    TeacherRatingScreen(
                                        onEvent = viewModel::onEvent,
                                        mainNavController = mainNavController,
                                        state = state
                                    )
                                }

                                composable(route = Screens.TeacherRatingDetail.route) { backStackEntry ->

                                    val viewModel = backStackEntry.sharedViewModel<TeacherRatingViewModel>(navController = mainNavController)
                                    val state by viewModel.uiState.collectAsStateWithLifecycle()

                                    TeacherDetailsScreen(
                                        state = state,
                                        onEvent = viewModel::onEvent,
                                        mainNavController = mainNavController
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.homeEvents.collect { homeEvent ->
                when (homeEvent) {
                    is HomeEvents.LogoutSuccessful -> {
                        Timber.d("Logout successful")
                        googleAuthClient.signOut()
                        navController.navigate(R.id.action_homeFragment_to_loginFragment)
                    }
                }
            }
        }
    }


}