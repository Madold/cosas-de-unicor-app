package com.markusw.cosasdeunicorapp.home.presentation

import android.Manifest
import android.app.Activity
import android.content.ContextWrapper
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.markusw.cosasdeunicorapp.core.ext.openAppSettings
import com.markusw.cosasdeunicorapp.core.presentation.PermissionDialog
import com.markusw.cosasdeunicorapp.core.presentation.ReadExternalStoragePermission
import com.markusw.cosasdeunicorapp.core.presentation.WriteExternalStoragePermission
import com.markusw.cosasdeunicorapp.home.presentation.chat.ChatScreenContent
import com.markusw.cosasdeunicorapp.home.presentation.composables.BottomNavigationBar
import com.markusw.cosasdeunicorapp.home.presentation.docs.DocsScreenContent
import com.markusw.cosasdeunicorapp.home.presentation.main.HomeScreenContent
import com.markusw.cosasdeunicorapp.home.presentation.more.MoreScreenContent
import com.markusw.cosasdeunicorapp.home.presentation.news.NewsScreenContent
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

/**
 * Permissions to request when the user tries to download a document.
 */
val permissionsToRequest = arrayOf(
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE
)

@Composable
fun HomeScreen(
    mainNavController: NavController,
    viewModel: HomeViewModel
) {

    val navController = rememberNavController()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Timber.d("Permission granted")
            } else {
                Timber.d("Permission denied")
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(
        content = {
            HomeHost(
                navController = navController,
                contentPadding = it,
                viewModel = viewModel,
                mainNavController = mainNavController
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController
            )
        }
    )
}

@Composable
private fun HomeHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    mainNavController: NavController,
    contentPadding: PaddingValues,
    viewModel: HomeViewModel
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = contentPadding.calculateTopPadding(),
                bottom = 0.dp,
                start = contentPadding.calculateStartPadding(
                    LayoutDirection.Ltr
                ),
                end = contentPadding.calculateEndPadding(
                    LayoutDirection.Ltr
                )
            ),
        navController = navController,
        startDestination = HomeScreens.Home.route
    ) {
        composable(route = HomeScreens.Home.route) {
            HomeScreenContent(
                state = uiState,
                onEvent = viewModel::onEvent,
                mainNavController = mainNavController,
                bottomBarNavController = navController,
                paddingValues = contentPadding
            )
        }

        composable(route = HomeScreens.Chat.route) {

            val scrollState = rememberLazyListState()

            LaunchedEffect(key1 = viewModel.chatListEvents) {
                viewModel.chatListEvents.collectLatest { chatListEvent ->
                    when (chatListEvent) {
                        is ChatListEvent.MessageAdded -> {
                            Timber.d("Message added scrolling to bottom")
                            scrollState.animateScrollToItem(0)
                        }
                    }
                }
            }

            ChatScreenContent(
                state = uiState,
                onEvent = viewModel::onEvent,
                scrollState = scrollState,
                paddingValues = contentPadding
            )
        }

        composable(route = HomeScreens.News.route) {

            val scrollState = rememberLazyListState()

            LaunchedEffect(key1 = viewModel.newsListEvents) {
                viewModel.newsListEvents.collectLatest { newsListEvent ->
                    when (newsListEvent) {
                        NewsListEvent.NewsAdded -> {
                            scrollState.animateScrollToItem(0)
                        }
                    }
                }
            }

            NewsScreenContent(
                state = uiState,
                onEvent = viewModel::onEvent,
                scrollState = scrollState,
                paddingValues = contentPadding
            )
        }

        composable(route = HomeScreens.Documents.route) {
            val context = LocalContext.current

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { permissions ->
                    permissionsToRequest.forEach { permission ->
                        viewModel.onPermissionResult(
                            permission = permission,
                            isGranted = permissions[permission] == true
                        )
                    }
                }
            )

            DocsScreenContent(
                onEvent = viewModel::onEvent,
                multiplePermissionLauncher = permissionLauncher,
                state = uiState,
                paddingValues = contentPadding
            )

            viewModel.visiblePermissionDialogQueue
                .asReversed()
                .forEach { permission ->
                    PermissionDialog(
                        permissionTextProvider = when (permission) {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE -> WriteExternalStoragePermission()
                            Manifest.permission.READ_EXTERNAL_STORAGE -> ReadExternalStoragePermission()
                            else -> return@forEach
                        },
                        isPermanentlyDeclined = ((context as ContextWrapper).baseContext as Activity).shouldShowRequestPermissionRationale(
                            permission
                        ).not(),
                        onDismiss = viewModel::dismissDialog,
                        onDone = {
                            viewModel.dismissDialog()
                            permissionLauncher.launch(arrayOf(permission))
                        },
                        onGotToSettings = {
                            viewModel.dismissDialog()
                            context.openAppSettings()
                        })
                }

        }

        composable(route = HomeScreens.More.route) {
            MoreScreenContent()
        }
    }
}

